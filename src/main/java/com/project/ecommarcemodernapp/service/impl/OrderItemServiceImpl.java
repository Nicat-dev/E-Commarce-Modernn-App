package com.project.ecommarcemodernapp.service.impl;

import com.project.ecommarcemodernapp.dto.OrderItemDto;
import com.project.ecommarcemodernapp.dto.request.OrderItemRequest;
import com.project.ecommarcemodernapp.exception.ApplicationException;
import com.project.ecommarcemodernapp.exception.enums.ExceptionStatus;
import com.project.ecommarcemodernapp.mapper.OrderItemMapper;
import com.project.ecommarcemodernapp.model.OrderItem;
import com.project.ecommarcemodernapp.model.Product;
import com.project.ecommarcemodernapp.repository.OrderItemRepository;
import com.project.ecommarcemodernapp.service.OrderItemService;
import com.project.ecommarcemodernapp.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.function.Predicate;

import static com.project.ecommarcemodernapp.exception.ApplicationException.throwIf;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository repository;
    private final OrderItemMapper orderItemMapper;
    private final ProductService productService;

    private final Predicate<OrderItemRequest> isValidQuantity = request -> request.quantity() > 0;

    @Override
    public OrderItemDto createOrderItem(OrderItemRequest orderItemRequest) {
        validateOrderItemRequest(orderItemRequest);
        Product product = productService.getProductEntityById(orderItemRequest.productId());

        /*comment: Nijat changes*/
        // Check if the requested quantity exceeds available stock

        OrderItem orderItem = orderItemMapper.toEntity(orderItemRequest);
        orderItem.setProduct(product);

        return orderItemMapper.toDto(repository.save(orderItem));
    }

    @Override
    public OrderItemDto updateOrderItem(OrderItemRequest orderItemRequest, Long orderItemId) {
        return repository.findById(orderItemId).map((orderItem) -> {
            orderItemMapper.updateEntityFromRequest(orderItemRequest, orderItem);
            validateOrderItemRequest(orderItemRequest);
            orderItem.setId(orderItemId);
            Product product = productService.getProductEntityById(orderItemRequest.productId());
            orderItem.setProduct(product);
            orderItem.setQuantity(orderItemRequest.quantity());
            return orderItemMapper.toDto(repository.save(orderItem));
        }).orElseThrow(() -> new ApplicationException(ExceptionStatus.ORDER_ITEM_NOT_FOUND));
    }

    @Override
    public void deleteOrderItem(Long orderItemId) {
        repository.delete(getOrderItem(orderItemId));
    }

    @Override
    public OrderItemDto getOrderItemById(Long orderItemId) {
        return orderItemMapper.toDto(getOrderItem(orderItemId));
    }

    private OrderItem getOrderItem(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new ApplicationException(ExceptionStatus.ORDER_ITEM_NOT_FOUND));
    }

    private void validateOrderItemRequest(OrderItemRequest orderItemRequest) {
        throwIf(!isValidQuantity.test(orderItemRequest), ExceptionStatus.INVALID_ORDER_ITEM_QUANTITY);
    }
}
