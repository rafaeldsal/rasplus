package com.client.ws.rasmooplus.mapper.wsraspay;

import com.client.ws.rasmooplus.dto.wsraspay.CustomerDto;
import com.client.ws.rasmooplus.model.jpa.User;

public class CustomerMapper {

  public static CustomerDto build(User user) {
    String[] fullName = user.getName().split(" ");
    String firstName = fullName[0];
    String lastName = fullName.length > 1 ? fullName[fullName.length - 1] : "";
    return CustomerDto.builder()
        .cpf(user.getCpf())
        .email(user.getEmail())
        .firstName(firstName)
        .lastName(lastName)
        .build();
  }
}
