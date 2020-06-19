package com.stanislavgrujic.documentimporter.web;

import com.stanislavgrujic.documentimporter.web.security.AccessForbiddenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StreamUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.io.IOException;
import java.nio.charset.Charset;

@ControllerAdvice
@Slf4j
public class ApplicationExceptionHandler {

  @ExceptionHandler(NoHandlerFoundException.class)
  public ResponseEntity<String> renderDefaultPage() {
    try {
      Resource indexFile = new ClassPathResource("public/index.html");
      String body = StreamUtils.copyToString(indexFile.getInputStream(), Charset.defaultCharset());
      return ResponseEntity.ok().contentType(MediaType.TEXT_HTML).body(body);
    } catch (IOException e) {
      log.error("Could not read public/index.html", e);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("There was an error completing the action.");
    }
  }

  @ExceptionHandler(AccessForbiddenException.class)
  public ResponseEntity<String> handle(AccessForbiddenException e) {
    return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
  }

  @ExceptionHandler(Exception.class)
  public ResponseEntity<String> renderErrorPage() {
    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).contentType(MediaType.TEXT_HTML).body("Sorry");
  }
}
