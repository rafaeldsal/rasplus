package com.client.ws.rasmooplus.service;

import com.client.ws.rasmooplus.model.jpa.UserType;
import com.client.ws.rasmooplus.repository.jpa.UserTypeRepository;
import com.client.ws.rasmooplus.service.impl.UserTypeServiceImpl;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class UserTypeServiceTest {

  @Mock
  private UserTypeRepository userTypeRepository;

  @InjectMocks
  private UserTypeServiceImpl userTypeService;

  @Test
  void given_findAll_when_thereAreDatasInDataBase_then_returnAllDatas() {
    List<UserType> userTypeList = new ArrayList<>();
    UserType userType1 = new UserType(1l, "Professor", "Professor da Plataforma");
    UserType userType2 = new UserType(2l, "Administrador", "Funcionário");
    userTypeList.add(userType1);
    userTypeList.add(userType2);


    Mockito.when(userTypeRepository.findAll()).thenReturn(userTypeList);
    var result = userTypeService.findAll();
    Assertions.assertThat(result).isNotEmpty().hasSize(2);
  }
}
