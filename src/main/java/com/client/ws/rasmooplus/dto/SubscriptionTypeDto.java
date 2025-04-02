package com.client.ws.rasmooplus.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SubscriptionTypeDto {

  private Long id;

  private String name;

  private Long accessMonths;

  private  BigDecimal price;

  private String productKey;
}
