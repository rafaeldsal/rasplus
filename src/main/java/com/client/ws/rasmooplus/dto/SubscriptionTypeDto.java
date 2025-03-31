package com.client.ws.rasmooplus.dto;

import java.math.BigDecimal;

public record SubscriptionTypeDto(
    Long id,
    String name,
    Integer accessMonths,
    BigDecimal price,
    String productKey
) {
}
