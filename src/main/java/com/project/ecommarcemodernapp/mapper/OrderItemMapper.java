package com.project.ecommarcemodernapp.mapper;

import com.project.ecommarcemodernapp.dto.OrderItemDto;
import com.project.ecommarcemodernapp.dto.request.OrderItemRequest;
import com.project.ecommarcemodernapp.dto.response.OrderItemResponse;
import com.project.ecommarcemodernapp.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper for OrderItem entity to/from DTOs.
 */
@Mapper(componentModel = "spring")
public interface OrderItemMapper {

    // Request to Entity
    OrderItem toEntity(OrderItemRequest request);

    // Entity to DTO
    OrderItemDto toDto(OrderItem orderItem);


    // List conversions
    List<OrderItemDto> toDtoList(List<OrderItem> orderItems);

    List<OrderItemResponse> toResponseList(List<OrderItem> orderItems);

    // Update mapping
    void updateEntityFromRequest(OrderItemRequest request, @MappingTarget OrderItem entity);
}
