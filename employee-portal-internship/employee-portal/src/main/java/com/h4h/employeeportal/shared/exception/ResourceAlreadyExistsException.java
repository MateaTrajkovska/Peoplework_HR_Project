package com.h4h.employeeportal.shared.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ResourceAlreadyExistsException
        extends RuntimeException {

    public ResourceAlreadyExistsException(String errorCode) {
        super(errorCode);
    }
}