package com.client.ws.rasmooplus.model.jpa;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Entity
@Table(name = "user_credentials")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserCredentials implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  @Column(name = "user_credentials_id", unique = true, nullable = false)
  private Long id;

  private String username;

  private String password;

  @ManyToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "user_type_id")
  private UserType userType;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    List<UserType> userTypes = new ArrayList<>();
    userTypes.add(userType);
    return userTypes;
  }

  @Override
  public String getPassword() {
    return password;
  }

  @Override
  public String getUsername() {
    return username;
  }
}
