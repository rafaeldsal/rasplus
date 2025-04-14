package com.client.ws.rasmooplus.dto;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;

import java.math.BigDecimal;
import java.time.LocalDate;

@Builder
public record UserPaymentInfoDto(

  Long id,

  @Size(min = 16, max = 16, message = "deve conter 16 caracteres")
  String cardNumber,

  @Min(value = 1)
  @Max(value = 12)
  Integer cardExpirationMonth,

  Integer cardExpirationYear,

  @Size(min = 3, max = 3, message = "deve conter 3 caracteres")
  String cardSecurityCode,

  @DecimalMin(value = "0.0", inclusive = false, message = "deve ser maior que zero")
  BigDecimal price,

  Integer installments,

  LocalDate dtPayment,

  @NotNull(message = "deve ser informado")
  Long userId
) {
}
