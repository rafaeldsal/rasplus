package com.client.ws.rasmooplus.service;

import com.client.ws.rasmooplus.service.impl.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
public class UserServiceTest {

  @Autowired
  private UserDetailsServiceImpl userService;

  @Test
  void contextLoads() {
    userService.sendRecoveryCode("cornel4755@uorak.com");
  }
}
