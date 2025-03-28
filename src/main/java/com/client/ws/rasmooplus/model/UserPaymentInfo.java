package com.client.ws.rasmooplus.model;

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
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "user_payment_info_id")
  private Long id;

  @Column(name = "card_number")
  private String cardNumber;

  @Column(name = "card_expiration_month")
  private Integer cardExpirationMonth;

  @Column(name = "card_expiration_year")
  private Integer cardExpirationYear;

  @Column(name = "card_security_code")
  private String cardSecurityCode;

  private BigDecimal price;

  private Integer instalments;

  @Column(name = "dt_payment")
  private LocalDate dtPayment = LocalDate.now();

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id")
  private User user;

}
