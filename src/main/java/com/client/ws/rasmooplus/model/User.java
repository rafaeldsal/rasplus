package com.client.ws.rasmooplus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.LocalDate;

@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "users_id", nullable = false, unique = true)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false, unique = true)
  private String email;

  @Column(nullable = false, unique = true)
  private String phone;

  @Column(nullable = false, unique = true)
  private String cpf;

  @Column(name = "dt_subscription", nullable = false)
  private LocalDate dtSubscription = LocalDate.now();

  @Column(name = "dt_expiration", nullable = false)
  private LocalDate dtExpiration;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_type_id")
  private UserType userType;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "subscription_type_id")
  private SubscriptionType subscriptionType;

}
