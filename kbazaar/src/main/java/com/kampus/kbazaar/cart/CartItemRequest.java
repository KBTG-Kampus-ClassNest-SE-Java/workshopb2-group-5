package com.kampus.kbazaar.cart;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class CartItemRequest {
    private String code;
    private BigDecimal discountAmount;
    private String productSkus;
}
