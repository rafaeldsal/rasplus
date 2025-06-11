package com.client.ws.rasmooplus.model.redis;

import jakarta.persistence.Id;
import jakarta.validation.constraints.Email;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.index.Indexed;

import java.io.Serializable;
import java.time.LocalDateTime;

@RedisHash("userRecoveryCode")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserRecoveryCode implements Serializable {

  @Id
  private String id;

  @Indexed
  @Email
  private String email;

  private String code;

  private LocalDateTime creationDate = LocalDateTime.now();
}
