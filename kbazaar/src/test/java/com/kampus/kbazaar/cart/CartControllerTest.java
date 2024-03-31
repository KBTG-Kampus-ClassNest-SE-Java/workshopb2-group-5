package com.kampus.kbazaar.cart;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kampus.kbazaar.product.ProductRequest;
import com.kampus.kbazaar.security.JwtAuthFilter;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@ExtendWith(SpringExtension.class)
@WebMvcTest(
        controllers = CartController.class,
        excludeFilters =
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtAuthFilter.class))
public class CartControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private CartService cartService;

    @InjectMocks private CartController cartController;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
        this.cartController = new CartController(cartService);
        this.mockMvc = MockMvcBuilders.standaloneSetup(cartController).build();
    }

    @Test
    @DisplayName("should return username and status created")
    public void addProductToCarts() throws Exception {
        String username = "TechNinja";
        ProductRequest productRequest =
                new ProductRequest(
                        "STATIONERY-PENCIL-FABER-CASTELL",
                        "Pencils",
                        BigDecimal.valueOf(10.25),
                        BigDecimal.valueOf(1),
                        1,
                        BigDecimal.valueOf(0),
                        "");
        CartItemResponse cartItemResponse = new CartItemResponse();

        when(cartService.addProductToCart(productRequest, username)).thenReturn(cartItemResponse);

        mockMvc.perform(
                        post("/api/v1/carts/TechNinja/items")
                                .content(
                                        "{\n"
                                                + "  \"sku\": \"STATIONERY-PENCIL-FABER-CASTELL\",\n"
                                                + "  \"name\": \"Pencils\",\n"
                                                + "  \"price\": 10.25,\n"
                                                + "  \"quantity\": 1,\n"
                                                + "  \"discount\": 0,\n"
                                                + "  \"promotionCodes\": \"\"\n"
                                                + "}")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }
}
