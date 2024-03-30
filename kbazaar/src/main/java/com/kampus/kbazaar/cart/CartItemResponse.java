package com.kampus.kbazaar.cart;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItemResponse {
    private String username;
    private List<CartItem> items;
    private BigDecimal discount;
    private BigDecimal totalDiscount;
    private BigDecimal subtotal;
    private BigDecimal grandTotal;
    private BigDecimal promotionCodes;

}
