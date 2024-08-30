package org.example.eshop;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class DuplicateProductNameException extends RuntimeException {

    public DuplicateProductNameException(String name) {
        super(String.format("A product with name <%s> already exists", name));
    }
}
