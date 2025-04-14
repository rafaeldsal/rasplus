package com.client.ws.rasmooplus.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class MailIntegrationImplTest {

  @Autowired
  private MailIntegration mailIntegration;

  @Test
  void sendEmail() {
    mailIntegration.send("rafaelsnt123@gmail.com", "Olá, gmail", "Ola");
  }
}
