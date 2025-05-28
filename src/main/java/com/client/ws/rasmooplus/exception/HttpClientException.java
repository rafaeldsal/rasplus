package com.client.ws.rasmooplus.exception;

import org.springframework.http.HttpStatus;

public class HttpClientException extends RuntimeException {

  private final HttpStatus status;

  public HttpClientException(String message, HttpStatus status) {
    super(message);
    this.status = status;
  }

  public HttpStatus getStatus() {
    return status;
  }
}
