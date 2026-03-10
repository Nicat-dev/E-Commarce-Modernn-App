package com.project.ecommarcemodernapp.service.impl;

import com.project.ecommarcemodernapp.dto.ProductDto;
import com.project.ecommarcemodernapp.dto.request.ProductRequest;
import com.project.ecommarcemodernapp.mapper.ProductMapper;
import com.project.ecommarcemodernapp.repository.ProductRepository;
import com.project.ecommarcemodernapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;



    @Override
    public ProductDto createProduct(ProductRequest productRequest) {
        return null;
    }

    @Override
    public ProductDto updateProduct(ProductRequest productRequest) {
        return null;
    }

    @Override
    public void deleteProduct(Long productId) {

    }

    @Override
    public ProductDto getProductById(Long productId) {
        return null;
    }
}
