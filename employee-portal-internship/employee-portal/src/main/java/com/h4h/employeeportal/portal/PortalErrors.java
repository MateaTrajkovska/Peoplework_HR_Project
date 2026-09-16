package com.h4h.employeeportal.portal;

import java.util.Map;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.*;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

@RestControllerAdvice
public class PortalErrors {

 @ExceptionHandler(MethodArgumentNotValidException.class)
 public ResponseEntity<?> validation(MethodArgumentNotValidException e) {
  return ResponseEntity.badRequest().body(
          Map.of(
                  "message",
                  e.getBindingResult().getFieldErrors().stream()
                          .map(f -> f.getField() + ": " + f.getDefaultMessage())
                          .findFirst()
                          .orElse("Check the form fields.")
          )
  );
 }

 @ExceptionHandler(DataIntegrityViolationException.class)
 public ResponseEntity<?> conflict(DataIntegrityViolationException e) {
  return ResponseEntity.status(409).body(
          Map.of(
                  "message",
                  "A record with these unique details already exists, or this record is still in use."
          )
  );
 }

 @ExceptionHandler(HttpMessageNotReadableException.class)
 public ResponseEntity<?> invalid(HttpMessageNotReadableException e) {
  return ResponseEntity.badRequest().body(
          Map.of("message", "Check the dates, numbers and required fields.")
  );
 }
}