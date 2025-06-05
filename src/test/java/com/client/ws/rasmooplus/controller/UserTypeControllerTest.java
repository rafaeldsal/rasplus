package com.client.ws.rasmooplus.controller;

import com.client.ws.rasmooplus.model.jpa.UserType;
import com.client.ws.rasmooplus.service.UserTypeService;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.Mockito.when;

@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(UserTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
class UserTypeControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private UserTypeService userTypeService;

  @Test
  void given_findAll_then_returnAllUserType() throws Exception {
    List<UserType> userTypeList = new ArrayList<>();

    UserType userType1 = new UserType(1L, "Professor", "Professor da plataforma");
    UserType userType2 = new UserType(2L, "Administrador", "Funcionário da plataforma");
    userTypeList.add(userType1);
    userTypeList.add(userType2);

    when(userTypeService.findAll()).thenReturn(userTypeList);

    mockMvc.perform(MockMvcRequestBuilders.get("/user-type"))
        .andExpect(MockMvcResultMatchers.status().isOk());
  }

}