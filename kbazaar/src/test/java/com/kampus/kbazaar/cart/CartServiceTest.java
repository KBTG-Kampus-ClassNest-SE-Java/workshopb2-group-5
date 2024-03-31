package com.kampus.kbazaar.cart;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

public class CartServiceTest {

    @Mock private CartItemRepository cartItemRepository;

    @InjectMocks private CartService cartService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        List<CartItemResponse> cartItemResponseList =
                new ArrayList<>(
                        List.of(
                                new CartItemResponse(
                                        "TechNinja",
                                        new ArrayList<CartItem>(
                                                List.of(
                                                        new CartItem(
                                                                2L,
                                                                "TechNinja",
                                                                "MOBILE-APPLE-IPHONE-12-PRO",
                                                                "Apple iPhone 12 Pro",
                                                                BigDecimal.valueOf(12990.75),
                                                                1,
                                                                BigDecimal.valueOf(0.00),
                                                                null))),
                                        BigDecimal.valueOf(0),
                                        BigDecimal.valueOf(0),
                                        BigDecimal.valueOf(38972.25),
                                        BigDecimal.valueOf(38972.25),
                                        "")));
    }

    @Test
    @DisplayName("should return all three different users")
    public void shouldReturnAllThreeDifferentUsers() {
        CartItem mockCartItem =
                new CartItem(
                        2L,
                        "TechNinja",
                        "MOBILE-APPLE-IPHONE-12-PRO",
                        "Apple iPhone 12 Pro",
                        BigDecimal.valueOf(12990.75),
                        1,
                        BigDecimal.valueOf(0.00),
                        null);
        CartItem mockCartItem2 =
                new CartItem(
                        3L,
                        "TechNinja2",
                        "MOBILE-APPLE-IPHONE-12-PRO",
                        "Apple iPhone 12 Pro",
                        BigDecimal.valueOf(12990.75),
                        1,
                        BigDecimal.valueOf(0.00),
                        null);
        CartItem mockCartItem3 =
                new CartItem(
                        4L,
                        "TechNinja3",
                        "MOBILE-APPLE-IPHONE-12-PRO",
                        "Apple iPhone 12 Pro",
                        BigDecimal.valueOf(12990.75),
                        1,
                        BigDecimal.valueOf(0.00),
                        null);
        when(cartItemRepository.findAll())
                .thenReturn(new ArrayList<>(List.of(mockCartItem, mockCartItem2, mockCartItem3)));

        // Call service method
        ResponseEntity<List<CartItemResponse>> result = cartService.getAllCarts();

        // Assertions
        assertEquals("TechNinja", result.getBody().get(0).getUsername());
        assertEquals("TechNinja2", result.getBody().get(1).getUsername());
        assertEquals("TechNinja3", result.getBody().get(2).getUsername());
    }
}
