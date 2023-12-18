package com.rpissarra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class RequestValidationException extends RuntimeException {

    public RequestValidationException() {
    }

    ;

    public RequestValidationException(String message) {
        super(message);
    }
}
