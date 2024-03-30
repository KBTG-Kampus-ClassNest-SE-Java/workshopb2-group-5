package com.kampus.kbazaar.product;

import com.kampus.kbazaar.exceptions.NotFoundException;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

import java.awt.print.Pageable;
import java.util.List;
import java.util.Optional;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class ProductController {

    private ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @ApiResponse(
            responseCode = "200",
            description = "list all products",
            content = {
                    @Content(
                            mediaType = "application/json",
                            array =
                            @ArraySchema(
                                    schema = @Schema(implementation = ProductResponse.class)))
            })
    @ApiResponse(
            responseCode = "500",
            description = "internal server error",
            content =
            @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = NotFoundException.class)))
    @GetMapping("/products")
    public List<ProductResponse> getProducts(@RequestParam(name = "page", required = false) Optional<Integer> page, @RequestParam(name = "per_page", required = false) Optional<Integer> perPage) {
        if (page != null) {
            return productService.getPagination(Integer.valueOf(page.get())-1, Integer.valueOf(perPage.get()));
        } else {
            return productService.getAll();
        }

    }

    @ApiResponse(
            responseCode = "200",
            description = "get product by sku",
            content = {
                    @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProductResponse.class))
            })
    @ApiResponse(
            responseCode = "404",
            description = "product not found",
            content =
            @Content(
                    mediaType = "application/json",
                    schema = @Schema(implementation = NotFoundException.class)))
    @GetMapping("/products/{sku}")
    public ProductResponse getProductById(@PathVariable String sku) {
        return productService.getBySku(sku);
    }
}
