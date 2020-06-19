package com.stanislavgrujic.documentimporter.web.security;

public class AccessForbiddenException extends RuntimeException {

  public AccessForbiddenException() {
    super("Could not validate JWT token.");
  }
}
