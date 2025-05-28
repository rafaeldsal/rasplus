package com.client.ws.rasmooplus.integration;

import com.client.ws.rasmooplus.dto.wsraspay.CustomerDto;
import com.client.ws.rasmooplus.exception.HttpClientException;
import com.client.ws.rasmooplus.integration.impl.WsRaspayIntegrationImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class WsRaspayIntegrationTest {

  @Mock
  private RestTemplate restTemplate;
  private static HttpHeaders headers;

  @InjectMocks
  private WsRaspayIntegrationImpl wsRaspayIntegration;

  @BeforeAll
  public static void loadHeaders() {
    headers = getHttpHeaders();
  }

  @Test
  void given_createCustomer_when_apiResponseIs201Created_then_returnCustomerDto() {
    CustomerDto dto = CustomerDto.builder()
        .cpf("11111111111")
        .build();
    ReflectionTestUtils.setField(wsRaspayIntegration, "raspayHost", "http://localhost:8080");
    ReflectionTestUtils.setField(wsRaspayIntegration, "customerUrl", "/customer");
    HttpEntity<CustomerDto> request = new HttpEntity<>(dto, this.headers);
    when(restTemplate.exchange("http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class))
        .thenReturn(ResponseEntity.of(Optional.of(dto)));

    assertEquals(dto, wsRaspayIntegration.createCustomer(dto));

    verify(restTemplate, times(1)).exchange("http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class);
  }

  @Test
  void given_createCustomer_when_apiResponseIs400BadRequest_then_returnNull() {
    CustomerDto dto = CustomerDto.builder()
        .cpf("11111111111")
        .build();
    ReflectionTestUtils.setField(wsRaspayIntegration, "raspayHost", "http://localhost:8080");
    ReflectionTestUtils.setField(wsRaspayIntegration, "customerUrl", "/customer");
    HttpEntity<CustomerDto> request = new HttpEntity<>(dto, this.headers);
    when(restTemplate.exchange("http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class))
        .thenReturn(ResponseEntity.badRequest().build());

    assertNull(wsRaspayIntegration.createCustomer(dto));

    verify(restTemplate, times(1)).exchange("http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class);
  }

  @Test
  void given_createCustomer_when_apiResponseGetThrows_then_throwsHttpClientException() {
    CustomerDto dto = CustomerDto.builder()
        .cpf("11111111111")
        .build();
    ReflectionTestUtils.setField(wsRaspayIntegration, "raspayHost", "http://localhost:8080");
    ReflectionTestUtils.setField(wsRaspayIntegration, "customerUrl", "/customer");
    HttpEntity<CustomerDto> request = new HttpEntity<>(dto, this.headers);
    when(restTemplate.exchange("http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class))
        .thenThrow(HttpClientException.class);

    assertThrows(HttpClientException.class, () -> wsRaspayIntegration.createCustomer(dto));

    verify(restTemplate, times(1)).exchange("http://localhost:8080/customer", HttpMethod.POST, request, CustomerDto.class);
  }

  private static HttpHeaders getHttpHeaders() {
    org.springframework.http.HttpHeaders headers = new org.springframework.http.HttpHeaders();
    String credentials = "rasmooplus:r@sm00";
    String base64 = Base64.getEncoder().encodeToString(credentials.getBytes());
    headers.add("Authorization", "Basic " + base64);
    return headers;
  }

}