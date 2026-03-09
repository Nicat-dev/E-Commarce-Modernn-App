package com.project.ecommarcemodernapp.mapper;

import com.project.ecommarcemodernapp.dto.OrderDto;
import com.project.ecommarcemodernapp.dto.request.OrderRequest;
import com.project.ecommarcemodernapp.model.Order;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {
    OrderDto toDto(Order order);

    Order toEntity(OrderRequest request);

    List<OrderDto> toDtoList(List<Order> orders);
}
