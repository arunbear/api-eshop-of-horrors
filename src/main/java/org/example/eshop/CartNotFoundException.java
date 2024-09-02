package org.example.eshop;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class CartNotFoundException extends RuntimeException {

    public CartNotFoundException(long cartId) {
        super(String.format("Cart with id <%d> not found", cartId));
    }
}
