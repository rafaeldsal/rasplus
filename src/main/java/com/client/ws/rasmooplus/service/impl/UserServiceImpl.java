package com.client.ws.rasmooplus.service.impl;

import com.client.ws.rasmooplus.dto.UserDto;
import com.client.ws.rasmooplus.exception.BadRequestException;
import com.client.ws.rasmooplus.exception.NotFoundException;
import com.client.ws.rasmooplus.mapper.UserMapper;
import com.client.ws.rasmooplus.model.jpa.User;
import com.client.ws.rasmooplus.model.jpa.UserType;
import com.client.ws.rasmooplus.repository.jpa.UserRepository;
import com.client.ws.rasmooplus.repository.jpa.UserTypeRepository;
import com.client.ws.rasmooplus.service.UserService;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
public class UserServiceImpl implements UserService {

  private final UserRepository userRepository;
  private final  UserTypeRepository userTypeRepository;

  UserServiceImpl(UserRepository userRepository, UserTypeRepository userTypeRepository) {
    this.userRepository = userRepository;
    this.userTypeRepository = userTypeRepository;
  }

  @Override
  public User create(UserDto dto) {

    if(Objects.nonNull(dto.getId())) {
      throw new BadRequestException("ID deve ser nulo");
    }

    var userTypeOpt = userTypeRepository.findById(dto.getUserTypeId());

    if (userTypeOpt.isEmpty()) {
      throw new NotFoundException("userTypeId não encontrado");
    }

    UserType userType = userTypeOpt.get();

    User user = UserMapper.fromDtoToEntity(dto, userType, null);

    return userRepository.save(user);
  }

}
