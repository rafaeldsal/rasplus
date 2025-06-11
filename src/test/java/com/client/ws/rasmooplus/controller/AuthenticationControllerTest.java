package com.client.ws.rasmooplus.controller;

import com.client.ws.rasmooplus.dto.LoginDto;
import com.client.ws.rasmooplus.dto.TokenDto;
import com.client.ws.rasmooplus.dto.UserDetailsDto;
import com.client.ws.rasmooplus.model.redis.UserRecoveryCode;
import com.client.ws.rasmooplus.service.AuthenticationService;
import com.client.ws.rasmooplus.service.CustomUserService;
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

import java.time.LocalDateTime;
import java.util.UUID;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureDataJpa
@AutoConfigureTestDatabase
@WebMvcTest(AuthenticationController.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles(profiles = "test")
class AuthenticationControllerTest {

    private static final String TOKEN = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiIxMjM0NTY3ODkwIiwibmFtZSI6IkpvaG4gRG9lIiwiaWF0IjoxNTE2MjM5MDIyfQ.SflKxwRJSMeKKF2QT4fwpMeJf36POk6yJV_adQssw5c";

    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthenticationService authenticationService;

    @MockitoBean
    private CustomUserService customUserService;

    @Test
    void given_auth_when_loginDtoIsValid_then_returnTokenDto() throws Exception {
        LoginDto dto = LoginDto.builder()
                .username("rafael@email.com")
                .password("senha123")
                .build();

        TokenDto tokenDto = TokenDto.builder()
                .token(TOKEN)
                .type("Bearer")
                .build();

        when(authenticationService.auth(dto)).thenReturn(tokenDto);

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(jsonPath("$.type", Matchers.is("Bearer")))
                .andExpect(jsonPath("$.token", Matchers.is(TOKEN)))
                .andExpect(status().isOk());

        verify(authenticationService, times(1)).auth(dto);
    }

    @Test
    void given_auth_when_dtoIsMissingValues_then_returnBadRequest() throws Exception {
        LoginDto dto = LoginDto.builder().build();

        mockMvc.perform(post("/auth")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("[password=atributo obrigatório, username=atributo obrigatório]")))
                .andExpect(jsonPath("$.status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("$.statusCode", Matchers.is(400)));
    }

    @Test
    void given_sendRecoveryCode_when_dtoUserRecoveryCodeIsValid_then_returnNoContent() throws Exception {
        UserRecoveryCode dto = UserRecoveryCode.builder()
                .id(UUID.randomUUID().toString())
                .code("1234")
                .creationDate(LocalDateTime.now())
                .email("rafael@email.com")
                .build();

        mockMvc.perform(post("/auth/recovery-code/send")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isNoContent());

        verify(customUserService, times(1)).sendRecoveryCode(any());
    }

    @Test
    void given_sendRecoveryCode_when_emailIsNotSentOrWrong_then_returnBadRequest() throws Exception {
        UserRecoveryCode dto = UserRecoveryCode.builder()
                .id(UUID.randomUUID().toString())
                .code("1234")
                .creationDate(LocalDateTime.now())
                .email("usuario")
                .build();

        mockMvc.perform(post("/auth/recovery-code/send")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("[email=must be a well-formed email address]")))
                .andExpect(jsonPath("$.status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("$.statusCode", Matchers.is(400)));
    }

    @Test
    void given_updatedPasswordByRecoveryCode_when_userDetailsDtoIsValid_then_returnIsOkStatus() throws Exception {
        UserDetailsDto dto = UserDetailsDto.builder()
                .recoveryCode("1234")
                .email("rafael@email.com")
                .password("senha123")
                .build();

        mockMvc.perform(patch("/auth/recovery-code/password")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        verify(customUserService, times(1)).updatedPasswordByRecoveryCode(dto);
    }

    @Test
    void given_updatedPasswordByRecoveryCode_when_emailIsWrongAndPassIsNull_then_returnBadRequest() throws Exception {
        UserDetailsDto dto = UserDetailsDto.builder()
                .recoveryCode("1234")
                .email("usuario")
                .password(" ")
                .build();

        mockMvc.perform(patch("/auth/recovery-code/password")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message", Matchers.is("[password=campo inválido, email=campo inválido]")))
                .andExpect(jsonPath("$.status", Matchers.is("BAD_REQUEST")))
                .andExpect(jsonPath("$.statusCode", Matchers.is(400)));

        verify(customUserService, times(0)).updatedPasswordByRecoveryCode(dto);
    }

    @Test
    void given_recoveryCodeIsValid_then_returnOk() throws Exception {
        when(customUserService.recoveryCodeIsValid("1234", "usuario@email.com")).thenReturn(true);

        mockMvc.perform(get("/auth/recovery-code")
                        .param("recoveryCode", "1234")
                        .param("email", "usuario@email.com"))
                .andExpect(status().isOk());

        verify(customUserService, times(1))
                .recoveryCodeIsValid("1234", "usuario@email.com");
    }
}