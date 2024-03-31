package com.kampus.kbazaar.product;

import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.kampus.kbazaar.security.JwtAuthFilter;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

@ExtendWith(SpringExtension.class)
@AutoConfigureMockMvc(addFilters = false)
@WebMvcTest(
        controllers = ProductController.class,
        excludeFilters =
                @ComponentScan.Filter(
                        type = FilterType.ASSIGNABLE_TYPE,
                        classes = JwtAuthFilter.class))
public class ProductControllerTest {

    @Autowired private MockMvc mockMvc;

    @MockBean private ProductService productService;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("should return all product")
    public void shouldReturnAllProduct() throws Exception {
        // Given

        // When & Then
        when(productService.getAll()).thenReturn(new ArrayList<>());

        mockMvc.perform(get("/api/v1/products").contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService, times(1)).getAll();
    }

    @Test
    @DisplayName("should return 2 product based on 1 page and 1 perPage")
    public void shouldReturnSomeProduct() throws Exception {
        ProductResponse productResponse =
                new ProductResponse(1L, "mock_product", "mock_sku", BigDecimal.valueOf(10), 10);
        ProductResponse productResponse2 =
                new ProductResponse(2L, "mock_product2", "mock_sku2", BigDecimal.valueOf(11), 11);
        List<ProductResponse> mockResponse = new ArrayList<>();
        mockResponse.add(productResponse);
        mockResponse.add(productResponse2);

        // When & Then
        when(productService.getPagination(any(), any())).thenReturn(mockResponse);

        mockMvc.perform(
                        get("/api/v1/products?page=1&per_page=2")
                                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(status().isOk());

        verify(productService, times(0)).getAll();
    }

    @Test
    @DisplayName("should return product")
    public void shouldReturnProduct() throws Exception {
        // Given
        String sku = "PROMO-1";

        // When & Then
        when(productService.getBySku(sku)).thenReturn(null);

        mockMvc.perform(get("/api/v1/products/" + sku).contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        verify(productService, times(1)).getBySku(sku);
    }

    @Test
    @DisplayName("should return ALL if only 1 page, but missing per_page query param")
    public void shouldReturnAllProductWhenMissionPerPage() throws Exception {
        ProductResponse productResponse =
                new ProductResponse(1L, "mock_product", "mock_sku", BigDecimal.valueOf(10), 10);
        ProductResponse productResponse2 =
                new ProductResponse(2L, "mock_product2", "mock_sku2", BigDecimal.valueOf(11), 11);
        List<ProductResponse> mockResponse = new ArrayList<>();
        mockResponse.add(productResponse);
        mockResponse.add(productResponse2);

        // When & Then
        when(productService.getAll()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/products?page=1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(status().isOk());

        verify(productService, times(1)).getAll();
    }

    @Test
    @DisplayName("should return ALL if only 1 per_page, but missing page query param")
    public void shouldReturnAllProductWhenMissionPage() throws Exception {
        ProductResponse productResponse =
                new ProductResponse(1L, "mock_product", "mock_sku", BigDecimal.valueOf(10), 10);
        ProductResponse productResponse2 =
                new ProductResponse(2L, "mock_product2", "mock_sku2", BigDecimal.valueOf(11), 11);
        List<ProductResponse> mockResponse = new ArrayList<>();
        mockResponse.add(productResponse);
        mockResponse.add(productResponse2);

        // When & Then
        when(productService.getAll()).thenReturn(mockResponse);

        mockMvc.perform(get("/api/v1/products?per_page=1").contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$.length()", is(2)))
                .andExpect(status().isOk());

        verify(productService, times(1)).getAll();
    }
}
