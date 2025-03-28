package com.client.ws.rasmooplus.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "subscription_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionType implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  @Column(name = "subscription_type_id")
  private Long id;

  private String name;

  @Column(name = "access_months")
  private Integer accessMonths;

  private BigDecimal price;

  @Column(name = "product_key")
  private String productKey;
}
