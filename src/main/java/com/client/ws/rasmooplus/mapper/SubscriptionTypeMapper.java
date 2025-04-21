package com.client.ws.rasmooplus.mapper;

import com.client.ws.rasmooplus.dto.SubscriptionTypeDto;
import com.client.ws.rasmooplus.model.jpa.SubscriptionType;

public class SubscriptionTypeMapper {

  public static SubscriptionType fromDtoToEntity(SubscriptionTypeDto subscriptionTypeDto) {
    return SubscriptionType.builder()
        .id(subscriptionTypeDto.getId())
        .name(subscriptionTypeDto.getName())
        .accessMonths(subscriptionTypeDto.getAccessMonths())
        .price(subscriptionTypeDto.getPrice())
        .productKey(subscriptionTypeDto.getProductKey())
        .build();
  }
}
