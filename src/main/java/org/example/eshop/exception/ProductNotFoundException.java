package org.example.eshop.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ProductNotFoundException extends RuntimeException {

    public ProductNotFoundException(long productId) {
        super(String.format("Product with id <%d> not found", productId));
    }
}
