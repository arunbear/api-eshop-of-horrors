package org.example.eshop.service;

import org.example.eshop.CartNotFoundException;
import org.example.eshop.dto.CartDto;
import org.example.eshop.entity.Cart;
import org.example.eshop.repository.CartRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {
    private final CartRepository cartRepository;

    @Autowired
    public CartService(CartRepository cartRepository) {
        this.cartRepository = cartRepository;
    }

    public CartDto createCart() {
        Cart cart = cartRepository.save(new Cart());

        return CartDto.builder()
            .cartId(cart.getId())
            .products(List.of())
            .build();
    }

    public CartDto checkOutCart(long cartId) {
        Cart foundCart = cartRepository.findById(cartId)
            .orElseThrow(() -> new CartNotFoundException(cartId));

        foundCart.setCheckedOut(true);

        Cart savedCart = cartRepository.save(foundCart);

        return CartDto.builder()
            .cartId(savedCart.getId())
            .checkedOut(savedCart.isCheckedOut())
            .products(List.of())
            .build();
    }
}
