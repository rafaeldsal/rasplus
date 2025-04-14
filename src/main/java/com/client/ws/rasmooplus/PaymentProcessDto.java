package com.client.ws.rasmooplus;

import com.client.ws.rasmooplus.dto.UserPaymentInfoDto;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

import java.math.BigDecimal;

public record PaymentProcessDto(

    @NotBlank(message = "não pode ser nulo ou vazio. Deve ser informado")
    String productKey,

    BigDecimal discount,

    @NotNull(message = "dados do pagamento deve ser informado")
    @JsonProperty("userPaymentInfo")
    UserPaymentInfoDto userPaymentInfoDto
) {
}
