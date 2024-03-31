package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.promotion.PromotionRepository;
import java.math.BigDecimal;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final PromotionRepository promotionRepository;

    public List<CartItem> getCartItemsByUsernameAndSku(String username, String sku) {
        return cartItemRepository.findAllByUsernameAndSku(username, sku);
    }

    public BigDecimal calculateTotalDiscount(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItem::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateSubTotal(List<CartItem> cartItems) {
        return cartItems.stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateGrandTotal(BigDecimal discount, BigDecimal subtTotal) {
        return subtTotal.subtract(discount);
    }
}
