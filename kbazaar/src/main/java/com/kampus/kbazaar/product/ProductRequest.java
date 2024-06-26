package com.kampus.kbazaar.product;

import java.math.BigDecimal;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductRequest {
    private String sku;
    private String name;
    private BigDecimal price;
    private int quantity;
    private BigDecimal discount;
    private String promotionCodes;

    public ProductRequest(
            String s,
            String pencils,
            BigDecimal bigDecimal,
            BigDecimal bigDecimal1,
            int i,
            BigDecimal bigDecimal2,
            String s1) {}
}
