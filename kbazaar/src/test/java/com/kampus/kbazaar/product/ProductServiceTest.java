package com.kampus.kbazaar.product;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

import com.kampus.kbazaar.exceptions.NotFoundException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

class ProductServiceTest {

    @Mock private ProductRepository productRepository;

    @InjectMocks private ProductService productService;

    List<Product> productList;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        productList =
                new ArrayList<>(
                        List.of(
                                new Product(
                                        1L,
                                        "Pens",
                                        "STATIONERY-PEN-BIC-BALLPOINT",
                                        new BigDecimal(14.99),
                                        100),
                                new Product(
                                        2L,
                                        "Pens",
                                        "STATIONERY-PEN-BIC-BALLPOINT",
                                        new BigDecimal(14.99),
                                        100),
                                new Product(
                                        3L,
                                        "Pens",
                                        "STATIONERY-PEN-BIC-BALLPOINT",
                                        new BigDecimal(14.99),
                                        100),
                                new Product(
                                        4L,
                                        "Pens",
                                        "STATIONERY-PEN-BIC-BALLPOINT",
                                        new BigDecimal(14.99),
                                        100),
                                new Product(
                                        5L,
                                        "Pens",
                                        "STATIONERY-PEN-BIC-BALLPOINT",
                                        new BigDecimal(14.99),
                                        100),
                                new Product(
                                        6L,
                                        "Pens",
                                        "STATIONERY-PEN-BIC-BALLPOINT",
                                        new BigDecimal(14.99),
                                        100),
                                new Product(
                                        7L,
                                        "Pens",
                                        "STATIONERY-PEN-BIC-BALLPOINT",
                                        new BigDecimal(14.99),
                                        100),
                                new Product(
                                        8L,
                                        "Pens",
                                        "STATIONERY-PEN-BIC-BALLPOINT",
                                        new BigDecimal(14.99),
                                        100),
                                new Product(
                                        9L,
                                        "Pens",
                                        "STATIONERY-PEN-BIC-BALLPOINT",
                                        new BigDecimal(14.99),
                                        100),
                                new Product(
                                        10L,
                                        "Pens",
                                        "STATIONERY-PEN-BIC-BALLPOINT",
                                        new BigDecimal(14.99),
                                        100)));
    }

    @Test
    @DisplayName("should be able to get all products")
    void shouldBeAbleToGetAllProducts() {
        // Mock data
        Product product1 =
                new Product(
                        1L,
                        "Google Pixel 5",
                        "MOBILE-GOOGLE-PIXEL-5",
                        new BigDecimal(12990.75),
                        100);
        Product product2 =
                new Product(2L, "Coca-Cola", "BEV-COCA-COLA", new BigDecimal(20.75), 150);
        List<Product> productList = Arrays.asList(product1, product2);

        // Mock repository method
        when(productRepository.findAll()).thenReturn(productList);

        // Call service method
        List<ProductResponse> result = productService.getAll();

        // Assertions
        assertEquals(2, result.size());
        assertEquals("Google Pixel 5", result.get(0).name());
        assertEquals("BEV-COCA-COLA", result.get(1).sku());
    }

    @Test
    @DisplayName("should return empty list when no product found")
    void shouldReturnEmptyListWhenNoProductFoundGetAllProducts() {
        // Mock repository method returning empty list
        when(productRepository.findAll()).thenReturn(Arrays.asList());

        // Call service method
        List<ProductResponse> result = productService.getAll();

        // Assertions
        assertTrue(result.isEmpty());
    }

    @Test
    @DisplayName("should be able to get product by SKU")
    void shouldBeAbleToGetProductBySku() {
        // Mock data
        Product product =
                new Product(1L, "Pens", "STATIONERY-PEN-BIC-BALLPOINT", new BigDecimal(14.99), 100);

        // Mock repository method
        when(productRepository.findBySku("STATIONERY-PEN-BIC-BALLPOINT"))
                .thenReturn(Optional.of(product));

        // Call service method
        ProductResponse result = productService.getBySku("STATIONERY-PEN-BIC-BALLPOINT");

        // Assertions
        assertEquals("Pens", result.name());
        assertEquals(new BigDecimal(14.99), result.price());
    }

    @Test
    @DisplayName("should return null when get product non-existing SKU")
    void shouldReturnNullWhenGetProductNonExistingSKU() {
        // Mock repository method returning empty optional
        when(productRepository.findBySku(anyString())).thenReturn(Optional.empty());

        // Assertions
        assertThrows(NotFoundException.class, () -> productService.getBySku("NonExistingSKU"));
    }

    @Test
    @DisplayName("should return 0 page limit 2")
    void shouldReturnEqualTwoPage() {
        // Mock data
        Product product1 =
                new Product(
                        1L,
                        "Google Pixel 5",
                        "MOBILE-GOOGLE-PIXEL-5",
                        new BigDecimal(12990.75),
                        100);
        Product product2 =
                new Product(2L, "Coca-Cola", "BEV-COCA-COLA", new BigDecimal(20.75), 150);

        List<Product> productList = Arrays.asList(product1, product2);
        Pageable pageable = PageRequest.of(0, 2);
        Page<Product> page = new PageImpl<>(productList);
        when(productRepository.findAll(pageable)).thenReturn(page);

        // Call service method
        List<ProductResponse> result = productService.getPagination(0, 2);

        // Assertions
        assertEquals(2, result.size());
        assertEquals("Google Pixel 5", result.get(0).name());
        assertEquals("BEV-COCA-COLA", result.get(1).sku());
    }

    @Test
    @DisplayName("should return page 2 limit 2")
    void shouldReturnPage2limit2() {
        int pageNumber = 2;
        int pageSize = 2;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> page =
                new PageImpl<>(
                        productList.subList(pageSize * pageSize, pageNumber * pageSize + pageSize));
        when(productRepository.findAll(pageable)).thenReturn(page);

        // Call service method
        List<ProductResponse> result = productService.getPagination(pageNumber, pageSize);

        // Assertions
        assertEquals(2, result.size());
        assertEquals(5L, result.get(0).id());
        assertEquals(6L, result.get(1).id());
    }

    @Test
    @DisplayName("should return empty list when page = 6 and size =2 ")
    void shouldReturnEmptyList() {
        int pageNumber = 6;
        int pageSize = 2;
        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Product> page = new PageImpl<>(new ArrayList<Product>());
        when(productRepository.findAll(pageable)).thenReturn(page);

        // Call service method
        List<ProductResponse> result = productService.getPagination(pageNumber, pageSize);
        System.out.println(result);

        // Assertions
        assertEquals(0, result.size());
    }
}
