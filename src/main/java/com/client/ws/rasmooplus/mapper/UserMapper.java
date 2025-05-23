package com.client.ws.rasmooplus.mapper;

import com.client.ws.rasmooplus.dto.UserDto;
import com.client.ws.rasmooplus.model.jpa.SubscriptionType;
import com.client.ws.rasmooplus.model.jpa.User;
import com.client.ws.rasmooplus.model.jpa.UserType;

public class UserMapper {

  public static User fromDtoToEntity(UserDto dto, UserType userType, SubscriptionType subscriptionType) {
    return User.builder()
        .id(dto.getId())
        .name(dto.getName())
        .email(dto.getEmail())
        .cpf(dto.getCpf())
        .phone(dto.getPhone())
        .dtSubscription(dto.getDtSubscription())
        .dtExpiration(dto.getDtExpiration())
        .userType(userType)
        .subscriptionType(subscriptionType)
        .build();
  }
}
