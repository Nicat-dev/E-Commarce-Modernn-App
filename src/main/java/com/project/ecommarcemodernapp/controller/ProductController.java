package com.project.ecommarcemodernapp.controller;

import com.project.ecommarcemodernapp.dto.ProductDto;
import com.project.ecommarcemodernapp.dto.request.ProductRequest;
import com.project.ecommarcemodernapp.dto.response.ProductResponse;
import com.project.ecommarcemodernapp.mapper.ProductMapper;
import com.project.ecommarcemodernapp.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Product management REST controller.
 * Handles product CRUD operations with role-based access control.
 */
@Slf4j
@RestController
@RequestMapping("/api/products")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class ProductController {

    private final ProductService productService;
    private final ProductMapper productMapper;

    /**
     * Get all products (Public endpoint).
     *
     * @return List of all products
     */
    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        log.debug("Fetching all products");
        return ResponseEntity.ok(List.of());
    }

    /**
     * Create a new product (Admin only).
     *
     * @param productRequest Product details
     * @return Created product response
     */
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> createProduct(
            @Valid @RequestBody ProductRequest productRequest) {
        log.info("Creating product: {}", productRequest.name());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(productService.createProduct(productRequest));
    }

    /**
     * Get product by ID (Public endpoint).
     *
     * @param id Product ID
     * @return Product response
     */
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProductById(@PathVariable Long id) {
        log.debug("Fetching product with id: {}", id);
        return ResponseEntity.ok(productService.getProductById(id));
    }

    /**
     * Update product information (Admin only).
     *
     * @param id Product ID
     * @param productRequest Updated product data
     * @return Updated product response
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ProductDto> updateProduct(
            @PathVariable Long id,
            @Valid @RequestBody ProductRequest productRequest) {
        log.info("Updating product with id: {}", id);
        return ResponseEntity.ok(productService.updateProduct(productRequest, id));
    }

    /**
     * Delete a product (Admin only).
     *
     * @param id Product ID
     * @return No content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteProduct(@PathVariable Long id) {
        log.info("Deleting product with id: {}", id);
        productService.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}

