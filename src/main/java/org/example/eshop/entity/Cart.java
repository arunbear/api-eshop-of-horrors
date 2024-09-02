package org.example.eshop.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.example.eshop.dto.CartDto;
import org.example.eshop.dto.CartItemDto;

import java.util.List;
import java.util.Set;

@Getter
@Setter
@Entity
@Table(name = "cart")
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(name = "id", nullable = false)
    private Long id;

    @OneToMany
    private Set<CartItem> cartItems;

    boolean checkedOut;

    public CartDto toDto() {
        List<CartItemDto> cartItemDtos = cartItems.stream().map(cartItem -> CartItemDto.builder()
            .productId(cartItem.getProductId())
            .quantity(cartItem.getQuantity())
            .build()
        ).toList();

        return CartDto.builder()
            .cartId(id)
            .checkedOut(checkedOut)
            .products(cartItemDtos)
            .build();
    }
}
