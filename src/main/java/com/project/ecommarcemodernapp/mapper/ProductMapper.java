package com.project.ecommarcemodernapp.mapper;

import com.project.ecommarcemodernapp.dto.ProductDto;
import com.project.ecommarcemodernapp.dto.request.ProductRequest;
import com.project.ecommarcemodernapp.model.Product;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ProductMapper {
    ProductDto toDto(Product product);

    Product toEntity(ProductRequest request);

    List<ProductDto> toDtoList(List<Product> products);
}
