package com.client.ws.rasmooplus.dto.wsraspay;

import lombok.Builder;

@Builder
public record OrderDto(
    String id,
    String customerId,
    String productAcronym,
    Long discount
) {
}
