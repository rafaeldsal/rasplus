package com.client.ws.rasmooplus.integration;

import com.client.ws.rasmooplus.dto.wsraspay.CreditCardDto;
import com.client.ws.rasmooplus.dto.wsraspay.CustomerDto;
import com.client.ws.rasmooplus.dto.wsraspay.OrderDto;
import com.client.ws.rasmooplus.dto.wsraspay.PaymentDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;

@SpringBootTest
class WsRaspayIntegrationImplTest {

  @Autowired
  private WsRaspayIntegration wsRaspayIntegration;

  @Test
  void createCustomerWhenDtoOk() {
    CustomerDto dto = new CustomerDto(null, "63782411048", "teste@teste.com", "Rafael", "Souza");
    wsRaspayIntegration.createCustomer(dto);
  }

  @Test
  void createOrderWhenDtoOk() {
    OrderDto dto = new OrderDto(null, "67f9a85e5fdb7a46d001c97a","MONTH22", BigDecimal.ZERO);
    wsRaspayIntegration.createOrder(dto);
  }

  @Test
  void processPaymentWhenDtoOk() {
    CreditCardDto creditCardDto = new CreditCardDto(123L, "63782411048", "1234123412341234", 0L, 06, 2025);
    PaymentDto paymentDto = new PaymentDto(creditCardDto, "67f9a85e5fdb7a46d001c97a", "67f9a8aa5fdb7a46d001c97b");
    wsRaspayIntegration.processPayment(paymentDto);
  }
}
