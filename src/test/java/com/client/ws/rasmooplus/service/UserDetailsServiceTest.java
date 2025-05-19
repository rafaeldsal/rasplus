package com.client.ws.rasmooplus.service;

import com.client.ws.rasmooplus.dto.UserDetailsDto;
import com.client.ws.rasmooplus.exception.NotFoundException;
import com.client.ws.rasmooplus.integration.MailIntegration;
import com.client.ws.rasmooplus.model.jpa.UserCredentials;
import com.client.ws.rasmooplus.model.jpa.UserType;
import com.client.ws.rasmooplus.model.redis.UserRecoveryCode;
import com.client.ws.rasmooplus.repository.jpa.UserDetailsRepository;
import com.client.ws.rasmooplus.repository.redis.UserRecoveryCodeRepository;
import com.client.ws.rasmooplus.service.impl.UserDetailsServiceImpl;
import com.client.ws.rasmooplus.utils.PasswordUtils;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserDetailsServiceTest {

  private static final String STUDENT_USERNAME = "rafael@email.com";
  private static final String RECOVERY_CODE = "1234";
  private static final String STUDENT_PASSWORD = PasswordUtils.encode("senha123");

  @Mock
  private UserDetailsRepository userDetailsRepository;

  @Mock
  private UserRecoveryCodeRepository userRecoveryCodeRepository;

  @Mock
  private MailIntegration mailIntegration;

  @InjectMocks
  private UserDetailsServiceImpl userDetailsService;

  @Test
  void given_loadUserByUsername_when_thereIsUsername_then_returnUserCredentials() {
    UserType userType = getUserType();
    UserCredentials userCredentials = getUserCredentials(userType);

    when(userDetailsRepository.findByUsername(STUDENT_USERNAME)).thenReturn(Optional.of(userCredentials));

    //Act
    UserDetails userDetails = userDetailsService.loadUserByUsername(STUDENT_USERNAME);

    // Assert
    Assertions.assertNotNull(userDetails);
    Assertions.assertEquals(STUDENT_USERNAME, userDetails.getUsername());
    Assertions.assertEquals(userCredentials.getPassword(), userDetails.getPassword());
    Assertions.assertEquals(userType, userCredentials.getUserType());

    verify(userDetailsRepository, times(1)).findByUsername(STUDENT_USERNAME);
  }

  @Test
  void given_loadUserByUsername_when_usernameNotExists_then_throwUsernameNotFoundException() {
    String username = "nonexistent@email.com";

    when(userDetailsRepository.findByUsername(username)).thenReturn(Optional.empty());

    Assertions.assertThrows(UsernameNotFoundException.class, () -> {
      userDetailsService.loadUserByUsername(username);
    });

    verify(userDetailsRepository, times(1)).findByUsername(username);
  }

  @Test
  void given_sendRecoveryCode_when_emailExistsInRedisDataBase_then_sendRecoveryEmail() {

    UserRecoveryCode userRecoveryCode = getUserRecoveryCode();

    when(userRecoveryCodeRepository.findByEmail(STUDENT_USERNAME)).thenReturn(Optional.of(userRecoveryCode));

    when(userRecoveryCodeRepository.save(any(UserRecoveryCode.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    userDetailsService.sendRecoveryCode(STUDENT_USERNAME);

    // Assert
    verify(userRecoveryCodeRepository, times(1)).save(any(UserRecoveryCode.class));

    verify(mailIntegration, times(1)).send(eq(STUDENT_USERNAME), anyString(), eq("Recuperação de conta"));
  }

  @Test
  void given_sendRecoveryCode_when_emailExistsInJpaDataBase_then_sendRecoveryEmail() {

    UserType userType = getUserType();
    UserCredentials userCredentials = getUserCredentials(userType);

    when(userRecoveryCodeRepository.findByEmail(STUDENT_USERNAME)).thenReturn(Optional.empty());
    when(userDetailsRepository.findByUsername(STUDENT_USERNAME)).thenReturn(Optional.of(userCredentials));
    when(userRecoveryCodeRepository.save(any(UserRecoveryCode.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    userDetailsService.sendRecoveryCode(STUDENT_USERNAME);

    // Assert
    verify(userRecoveryCodeRepository, times(1)).findByEmail(STUDENT_USERNAME);

    verify(userRecoveryCodeRepository, times(1)).save(any(UserRecoveryCode.class));

    verify(userDetailsRepository, times(1)).findByUsername(STUDENT_USERNAME);

    verify(mailIntegration, times(1)).send(eq(STUDENT_USERNAME), anyString(), any());
  }

  @Test
  void given_sendRecoveryCode_when_userNotFound_throwNotFoundException() {

    when(userRecoveryCodeRepository.findByEmail(STUDENT_USERNAME)).thenReturn(Optional.empty());
    when(userDetailsRepository.findByUsername(STUDENT_USERNAME)).thenReturn(Optional.empty());

    Assertions.assertThrows(NotFoundException.class, () -> userDetailsService.sendRecoveryCode(STUDENT_USERNAME));

    verify(userRecoveryCodeRepository, times(1)).findByEmail(STUDENT_USERNAME);
    verify(userDetailsRepository, times(1)).findByUsername(STUDENT_USERNAME);
    verify(userRecoveryCodeRepository, times(0)).save(any());
    verify(mailIntegration, times(0)).send(any(), anyString(), anyString());
  }

  @Test
  void given_recoveryCodeIsValid_when_userIsFound_then_returTrue() {
    ReflectionTestUtils.setField(userDetailsService, "recoveryCodeTimeout", "5");
    when(userRecoveryCodeRepository.findByEmail(STUDENT_USERNAME)).thenReturn(Optional.of(getUserRecoveryCode()));
    Assertions.assertTrue(userDetailsService.recoveryCodeIsValid(RECOVERY_CODE, STUDENT_USERNAME));

    verify(userRecoveryCodeRepository, times(1)).findByEmail(STUDENT_USERNAME);
  }

  @Test
  void given_recoveryCodeIsValid_when_codeIsInvalidButNotExpired_then_returnFalse() {
    ReflectionTestUtils.setField(userDetailsService, "recoveryCodeTimeout", "5");
    when(userRecoveryCodeRepository.findByEmail(STUDENT_USERNAME)).thenReturn(Optional.of(getUserRecoveryCode()));

    Assertions.assertFalse(userDetailsService.recoveryCodeIsValid("invalidCode", STUDENT_USERNAME));

    verify(userRecoveryCodeRepository, times(1)).findByEmail(STUDENT_USERNAME);
  }

  @Test
  void given_expiredRecoveryCode_when_userIsFound_then_returnFalse() {
    ReflectionTestUtils.setField(userDetailsService, "recoveryCodeTimeout", "5");
    UserRecoveryCode userRecoveryCode = mock(UserRecoveryCode.class);
    when(userRecoveryCode.getCreationDate()).thenReturn(LocalDateTime.now().minusMinutes(10));
    when(userRecoveryCode.getCode()).thenReturn(RECOVERY_CODE);
    when(userRecoveryCodeRepository.findByEmail(STUDENT_USERNAME)).thenReturn(Optional.of(userRecoveryCode));

    Assertions.assertFalse(userDetailsService.recoveryCodeIsValid(RECOVERY_CODE, STUDENT_USERNAME));

    verify(userRecoveryCodeRepository, times(1)).findByEmail(STUDENT_USERNAME);
  }

  @Test
  void given_recoveryCodeIsValid_when_userNotFound_then_throwNotFoundException() {
    when(userRecoveryCodeRepository.findByEmail(STUDENT_USERNAME)).thenReturn(Optional.empty());
    Assertions.assertThrows(NotFoundException.class, () -> userDetailsService.recoveryCodeIsValid(RECOVERY_CODE, STUDENT_USERNAME));
    verify(userRecoveryCodeRepository, times(1)).findByEmail(STUDENT_USERNAME);
  }

  @Test
  void given_updatedPasswordByRecoveryCode_when_userIsFound_then_updateCredentials() {
    ReflectionTestUtils.setField(userDetailsService, "recoveryCodeTimeout", "5");
    UserRecoveryCode userRecoveryCode = getUserRecoveryCode();

    when(userRecoveryCodeRepository.findByEmail(STUDENT_USERNAME)).thenReturn(Optional.of(userRecoveryCode));

    UserCredentials userCredentials = getUserCredentials(getUserType());
    when(userDetailsRepository.findByUsername(STUDENT_USERNAME)).thenReturn(Optional.of(userCredentials));
    when(userDetailsRepository.save(any(UserCredentials.class))).thenAnswer(invocation -> invocation.getArgument(0));

    UserDetailsDto dto = new UserDetailsDto(STUDENT_USERNAME, RECOVERY_CODE, "novaSenha123");
    userDetailsService.updatedPasswordByRecoveryCode(dto);

    verify(userDetailsRepository, times(1)).save(argThat(updatedUser ->
        PasswordUtils.matches("novaSenha123", updatedUser.getPassword())));

    verify(userDetailsRepository, times(1)).findByUsername(STUDENT_USERNAME);
  }

  @Test
  void given_updatedPasswordByRecoveryCode_when_userIsNotFound_then_throwNotFoundException() {
    when(userRecoveryCodeRepository.findByEmail(STUDENT_USERNAME)).thenReturn(Optional.empty());
    UserDetailsDto dto = new UserDetailsDto(STUDENT_USERNAME, RECOVERY_CODE, "novaSenha123");
    Assertions.assertThrows(NotFoundException.class, () -> userDetailsService.updatedPasswordByRecoveryCode(dto));
    verify(userRecoveryCodeRepository, times(1)).findByEmail(STUDENT_USERNAME);
  }

  @Test
  void given_updatedPasswordByRecoveryCode_when_recoveryCodeIsValidReturnFalse() {
    ReflectionTestUtils.setField(userDetailsService, "recoveryCodeTimeout", "5");
    UserRecoveryCode userRecoveryCode = mock(UserRecoveryCode.class);
    when(userRecoveryCode.getCreationDate()).thenReturn(LocalDateTime.now().minusMinutes(10));
    when(userRecoveryCode.getCode()).thenReturn(RECOVERY_CODE);
    when(userRecoveryCodeRepository.findByEmail(STUDENT_USERNAME)).thenReturn(Optional.of(userRecoveryCode));

    UserDetailsDto dto = new UserDetailsDto(STUDENT_USERNAME, RECOVERY_CODE, "novaSenha123");
    userDetailsService.updatedPasswordByRecoveryCode(dto);

    verify(userDetailsRepository, never()).findByUsername(any());
    verify(userDetailsRepository, never()).save(any());
  }

  private static UserType getUserType() {
    return new UserType(1L, "Aluno", "Aluno da plataforma");
  }

  private static UserCredentials getUserCredentials(UserType userType) {
    return new UserCredentials(1L, STUDENT_USERNAME, STUDENT_PASSWORD, userType);
  }

  private static UserRecoveryCode getUserRecoveryCode() {
    return new UserRecoveryCode(UUID.randomUUID().toString(), STUDENT_USERNAME, RECOVERY_CODE, LocalDateTime.now());
  }

}