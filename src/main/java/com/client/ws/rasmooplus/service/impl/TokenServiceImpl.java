package com.client.ws.rasmooplus.service.impl;

import com.client.ws.rasmooplus.model.UserCredentials;
import com.client.ws.rasmooplus.service.TokenService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Service
public class TokenServiceImpl implements TokenService {

  @Value("${webservices.rasplus.jwt.expiration}")
  private String expiration;

  @Value("${webservices.rasplus.jwt.secret}")
  private String secret;


  @Override
  public String getToken(Authentication auth) {

    UserCredentials user = (UserCredentials) auth.getPrincipal();
    Date today = new Date();
    Date expirationDate = new Date(today.getTime() + Long.parseLong(expiration));


    return Jwts.builder()
        .issuer("API Rasmoo Plus")
        .subject(user.getId().toString())
        .issuedAt(today)
        .expiration(expirationDate)
        .signWith(getKey(), Jwts.SIG.HS256)
        .compact();
  }

  @Override
  public Boolean isValid(String token) {
    try {
      getClaimsJws(token);
      return true;
    } catch (JwtException e) {
      return false;
    }
  }

  @Override
  public Long getUserId(String token) {
    Jws<Claims> claims = getClaimsJws(token);
    return Long.parseLong(claims.getPayload().getSubject());
  }

  private Jws<Claims> getClaimsJws(String token) {
    return Jwts.parser().verifyWith(getKey()).build().parseSignedClaims(token);
  }

  private SecretKey getKey() {
    return Keys.hmacShaKeyFor(Base64.getDecoder().decode(secret));
  }
}
