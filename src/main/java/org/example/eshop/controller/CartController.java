package org.example.eshop.controller;

import org.example.eshop.dto.CartDto;
import org.example.eshop.dto.CartItemDto;
import org.example.eshop.dto.CheckOutDto;
import org.example.eshop.service.CartService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @Autowired
    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ResponseEntity<CartDto> create() {
        CartDto cartDto = cartService.createCart();

        URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(42)
                .toUri();
        return ResponseEntity.created(uri).body(cartDto);
    }

    @PutMapping("/{cartId}")
    public ResponseEntity<CartDto> modify(@PathVariable long cartId, @RequestBody List<CartItemDto> updates) {
        CartDto cartDto = CartDto.builder()
            .cartId(cartId)
            .products(updates)
            .build();
        cartService.applyUpdates(cartId, updates);

        return ResponseEntity.ok(cartDto);
    }

    @PostMapping("/{cartId}/checkout")
    public ResponseEntity<CheckOutDto> checkOut(@PathVariable long cartId) {
        CheckOutDto checkOutDto = cartService.checkOutCart(cartId);

        return ResponseEntity.ok(checkOutDto);
    }

    @GetMapping
    public ResponseEntity<List<CartDto>> retrieveAll() {
        List<CartDto> carts = cartService.findAll();
        return ResponseEntity.ok(carts);
    }
}
