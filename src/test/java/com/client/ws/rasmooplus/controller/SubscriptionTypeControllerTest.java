package com.client.ws.rasmooplus.controller;

import com.client.ws.rasmooplus.dto.SubscriptionTypeDto;
import com.client.ws.rasmooplus.model.jpa.SubscriptionType;
import com.client.ws.rasmooplus.service.SubscriptionTypeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.AutoConfigureDataJpa;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;

import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(SubscriptionTypeController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
class SubscriptionTypeControllerTest {

  @Autowired
  private ObjectMapper objectMapper;

  @Autowired
  private MockMvc mockMvc;

  @MockitoBean
  private SubscriptionTypeService subscriptionTypeService;

  @Test
  void given_findAll_then_returnAllSubscriptionType() throws Exception {
    mockMvc.perform(get("/subscription-type"))
        .andExpect(status().isOk());
  }

  @Test
  void given_findById_when_getId2_then_returnOneSubscriptionType() throws Exception {

    SubscriptionType subscriptionType = SubscriptionType.builder()
        .id(2L)
        .name("VITALICIO")
        .accessMonths(null)
        .price(BigDecimal.valueOf(997))
        .productKey("FOREVER2025")
        .build();

    when(subscriptionTypeService.findById(2L)).thenReturn(subscriptionType);

    mockMvc.perform(get("/subscription-type/2"))
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.id", Matchers.is(2)))
        .andExpect(jsonPath("$.name", Matchers.is("VITALICIO")));
  }

  @Test
  void given_delete_when_getId_then_noReturnAndNoContent() throws Exception {
    mockMvc.perform(delete("/subscription-type/{id}", 2L))
        .andExpect(status().isNoContent());
    verify(subscriptionTypeService, times(1)).delete(2L);
  }

  @Test
  void given_create_when_dtoIsValid_then_returnSubscriptionTypeCreated() throws Exception {
    SubscriptionType subscriptionType = SubscriptionType.builder()
        .id(2L)
        .name("VITALICIO")
        .accessMonths(null)
        .price(BigDecimal.valueOf(997))
        .productKey("FOREVER2025")
        .build();

    SubscriptionTypeDto dto = SubscriptionTypeDto.builder()
        .id(null)
        .accessMonths(null)
        .name("VITALICIO")
        .price(BigDecimal.valueOf(997))
        .productKey("FOREVER2025")
        .build();

    when(subscriptionTypeService.create(dto)).thenReturn(subscriptionType);

    mockMvc.perform(post("/subscription-type")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isCreated())
        .andExpect(jsonPath("$.id", Matchers.notNullValue()));

    verify(subscriptionTypeService, times(1)).create(dto);
  }

  @Test
  void given_create_when_dtoIsMissingValues_then_returnBadRequest() throws Exception {
    SubscriptionTypeDto dto = SubscriptionTypeDto.builder()
        .id(null)
        .accessMonths(13L)
        .name("")
        .price(null)
        .productKey("22")
        .build();

    mockMvc.perform(post("/subscription-type")
            .contentType(MediaType.APPLICATION_JSON_VALUE)
            .content(objectMapper.writeValueAsString(dto)))
        .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("$.message", Matchers.is("[price=não pode ser nulo, accessMonths=não pode ser maior que 12, name=campo 'name' deve ter tamanho entre 5 e 30, productKey=deve ter tamanho entre 5 e 15]")))
        .andExpect(jsonPath("$.status", Matchers.is("BAD_REQUEST")))
        .andExpect(jsonPath("$.statusCode", Matchers.is(400)));

    verify(subscriptionTypeService, times(0)).create(dto);
  }

  @Test
  void given_update_when_dtoIsValid_then_returnSubscriptionTypeUpdated() throws Exception {
    SubscriptionType subscriptionType = SubscriptionType.builder()
            .id(2L)
            .name("VITALICIO")
            .accessMonths(null)
            .price(BigDecimal.valueOf(997))
            .productKey("FOREVER2025")
            .build();

    SubscriptionTypeDto dto = SubscriptionTypeDto.builder()
            .id(2L)
            .accessMonths(null)
            .name("VITALICIO")
            .price(BigDecimal.valueOf(997))
            .productKey("FOREVER2025")
            .build();

    when(subscriptionTypeService.update(2L, dto)).thenReturn(subscriptionType);

    mockMvc.perform(put("/subscription-type/2")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(dto)))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id", Matchers.notNullValue()));

    verify(subscriptionTypeService, times(1)).update(2L, dto);
  }

  @Test
  void given_update_when_dtoIsMissingValues_then_returnBadRequest() throws Exception {
    SubscriptionTypeDto dto = SubscriptionTypeDto.builder()
            .id(null)
            .accessMonths(13L)
            .name("")
            .price(null)
            .productKey("22")
            .build();

    mockMvc.perform(put("/subscription-type/2")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(dto)))
            .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.message", Matchers.is("[price=não pode ser nulo, accessMonths=não pode ser maior que 12, name=campo 'name' deve ter tamanho entre 5 e 30, productKey=deve ter tamanho entre 5 e 15]")))
            .andExpect(jsonPath("$.status", Matchers.is("BAD_REQUEST")))
            .andExpect(jsonPath("$.statusCode", Matchers.is(400)));

    verify(subscriptionTypeService, times(0)).update(2L, dto);
  }

  @Test
  void given_update_when_idIsNull_then_returnBadRequest() throws Exception {
    SubscriptionTypeDto dto = SubscriptionTypeDto.builder()
            .id(null)
            .accessMonths(13L)
            .name("")
            .price(null)
            .productKey("22")
            .build();

    mockMvc.perform(put("/subscription-type/null")
                    .contentType(MediaType.APPLICATION_JSON_VALUE)
                    .content(objectMapper.writeValueAsString(dto)))
            .andExpect(status().isBadRequest());

    verify(subscriptionTypeService, times(0)).update(null, dto);
  }

}