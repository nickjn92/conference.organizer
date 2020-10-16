package com.nickjn92.conference.organizer.service;

import com.nickjn92.conference.organizer.domain.model.exception.BadRequestException;
import com.nickjn92.conference.organizer.domain.model.exception.NotFoundException;
import com.nickjn92.conference.organizer.domain.model.exception.TemporaryException;
import lombok.Builder;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import reactor.core.publisher.Mono;

@Slf4j
@RestControllerAdvice
public class ConferenceControllerAdvice {

  @ExceptionHandler(DataIntegrityViolationException.class)
  @ResponseBody
  public Mono<ResponseEntity<ErrorResponse>> handleDatabaseErrors(DataAccessException err) {
    if (err instanceof DataIntegrityViolationException) {
      // One of the values does not exist in database, should be handled better but default to 404
      return Mono.just(getResponse(HttpStatus.NOT_FOUND, "Invalid values in request"));
    }
    // Else we have some unknown exception, default to INTERNAL_SERVER_ERROR
    log.warn("Temporary exception when handling incoming request", err);
    return Mono.just(getResponse(HttpStatus.INTERNAL_SERVER_ERROR, err.getMessage()));
  }

  @ExceptionHandler(TemporaryException.class)
  @ResponseBody
  public Mono<ResponseEntity<ErrorResponse>> handleTemporaryErrors(TemporaryException err) {
    log.warn("Temporary exception when handling incoming request", err);
    return Mono.just(getResponse(HttpStatus.SERVICE_UNAVAILABLE, err.getMessage()));
  }

  @ExceptionHandler(BadRequestException.class)
  @ResponseBody
  public Mono<ResponseEntity<ErrorResponse>> handleBadRequestErrors(BadRequestException err) {
    return Mono.just(getResponse(HttpStatus.BAD_REQUEST, err.getMessage()));
  }

  @ExceptionHandler(NotFoundException.class)
  @ResponseBody
  public Mono<ResponseEntity<ErrorResponse>> handleNotFoundErrors(NotFoundException err) {
    return Mono.just(getResponse(HttpStatus.NOT_FOUND, err.getMessage()));
  }

  private ResponseEntity<ErrorResponse> getResponse(HttpStatus status, String message) {
    return ResponseEntity.status(status).body(ErrorResponse.builder().message(message).build());
  }

  @Builder
  @Data
  public static class ErrorResponse {
    String message;
  }
}
