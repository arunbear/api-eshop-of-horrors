package org.example.eshop;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidProductLabelException extends RuntimeException {

    public InvalidProductLabelException(String name) {
        super(String.format("Invalid product label <%s>", name));
    }
}
