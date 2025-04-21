package com.client.ws.rasmooplus.service.impl;

import com.client.ws.rasmooplus.dto.UserDetailsDto;
import com.client.ws.rasmooplus.exception.NotFoundException;
import com.client.ws.rasmooplus.integration.MailIntegration;
import com.client.ws.rasmooplus.model.jpa.UserCredentials;
import com.client.ws.rasmooplus.model.redis.UserRecoveryCode;
import com.client.ws.rasmooplus.repository.jpa.UserDetailsRepository;
import com.client.ws.rasmooplus.repository.redis.UserRecoveryCodeRepository;
import com.client.ws.rasmooplus.service.CustomUserService;
import com.client.ws.rasmooplus.utils.PasswordUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Random;

@Service
public class UserDetailsServiceImpl implements UserDetailsService, CustomUserService {

  @Value("${webservices.rasplus.recoveryCode.timeout}")
  private String recoveryCodeTimeout;

  @Autowired
  private UserDetailsRepository userDetailsRepository;

  @Autowired
  private UserRecoveryCodeRepository userRecoveryCodeRepository;

  @Autowired
  private MailIntegration mailIntegration;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    return userDetailsRepository.findByUsername(username)
        .orElseThrow(() -> new UsernameNotFoundException("Usuário não encontrado"));
  }

  @Override
  public void sendRecoveryCode(String email) {

    UserRecoveryCode userRecoveryCode;
    String code = String.format("%04d", new Random().nextInt(10000));

    var userRecoveryCodeOpt = userRecoveryCodeRepository.findByEmail(email);

    if (userRecoveryCodeOpt.isEmpty()) {
      userDetailsRepository.findByUsername(email)
          .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

      userRecoveryCode = new UserRecoveryCode();
      userRecoveryCode.setEmail(email);
    } else {
      userRecoveryCode = userRecoveryCodeOpt.get();
    }
    userRecoveryCode.setCode(code);
    userRecoveryCode.setCreationDate(LocalDateTime.now());


    userRecoveryCodeRepository.save(userRecoveryCode);

    mailIntegration.send(email, "Código de recuperacao: " + code, "Recuperação de conta");
  }

  @Override
  public Boolean recoveryCodeIsValid(String recoveryCode, String email) {
    var userRecoveryCodeOpt = userRecoveryCodeRepository.findByEmail(email)
        .orElseThrow(() -> new NotFoundException("Usuário não encontrado"));

    LocalDateTime timeout = userRecoveryCodeOpt.getCreationDate().plusMinutes(Long.parseLong(recoveryCodeTimeout));
    LocalDateTime timeoutNow = LocalDateTime.now();

    return recoveryCode.equals(userRecoveryCodeOpt.getCode()) && timeoutNow.isBefore(timeout);
  }

  @Override
  public void updatedPasswordByRecoveryCode(UserDetailsDto dto) {
    if (recoveryCodeIsValid(dto.recoveryCode(), dto.email())) {
      var userDetails = userDetailsRepository.findByUsername(dto.email());

      UserCredentials userCredentials = userDetails.get();

      userCredentials.setPassword(PasswordUtils.encode(dto.password()));
      userDetailsRepository.save(userCredentials);
    }
  }
}
