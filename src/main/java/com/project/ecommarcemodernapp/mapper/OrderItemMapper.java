package com.project.ecommarcemodernapp.mapper;

import com.project.ecommarcemodernapp.dto.OrderItemDto;
import com.project.ecommarcemodernapp.dto.request.OrderItemRequest;
import com.project.ecommarcemodernapp.model.OrderItem;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemDto toDto(OrderItem orderItem);

    OrderItem toEntity(OrderItemRequest request);

    void updateEntityFromRequest(OrderItemRequest request, @MappingTarget OrderItem entity);

    List<OrderItemDto> toDtoList(List<OrderItem> orderItems);
}
