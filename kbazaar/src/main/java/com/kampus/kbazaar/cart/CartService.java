package com.kampus.kbazaar.cart;

import org.springframework.stereotype.Service;

@Service
public class CartService {

    private CartRepository cartRepository;

    private CartItemRepository cartItemRepository;

    public CartService(CartRepository cartRepository, CartItemRepository cartItemRepository) {
        this.cartRepository = cartRepository;
        this.cartItemRepository = cartItemRepository;
    }
}
