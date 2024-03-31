package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.product.ProductRequest;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1")
public class CartController {

    private final CartService cartService;

    @GetMapping("/carts")
    public ResponseEntity<List<CartItemResponse>> getAllCarts() {
        return cartService.getAllCarts();
    }

    @PostMapping("/carts/{username}/items")
    public ResponseEntity<CartItemResponse> addProductToCart(
            @RequestBody ProductRequest productRequest, @PathVariable String username) {
        return cartService.addProductToCart(productRequest, username);
    }

    @PostMapping("/carts/{username}/promotions")
    public ResponseEntity<CartItemResponse> applyPromotionToCart(
            @RequestBody CartItemRequest cartItemRequest, @PathVariable String username) {
        return cartService.applyPromotionToCart(cartItemRequest, username);
    }
}
