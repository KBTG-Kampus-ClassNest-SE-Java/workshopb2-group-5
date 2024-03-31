package com.kampus.kbazaar.cart;

public class CartItemRequest {
    private String code;
    private String productSkus;

    public CartItemRequest(String code, String productSkus) {
        this.code = code;
        this.productSkus = productSkus;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getProductSkus() {
        return productSkus;
    }

    public void setProductSkus(String productSkus) {
        this.productSkus = productSkus;
    }

    public String toString() {
        return "{" + "\"code\":\"" + code + "\"," + "\"productSkus\":\"" + productSkus + "\"" + "}";
    }
}
