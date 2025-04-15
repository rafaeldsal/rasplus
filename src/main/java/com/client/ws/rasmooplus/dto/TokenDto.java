package com.client.ws.rasmooplus.dto;

import lombok.Builder;

@Builder
public record TokenDto(
    String token,
    String type
) {
}
