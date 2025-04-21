package com.client.ws.rasmooplus.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

  private PasswordUtils(){}

  public static String encode(String password) {
    return new BCryptPasswordEncoder().encode(password);
  }
}
