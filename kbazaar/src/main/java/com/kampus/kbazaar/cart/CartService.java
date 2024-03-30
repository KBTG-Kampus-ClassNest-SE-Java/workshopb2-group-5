package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.product.ProductRequest;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;

    private final CartItemRepository cartItemRepository;

    public ResponseEntity<CartItemResponse> addProductToCart(
            ProductRequest productRequest, String username) {
        CartItem cartItem =
                CartItem.builder()
                        .username(username)
                        .sku(productRequest.getSku())
                        .name(productRequest.getName())
                        .price(productRequest.getPrice())
                        .quantity(productRequest.getQuantity())
                        .discount(productRequest.getDiscount())
                        .promotionCodes(productRequest.getPromotionCodes())
                        .build();
        cartItemRepository.save(cartItem);

        return ResponseEntity.ok().body(mapToCartItemResponse(cartItem));
    }

    public CartItemResponse mapToCartItemResponse(CartItem cartItem) {
        List<CartItem> cartItemList = cartItemRepository.findAllByUsername(cartItem.getUsername());
        BigDecimal total =
                cartItemList.stream()
                        .filter(cartItem1 -> cartItem1.getPrice().compareTo(BigDecimal.ZERO) != 0)
                        .map(CartItem::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        CartItemResponse cartItemResponse =
                CartItemResponse.builder()
                        .username(cartItem.getUsername())
                        .items(cartItemList)
                        .discount(cartItem.getDiscount())
                        .totalDiscount(BigDecimal.valueOf(0))
                        .subtotal(total)
                        .grandTotal(total)
                        .promotionCodes(cartItem.getPromotionCodes())
                        .build();
        return cartItemResponse;
    }
}
