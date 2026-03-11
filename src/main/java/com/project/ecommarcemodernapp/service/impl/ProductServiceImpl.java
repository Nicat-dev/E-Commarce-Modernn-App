package com.project.ecommarcemodernapp.service.impl;

import com.project.ecommarcemodernapp.dto.ProductDto;
import com.project.ecommarcemodernapp.dto.request.ProductRequest;
import com.project.ecommarcemodernapp.exception.ApplicationException;
import com.project.ecommarcemodernapp.exception.enums.ExceptionStatus;
import com.project.ecommarcemodernapp.mapper.ProductMapper;
import com.project.ecommarcemodernapp.model.Product;
import com.project.ecommarcemodernapp.repository.ProductRepository;
import com.project.ecommarcemodernapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Predicate;

import static com.project.ecommarcemodernapp.exception.ApplicationException.throwIf;

@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;

    private final Predicate<ProductRequest> isValidPrice = request -> request.price() > 0;

    @Override
    public ProductDto createProduct(ProductRequest productRequest) {
        validateProductRequest(productRequest);
        Product product = productMapper.toEntity(productRequest);
        return productMapper.toDto(productRepository.save(product));
    }

    @Override
    public ProductDto updateProduct(ProductRequest productRequest, Long productId) {
        Product existingProduct = getProductEntity(productId);

        validateProductRequest(productRequest);

        existingProduct.setName(productRequest.name());
        existingProduct.setDescription(productRequest.description());
        existingProduct.setPrice(productRequest.price());

        return productMapper.toDto(productRepository.save(existingProduct));
    }

    @Override
    public void deleteProduct(Long productId) {
        productRepository.delete(getProductEntity(productId));
    }

    @Override
    public ProductDto getProductById(Long productId) {
        return productMapper.toDto(getProductEntity(productId));
    }

    @Override
    public Product getProductEntityById(Long productId) {
        return getProductEntity(productId);
    }

    private Product getProductEntity(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ExceptionStatus.PRODUCT_NOT_FOUND));
    }

    private void validateProductRequest(ProductRequest productRequest) {
        throwIf(!isValidPrice.test(productRequest), ExceptionStatus.INVALID_PRODUCT_PRICE);
    }
}
