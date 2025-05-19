package com.client.ws.rasmooplus.utils;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

public class PasswordUtils {

  private PasswordUtils(){}

  private static final BCryptPasswordEncoder ENCODER = new BCryptPasswordEncoder();

  public static String encode(String password) {
    return ENCODER.encode(password);
  }

  public static boolean matches(String rawPassword, String encodedPassword) {
    return ENCODER.matches(rawPassword, encodedPassword);
  }
}
