package com.project.ecommarcemodernapp.service;

import com.project.ecommarcemodernapp.dto.OrderDto;
import com.project.ecommarcemodernapp.dto.request.OrderRequest;

public interface OrderService {
    OrderDto createOrder(OrderRequest orderRequest);
    OrderDto updateOrder(OrderRequest orderRequest, Long orderId);
    void  deleteOrder(Long orderId);
    OrderDto getOrderById(Long orderId);
}
