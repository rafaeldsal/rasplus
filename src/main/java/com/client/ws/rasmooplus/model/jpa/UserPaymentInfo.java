package com.client.ws.rasmooplus.model.jpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Table(name = "user_payment_info")
@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class UserPaymentInfo implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_payment_info_id", nullable = false, unique = true)
  private Long id;

  @Column(name = "card_number", nullable = false, unique = true)
  private String cardNumber;

  @Column(name = "card_expiration_month", nullable = false)
  private Integer cardExpirationMonth;

  @Column(name = "card_expiration_year", nullable = false)
  private Integer cardExpirationYear;

  @Column(name = "card_security_code", nullable = false)
  private String cardSecurityCode;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(nullable = false)
  private Integer installments;

  @Column(name = "dt_payment", nullable = false)
  private LocalDate dtPayment = LocalDate.now();

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

}
