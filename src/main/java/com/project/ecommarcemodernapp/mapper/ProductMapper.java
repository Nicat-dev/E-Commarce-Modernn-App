package com.project.ecommarcemodernapp.mapper;

import com.project.ecommarcemodernapp.dto.ProductDto;
import com.project.ecommarcemodernapp.dto.request.ProductRequest;
import com.project.ecommarcemodernapp.dto.response.ProductResponse;
import com.project.ecommarcemodernapp.model.Product;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper for Product entity to/from DTOs.
 */
@Mapper(componentModel = "spring")
public interface ProductMapper {

    // Request to Entity
    Product toEntity(ProductRequest request);

    // Entity to DTO
    ProductDto toDto(Product product);


    // List conversions
    List<ProductDto> toDtoList(List<Product> products);

    List<ProductResponse> toResponseList(List<Product> products);

    // Update mapping
    void updateEntityFromRequest(ProductRequest request, @MappingTarget Product entity);
}
