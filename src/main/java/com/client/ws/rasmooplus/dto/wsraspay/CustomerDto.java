package com.client.ws.rasmooplus.dto.wsraspay;

import lombok.Builder;

@Builder
public record CustomerDto(
    String id,
    String cpf,
    String email,
    String firstName,
    String lastName
) {
}
