package com.h4h.employeeportal.shared.exception;

import java.time.Instant;

public record ErrorResponse(Instant timestamp, int status, String error) {}
