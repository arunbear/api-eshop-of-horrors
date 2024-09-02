package org.example.eshop.service;

import org.example.eshop.CartNotFoundException;
import org.example.eshop.ProductNotFoundException;
import org.example.eshop.dto.CartDto;
import org.example.eshop.dto.CartItemDto;
import org.example.eshop.dto.CheckOutDto;
import org.example.eshop.entity.Cart;
import org.example.eshop.entity.CartItem;
import org.example.eshop.entity.Product;
import org.example.eshop.repository.CartItemRepository;
import org.example.eshop.repository.CartRepository;
import org.example.eshop.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;

    @Autowired
    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository, ProductRepository productRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
        this.productRepository = productRepository;
    }

    public CartDto createCart() {
        Cart cart = cartRepository.save(new Cart());

        return CartDto.builder()
            .cartId(cart.getId())
            .products(List.of())
            .build();
    }

    public CheckOutDto checkOutCart(long cartId) {
        Cart foundCart = cartRepository.findById(cartId)
            .orElseThrow(() -> new CartNotFoundException(cartId));

        double totalCost = calculateTotalCost(foundCart);

        foundCart.setCheckedOut(true);
        Cart savedCart = cartRepository.save(foundCart);

        CartDto cartDto = savedCart.toDto();

        return CheckOutDto.builder()
            .cart(cartDto)
            .totalCost(totalCost)
            .build();
    }

    private double calculateTotalCost(Cart cart) {
        var totalCost = 0.0;

        for (CartItem cartItem: cart.getCartItems()) {

            Product product = productRepository.findById(cartItem.getProductId())
                .orElseThrow(() -> new ProductNotFoundException(cartItem.getProductId()));
            totalCost += cartItem.getQuantity() * product.getPrice();
        }
        return totalCost;
    }

    public void applyUpdates(long cartId, List<CartItemDto> updates) {
        Cart foundCart = cartRepository.findById(cartId)
            .orElseThrow(() -> new CartNotFoundException(cartId));
        Set<CartItem> newItems = new HashSet<>();

        for (CartItemDto cartItemDto: updates) {
            CartItem item = new CartItem();
            item.setProductId(cartItemDto.productId());
            item.setQuantity(cartItemDto.quantity());
            cartItemRepository.save(item);
            newItems.add(item);
        }

        foundCart.setCartItems(newItems);
        cartRepository.save(foundCart);
    }

    public List<CartDto> findAll() {
        var carts = cartRepository.findAll();
        return carts.stream().map(cart -> cart.toDto()).toList();
    }
}
