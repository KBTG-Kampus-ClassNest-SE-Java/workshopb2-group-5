package com.kampus.kbazaar.cart;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {
    private final CartItemRepository cartItemRepository;

    public List<CartItem> getCartItemAndApplySpecificCode(
            String userName, CartItemRequest cartItemRequest) {
        String productSkus = cartItemRequest.getProductSkus();
        BigDecimal discountAmount = cartItemRequest.getDiscountAmount();
        String promotionCodes = cartItemRequest.getCode();
        List<CartItem> cartItemByUsernameList = cartItemRepository.findAllByUsername(userName);
        List<String> skuList = Arrays.stream(productSkus.split(",")).toList();
        List<CartItem> cartItemFilterList =
                cartItemByUsernameList.stream()
                        .filter(cartItem -> skuList.contains(cartItem.getSku()))
                        .toList();

        cartItemFilterList.forEach(
                cartItem -> {
                    cartItem.setDiscount(discountAmount);
                    cartItem.setPromotionCodes(promotionCodes);
                });

        return cartItemFilterList;
    }
}
