package com.client.ws.rasmooplus.dto.wsraspay;

import lombok.Builder;

@Builder
public record CreditCardDto(
    Long cvv,
    String documentNumber,
    String number,
    Integer installments,
    Integer month,
    Integer year
) {
}
