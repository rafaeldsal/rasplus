package com.client.ws.rasmooplus.service.impl;

import com.client.ws.rasmooplus.dto.LoginDto;
import com.client.ws.rasmooplus.dto.TokenDto;
import com.client.ws.rasmooplus.exception.BadRequestException;
import com.client.ws.rasmooplus.service.AuthenticationService;
import com.client.ws.rasmooplus.service.TokenService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationServiceImpl implements AuthenticationService {

  @Autowired
  private AuthenticationManager authenticationManager;

  @Autowired
  private TokenService tokenService;

  @Override
  public TokenDto auth(LoginDto dto) {
    try {
      UsernamePasswordAuthenticationToken userPassAuth = new UsernamePasswordAuthenticationToken(dto.username(), dto.password());
      Authentication auth = authenticationManager.authenticate(userPassAuth);
      String token = tokenService.getToken(auth);
      return TokenDto.builder()
          .token(token)
          .type("Bearer")
          .build();
    } catch (Exception e) {
      throw new BadRequestException("Erro ao formatar token - " + e.getMessage());
    }
  }
}
