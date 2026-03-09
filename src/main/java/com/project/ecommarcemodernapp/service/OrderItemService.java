package com.project.ecommarcemodernapp.service;

import com.project.ecommarcemodernapp.dto.OrderItemDto;
import com.project.ecommarcemodernapp.dto.request.OrderItemRequest;

public interface OrderItemService {
    OrderItemDto createOrderItem(OrderItemRequest orderItemRequest);
    OrderItemDto updateOrderItem(OrderItemRequest orderItemRequest);
    void deleteOrderItem(Long orderItemId);
    OrderItemDto getOrderItemById(Long orderItemId);
}
