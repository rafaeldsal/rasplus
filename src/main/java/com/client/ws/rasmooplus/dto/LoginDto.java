package com.client.ws.rasmooplus.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;

@Builder
public record LoginDto(

    @NotBlank(message = "atributo obrigatório")
    String username,

    @NotBlank(message = "atributo obrigatório")
    String password
) {
}
