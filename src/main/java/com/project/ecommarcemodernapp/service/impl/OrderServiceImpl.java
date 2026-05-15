package com.project.ecommarcemodernapp.service.impl;

import com.project.ecommarcemodernapp.dto.OrderDto;
import com.project.ecommarcemodernapp.dto.request.OrderRequest;
import com.project.ecommarcemodernapp.exception.ApplicationException;
import com.project.ecommarcemodernapp.exception.enums.ExceptionStatus;
import com.project.ecommarcemodernapp.mapper.OrderMapper;
import com.project.ecommarcemodernapp.model.PurchaseOrder;
import com.project.ecommarcemodernapp.model.Users;
import com.project.ecommarcemodernapp.repository.OrderRepository;
import com.project.ecommarcemodernapp.service.OrderService;
import com.project.ecommarcemodernapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import static com.project.ecommarcemodernapp.exception.ApplicationException.throwIf;

/**
 * Order service implementation handling order CRUD operations.
 * Manages order creation, updates, deletion, and retrieval with proper validation.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserService userService;

    @Override
    public OrderDto createOrder(OrderRequest orderRequest) {
        validateOrderRequest(orderRequest);
        Users user = userService.user(orderRequest.userId());
        PurchaseOrder purchaseOrder = orderMapper.toEntity(orderRequest);
        purchaseOrder.setUser(user);
        purchaseOrder.getItems().forEach(item -> item.setPurchaseOrder(purchaseOrder));
        return orderMapper.toDto(orderRepository.save(purchaseOrder));
    }

    @Override
    public OrderDto updateOrder(OrderRequest orderRequest, Long orderId) {
        return orderRepository.findById(orderId).map((order -> {
            validateOrderRequest(orderRequest);
            orderMapper.updateEntityFromRequest(orderRequest, order);
            order.setUser(userService.user(orderRequest.userId()));
            order.setId(orderId);
            return orderMapper.toDto(orderRepository.save(order));
        })).orElseThrow(() -> new ApplicationException(ExceptionStatus.ORDER_NOT_FOUND));
    }

    @Override
    public void deleteOrder(Long orderId) {
        orderRepository.delete(order(orderId));
    }

    @Override
    public OrderDto getOrderById(Long orderId) {
        return orderMapper.toDto(order(orderId));
    }

    private PurchaseOrder order(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ExceptionStatus.ORDER_NOT_FOUND));
    }

    private void validateOrderRequest(OrderRequest orderRequest) {
        throwIf(orderRepository.existsByOrderCode(orderRequest.orderCode()), ExceptionStatus.ORDER_CODE_ALREADY_EXISTS);
    }
}
