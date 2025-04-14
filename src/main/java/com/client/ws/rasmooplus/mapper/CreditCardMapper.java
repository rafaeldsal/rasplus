package com.client.ws.rasmooplus.mapper;

import com.client.ws.rasmooplus.dto.UserPaymentInfoDto;
import com.client.ws.rasmooplus.dto.wsraspay.CreditCardDto;

public class CreditCardMapper {

  public static CreditCardDto build(UserPaymentInfoDto dto, String documentNumber) {
    return CreditCardDto.builder()
        .documentNumber(documentNumber)
        .cvv(Long.parseLong(dto.cardSecurityCode()))
        .number(dto.cardNumber())
        .month(dto.cardExpirationMonth())
        .year(dto.cardExpirationYear())
        .installments(dto.installments())
        .build();
  }
}
