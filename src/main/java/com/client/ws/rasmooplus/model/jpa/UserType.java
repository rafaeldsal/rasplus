package com.client.ws.rasmooplus.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Entity
@Table(name = "user_type")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserType implements GrantedAuthority {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_type_id",nullable = false, unique = true)
  private Long id;

  @Column(nullable = false)
  private String name;

  @Column(nullable = false)
  private String description;

  @Override
  public String getAuthority() {
    return name;
  }
}
