package com.client.ws.rasmooplus.dto.wsraspay;

import lombok.Builder;

@Builder
public record PaymentDto(
    CreditCardDto creditCard,
    String customerId,
    String orderId
) {
}
