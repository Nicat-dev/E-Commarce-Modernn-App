package com.project.ecommarcemodernapp.mapper;

import com.project.ecommarcemodernapp.dto.OrderItemDto;
import com.project.ecommarcemodernapp.dto.request.OrderItemRequest;
import com.project.ecommarcemodernapp.model.OrderItem;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring")
public interface OrderItemMapper {
    OrderItemDto toDto(OrderItem orderItem);

    OrderItem toEntity(OrderItemRequest request);

    List<OrderItemDto> toDtoList(List<OrderItem> orderItems);
}
