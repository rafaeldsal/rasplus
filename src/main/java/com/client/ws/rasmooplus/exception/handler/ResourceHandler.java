package com.client.ws.rasmooplus.exception.handler;

import com.client.ws.rasmooplus.dto.error.ErrorResponseDto;
import com.client.ws.rasmooplus.exception.BadRequestException;
import com.client.ws.rasmooplus.exception.BusinessException;
import com.client.ws.rasmooplus.exception.HttpClientException;
import com.client.ws.rasmooplus.exception.IntegrationException;
import com.client.ws.rasmooplus.exception.NotFoundException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class ResourceHandler {

  @ExceptionHandler(MethodArgumentNotValidException.class)
  public ResponseEntity<ErrorResponseDto> methodArgumentNotValidException(MethodArgumentNotValidException m) {

    Map<String, String> messages = new HashMap<>();

    m.getBindingResult().getAllErrors().forEach(error -> {
      String field = ((FieldError) error).getField();
      String message = error.getDefaultMessage();

      messages.put(field, message);
    });

    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto.builder()
        .message(Arrays.toString(messages.entrySet().toArray()))
        .status(HttpStatus.BAD_REQUEST)
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .build());
  }

  @ExceptionHandler(NotFoundException.class)
  public ResponseEntity<ErrorResponseDto> notFoundException(NotFoundException n) {
    return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ErrorResponseDto.builder()
        .message(n.getMessage())
        .status(HttpStatus.NOT_FOUND)
        .statusCode(HttpStatus.NOT_FOUND.value())
        .build());
  }

  @ExceptionHandler(BadRequestException.class)
  public ResponseEntity<ErrorResponseDto> badRequestException(BadRequestException b) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto.builder()
        .message(b.getMessage())
        .status(HttpStatus.BAD_REQUEST)
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .build());
  }

  @ExceptionHandler(DataIntegrityViolationException.class)
  public ResponseEntity<ErrorResponseDto> dataIntegrityViolationException(DataIntegrityViolationException d) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto.builder()
        .message(d.getMessage())
        .status(HttpStatus.BAD_REQUEST)
        .statusCode(HttpStatus.BAD_REQUEST.value())
        .build());
  }

  @ExceptionHandler(BusinessException.class)
  public ResponseEntity<ErrorResponseDto> handlerBusinessException(BusinessException d) {
    return ResponseEntity.status(HttpStatus.CONFLICT).body(ErrorResponseDto.builder()
        .message(d.getMessage())
        .status(HttpStatus.CONFLICT)
        .statusCode(HttpStatus.CONFLICT.value())
        .build());
  }

  @ExceptionHandler(IntegrationException.class)
  public ResponseEntity<ErrorResponseDto> handlerIntegrationException(IntegrationException d) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto.builder()
        .message(d.getMessage())
        .status(d.getStatus())
        .statusCode(d.getStatus().value())
        .build());
  }

  @ExceptionHandler(HttpClientException.class)
  public ResponseEntity<ErrorResponseDto> handlerHttpClientException(HttpClientException d) {
    return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ErrorResponseDto.builder()
        .message(d.getMessage())
        .status(d.getStatus())
        .statusCode(d.getStatus().value())
        .build());
  }
}
