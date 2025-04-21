package com.client.ws.rasmooplus.mapper;

import com.client.ws.rasmooplus.dto.UserPaymentInfoDto;
import com.client.ws.rasmooplus.model.jpa.User;
import com.client.ws.rasmooplus.model.jpa.UserPaymentInfo;

import java.time.LocalDate;

public class UserPaymentInfoMapper {

  public static UserPaymentInfo fromDtoToEntity(UserPaymentInfoDto dto, User user) {
    return UserPaymentInfo.builder()
        .id(dto.id())
        .cardNumber(dto.cardNumber())
        .cardExpirationMonth(dto.cardExpirationMonth())
        .cardExpirationYear(dto.cardExpirationYear())
        .cardSecurityCode(dto.cardSecurityCode())
        .price(dto.price())
        .dtPayment(LocalDate.now())
        .installments(dto.installments())
        .user(user)
        .build();
  }
}
