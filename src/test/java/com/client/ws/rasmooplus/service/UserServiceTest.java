package com.client.ws.rasmooplus.service;

import com.client.ws.rasmooplus.dto.UserDto;
import com.client.ws.rasmooplus.exception.BadRequestException;
import com.client.ws.rasmooplus.exception.NotFoundException;
import com.client.ws.rasmooplus.model.jpa.User;
import com.client.ws.rasmooplus.model.jpa.UserType;
import com.client.ws.rasmooplus.repository.jpa.UserRepository;
import com.client.ws.rasmooplus.repository.jpa.UserTypeRepository;
import com.client.ws.rasmooplus.service.impl.UserServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

  @Mock
  private UserRepository userRepository;

  @Mock
  private UserTypeRepository userTypeRepository;

  @InjectMocks
  private UserServiceImpl userService;

  private UserDto userDto;

  @BeforeEach
  void loadUserDto() {
    userDto = new UserDto();
    userDto.setEmail("rafael@email.com");
    userDto.setCpf("57105649070");
    userDto.setPhone("61999999999");
    userDto.setName("Rafael Souza");
    userDto.setDtExpiration(LocalDate.now());
    userDto.setDtSubscription(LocalDate.now());
    userDto.setUserTypeId(1L);
  }

  @Test
  void given_create_when_idIsNullAndUserTypeIsFound_then_returnUserCreated() {

    UserType userType = new UserType(1L, "Aluno", "Aluno da Plataforma");

    when(userTypeRepository.findById(1L)).thenReturn(Optional.of(userType));

    when(userRepository.save(any(User.class)))
        .thenAnswer(invocation -> invocation.getArgument(0));

    // Act
    User user = userService.create(userDto);

    // Assert
    Assertions.assertEquals(userDto.getName(), user.getName());
    Assertions.assertEquals(userDto.getEmail(), user.getEmail());
    Assertions.assertEquals(userDto.getCpf(), user.getCpf());
    Assertions.assertEquals(userDto.getPhone(), user.getPhone());
    Assertions.assertEquals(userDto.getDtSubscription(), user.getDtSubscription());
    Assertions.assertEquals(userDto.getDtExpiration(), user.getDtExpiration());
    Assertions.assertEquals(userType, user.getUserType());

    verify(userTypeRepository, times(1)).findById(1L);
    verify(userRepository, times(1)).save(user);
  }

  @Test
  void given_create_when_idIsNotNull_then_throwBadRequestException() {

    userDto.setId(1L);
    // Assert
    Assertions.assertThrows(BadRequestException.class, () -> userService.create(userDto));

    verify(userTypeRepository, times(0)).findById(any());
    verify(userRepository, times(0)).save(any());
  }

  @Test
  void given_create_when_idIsNullAndUserTypeIsEmpty_then_throwNotFoundException() {

    when(userTypeRepository.findById(1L)).thenReturn(Optional.empty());

    // Assert
    Assertions.assertThrows(NotFoundException.class, () -> userService.create(userDto));

    verify(userTypeRepository, times(1)).findById(any());
    verify(userRepository, times(0)).save(any());
  }

}