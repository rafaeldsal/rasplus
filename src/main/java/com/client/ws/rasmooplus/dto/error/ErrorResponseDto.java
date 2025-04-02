package com.client.ws.rasmooplus.dto.error;

import lombok.Builder;
import org.springframework.http.HttpStatus;

@Builder
public record ErrorResponseDto(
    String message,
    HttpStatus status,
    Integer statusCode
) {
}
