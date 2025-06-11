package com.client.ws.rasmooplus.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record UserDetailsDto(
    @Email(message = "campo inválido")
    String email,

    String recoveryCode,

    @NotBlank(message = "campo inválido")
    String password
) {
}
