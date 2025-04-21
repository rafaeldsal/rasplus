package com.client.ws.rasmooplus.model.jpa;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.hateoas.RepresentationModel;

import java.io.Serializable;
import java.math.BigDecimal;

@Entity
@Table(name = "subscriptions_type")
@Data
@EqualsAndHashCode(callSuper = false)
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubscriptionType extends RepresentationModel<SubscriptionType> implements Serializable {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "subscriptions_type_id", unique = true, nullable = false)
  private Long id;

  private String name;

  @Column(name = "access_months")
  private Long accessMonths;

  @Column(nullable = false)
  private BigDecimal price;

  @Column(name = "product_key", unique = true)
  private String productKey;
}
