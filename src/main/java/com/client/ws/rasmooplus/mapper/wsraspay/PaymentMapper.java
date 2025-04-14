package com.client.ws.rasmooplus.mapper.wsraspay;

import com.client.ws.rasmooplus.dto.wsraspay.CreditCardDto;
import com.client.ws.rasmooplus.dto.wsraspay.PaymentDto;
import com.client.ws.rasmooplus.mapper.CreditCardMapper;
import com.client.ws.rasmooplus.model.UserPaymentInfo;

public class PaymentMapper {

  public static PaymentDto build(String customerId, String orderId, CreditCardDto dto) {
    return PaymentDto.builder()
        .customerId(customerId)
        .orderId(orderId)
        .creditCard(dto)
        .build();
  }
}
