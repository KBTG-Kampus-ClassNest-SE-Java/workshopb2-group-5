package com.kampus.kbazaar.cart;

import com.kampus.kbazaar.product.ProductRequest;
import com.kampus.kbazaar.promotion.Promotion;
import com.kampus.kbazaar.promotion.PromotionRepository;
import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class CartService {

    private final CartItemRepository cartItemRepository;

    private final PromotionRepository promotionRepository;

    public CartService(
            CartItemRepository cartItemRepository, PromotionRepository promotionRepository) {
        this.cartItemRepository = cartItemRepository;
        this.promotionRepository = promotionRepository;
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
        List<CartItemResponse> cartItemResponseList =
                cartItemRepository.findAll().stream().map(this::mapToCartItemResponse).toList();
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

    public ResponseEntity<CartItemResponse> applyPromotionToCart(
            CartItemRequest cartItemRequest, String userName) {
        if (cartItemRequest.getProductSkus().isEmpty()) {
            // todo add to cart
        } else {
            // todo add to cart item
        }

        CartItemResponse cartItemResponse = new CartItemResponse();
        Optional<Promotion> applyPromotion =
                promotionRepository.findByCode(cartItemRequest.getCode());
        Promotion promotion = applyPromotion.get();
        promotion.getDiscountAmount();

        return null; // ResponseEntity.ok().body(cartItemResponseList);
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
