package com.project.ecommarcemodernapp.service.impl;

import com.project.ecommarcemodernapp.dto.OrderDto;
import com.project.ecommarcemodernapp.dto.request.OrderRequest;
import com.project.ecommarcemodernapp.exception.ApplicationException;
import com.project.ecommarcemodernapp.exception.enums.ExceptionStatus;
import com.project.ecommarcemodernapp.mapper.OrderMapper;
import com.project.ecommarcemodernapp.model.Order;
import com.project.ecommarcemodernapp.model.Users;
import com.project.ecommarcemodernapp.repository.OrderRepository;
import com.project.ecommarcemodernapp.service.OrderService;
import com.project.ecommarcemodernapp.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Predicate;

import static com.project.ecommarcemodernapp.exception.ApplicationException.throwIf;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final UserService userService;

    // Validation predicates
    private final Predicate<String> isOrderCodeUnique = orderCode -> !orderRepository.existsByOrderCode(orderCode);

    @Override
    public OrderDto createOrder(OrderRequest orderRequest) {
        validateOrderRequest(orderRequest);
        Users user = userService.user(orderRequest.userId());
        Order order = orderMapper.toEntity(orderRequest);
        order.setUser(user);
        order.getItems().forEach(item -> item.setOrder(order));
        return orderMapper.toDto(orderRepository.save(order));
    }

    @Override
    public OrderDto updateOrder(OrderRequest orderRequest,Long orderId) {
        return orderRepository.findById(orderId).map((order -> {
            validateOrderRequest(orderRequest);
            orderMapper.updateEntityFromRequest(orderRequest,order);
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

    private Order order(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new ApplicationException(ExceptionStatus.ORDER_NOT_FOUND));
    }

    private void validateOrderRequest(OrderRequest orderRequest) {
        throwIf(!isOrderCodeUnique.test(orderRequest.orderCode()), ExceptionStatus.ORDER_CODE_ALREADY_EXISTS);
    }
}
