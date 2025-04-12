package com.client.ws.rasmooplus.dto.wsraspay;

import lombok.Builder;

import java.math.BigDecimal;

@Builder
public record OrderDto(
    String id,
    String customerId,
    String productAcronym,
    BigDecimal discount
) {
}
