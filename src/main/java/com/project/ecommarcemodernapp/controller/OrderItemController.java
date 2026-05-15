package com.project.ecommarcemodernapp.controller;

import com.project.ecommarcemodernapp.dto.OrderItemDto;
import com.project.ecommarcemodernapp.dto.request.OrderItemRequest;
import com.project.ecommarcemodernapp.dto.response.OrderItemResponse;
import com.project.ecommarcemodernapp.mapper.OrderItemMapper;
import com.project.ecommarcemodernapp.service.OrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

/**
 * Order item management REST controller.
 * Handles order item CRUD operations for authenticated users.
 */
@Slf4j
@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderItemController {

    private final OrderItemService orderItemService;
    private final OrderItemMapper orderItemMapper;

    /**
     * Create a new order item (User only).
     *
     * @param orderItemRequest Order item details
     * @return Created order item response
     */
    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderItemDto> createOrderItem(
            @Valid @RequestBody OrderItemRequest orderItemRequest) {
        log.info("Creating order item with product id: {}", orderItemRequest.productId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderItemService.createOrderItem(orderItemRequest));
    }

    /**
     * Get order item by ID (User only).
     *
     * @param id Order item ID
     * @return Order item response with product details
     */
    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderItemDto> getOrderItemById(@PathVariable Long id) {
        log.debug("Fetching order item with id: {}", id);
        return ResponseEntity.ok(orderItemService.getOrderItemById(id));
    }

    /**
     * Update order item information (User only).
     *
     * @param id Order item ID
     * @param orderItemRequest Updated order item data
     * @return Updated order item response
     */
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderItemDto> updateOrderItem(
            @PathVariable Long id,
            @Valid @RequestBody OrderItemRequest orderItemRequest) {
        log.info("Updating order item with id: {}", id);
        return ResponseEntity.ok(orderItemService.updateOrderItem(orderItemRequest, id));
    }

    /**
     * Delete an order item (User only).
     *
     * @param id Order item ID
     * @return No content
     */
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        log.info("Deleting order item with id: {}", id);
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }
}

