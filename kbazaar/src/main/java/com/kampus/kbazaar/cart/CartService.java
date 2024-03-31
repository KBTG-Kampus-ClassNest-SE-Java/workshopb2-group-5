package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.product.ProductRequest;
import com.kampus.kbazaar.promotion.PromotionRepository;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;

    private final PromotionRepository promotionRepository;

    private final CartItemService cartItemService;

    @Value("${enabled.shipping.fee:false}")
    private boolean enableShippingFee;

    private BigDecimal spippingFeeConstant = new BigDecimal(25);

    public CartService(
            CartItemRepository cartItemRepository,
            PromotionRepository promotionRepository,
            CartItemService cartItemService) {
        this.cartItemRepository = cartItemRepository;
        this.promotionRepository = promotionRepository;
        this.cartItemService = cartItemService;
    }

    public CartItemResponse addProductToCart(ProductRequest productRequest, String username) {
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

        return mapToCartItemResponse(cartItem);
    }

    public ResponseEntity<List<CartItemResponse>> getAllCarts() {
        List<CartItem> cartItemList = cartItemRepository.findAll().stream().toList();
        List<String> usernameList =
                cartItemList.stream().map(CartItem::getUsername).distinct().toList();
        List<CartItemResponse> cartItemResponseList = new ArrayList<>();
        for (String username : usernameList) {
            List<CartItem> itemsList =
                    cartItemList.stream()
                            .filter(cartItem -> cartItem.getUsername().equals(username))
                            .toList();
            BigDecimal total =
                    itemsList.stream()
                            .filter(
                                    cartItem1 ->
                                            cartItem1.getPrice().compareTo(BigDecimal.ZERO) != 0)
                            .map(CartItem::getPrice)
                            .reduce(BigDecimal.ZERO, BigDecimal::add);
            CartItemResponse cartItemResponse =
                    CartItemResponse.builder()
                            .username(username)
                            .items(itemsList)
                            .discount(BigDecimal.valueOf(0))
                            .totalDiscount(BigDecimal.valueOf(0))
                            .subtotal(total)
                            .grandTotal(total)
                            .promotionCodes("")
                            .build();
            cartItemResponseList.add(cartItemResponse);
        }
        return ResponseEntity.ok().body(cartItemResponseList);
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

    public BigDecimal calculateTotalDiscount(List<CartItem> cartItems) {
        return cartItems.stream()
                .map(CartItem::getDiscount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public BigDecimal calculateSubTotal(List<CartItem> cartItems) {
        return cartItems.stream().map(CartItem::getPrice).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public ResponseEntity<CartItemResponse> applyPromotionToCart(
            CartItemRequest cartItemRequest, String userName) {
        BigDecimal discount = new BigDecimal(0);
        BigDecimal shippingFee = getShippingFee();
        String promotionCodes = "";
        List<CartItem> cartItemList = cartItemRepository.findAllByUsername(userName);
        if (!cartItemRequest.getProductSkus().isEmpty()) {
            cartItemList =
                    cartItemService.getCartItemAndApplySpecificCode(userName, cartItemRequest);
        } else {
            discount = cartItemRequest.getDiscountAmount();
            promotionCodes = cartItemRequest.getCode();
        }

        BigDecimal totalDiscount = calculateTotalDiscount(cartItemList).add(discount);
        BigDecimal subtotal = calculateSubTotal(cartItemList);
        BigDecimal grandTotal = subtotal.subtract(totalDiscount);
        CartItemResponse cartItemResponse =
                CartItemResponse.builder()
                        .username(userName)
                        .items(cartItemList)
                        .discount(discount)
                        .shippingFee(shippingFee)
                        .totalDiscount(totalDiscount)
                        .subtotal(subtotal)
                        .grandTotal(grandTotal)
                        .promotionCodes(promotionCodes)
                        .build();
        return ResponseEntity.ok().body(cartItemResponse);
    }

    public BigDecimal getShippingFee() {
        BigDecimal shippingFee = new BigDecimal(0);
        // logic
        if (enableShippingFee) {
            shippingFee = spippingFeeConstant;
        }
        return shippingFee;
    }

    public CartItemResponse mapToDto(String username) {
        List<CartItem> cartItemList = cartItemRepository.findAllByUsername(username);
        BigDecimal total =
                cartItemList.stream()
                        .filter(cartItem1 -> cartItem1.getPrice().compareTo(BigDecimal.ZERO) != 0)
                        .map(CartItem::getPrice)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        CartItemResponse cartItemResponse =
                CartItemResponse.builder()
                        .username(username)
                        .items(cartItemList)
                        .discount(BigDecimal.valueOf(0))
                        .totalDiscount(BigDecimal.valueOf(0))
                        .promotionCodes("")
                        .subtotal(total)
                        .grandTotal(total)
                        .build();
        return cartItemResponse;
    }
}
