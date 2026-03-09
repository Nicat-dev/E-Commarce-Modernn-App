package com.project.ecommarcemodernapp.service;

import com.project.ecommarcemodernapp.dto.ProductDto;
import com.project.ecommarcemodernapp.dto.request.ProductRequest;

public interface ProductService {
    ProductDto createProduct(ProductRequest productRequest);
    ProductDto updateProduct(ProductRequest productRequest);
    void deleteProduct(Long productId);
    ProductDto getProductById(Long productId);
}
