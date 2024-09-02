package org.example.eshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidProductNameException extends RuntimeException {

    public InvalidProductNameException(String message) {
        super(message);
    }
}
