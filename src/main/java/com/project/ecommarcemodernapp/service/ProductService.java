package com.project.ecommarcemodernapp.service;

import com.project.ecommarcemodernapp.dto.ProductDto;
import com.project.ecommarcemodernapp.dto.request.ProductRequest;
import com.project.ecommarcemodernapp.model.Product;

public interface ProductService {
    ProductDto createProduct(ProductRequest productRequest);
    ProductDto updateProduct(ProductRequest productRequest, Long productId);
    void deleteProduct(Long productId);
    ProductDto getProductById(Long productId);
    Product getProductEntityById(Long productId);
}
