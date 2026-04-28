package com.project.ecommarcemodernapp.controller;

import com.project.ecommarcemodernapp.dto.OrderItemDto;
import com.project.ecommarcemodernapp.dto.request.OrderItemRequest;
import com.project.ecommarcemodernapp.service.OrderItemService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/order-items")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderItemController {

    private final OrderItemService orderItemService;

    // For test purpose only

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderItemDto> createOrderItem(
            @Valid @RequestBody OrderItemRequest orderItemRequest) {
        log.info("Creating order item with product id: {}", orderItemRequest.productId());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderItemService.createOrderItem(orderItemRequest));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderItemDto> getOrderItemById(@PathVariable Long id) {
        log.debug("Fetching order item with id: {}", id);
        return ResponseEntity.ok(orderItemService.getOrderItemById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderItemDto> updateOrderItem(
            @PathVariable Long id,
            @Valid @RequestBody OrderItemRequest orderItemRequest) {
        log.info("Updating order item with id: {}", id);
        return ResponseEntity.ok(orderItemService.updateOrderItem(orderItemRequest, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteOrderItem(@PathVariable Long id) {
        log.info("Deleting order item with id: {}", id);
        orderItemService.deleteOrderItem(id);
        return ResponseEntity.noContent().build();
    }
}

