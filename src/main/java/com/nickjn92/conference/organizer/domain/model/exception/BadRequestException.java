package com.nickjn92.conference.organizer.domain.model.exception;

public class BadRequestException extends Exception {
  public BadRequestException(String message) {
    super(message);
  }
}
