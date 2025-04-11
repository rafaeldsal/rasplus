package com.client.ws.rasmooplus.dto.wsraspay;

import lombok.Builder;

@Builder
public record CreditCardDto(
    Long cvv,
    String documentNumber,
    String number,
    Long installments,
    Long month,
    Long year

) {
}
