package com.project.ecommarcemodernapp.controller;

import com.project.ecommarcemodernapp.dto.OrderDto;
import com.project.ecommarcemodernapp.dto.request.OrderRequest;
import com.project.ecommarcemodernapp.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
@CrossOrigin(origins = "*", maxAge = 3600)
public class OrderController {

    private final OrderService orderService;

    @PostMapping
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDto> createOrder(
            @Valid @RequestBody OrderRequest orderRequest) {
        log.info("Creating order with code: {}", orderRequest.orderCode());
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(orderService.createOrder(orderRequest));
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDto> getOrderById(@PathVariable Long id) {
        log.debug("Fetching order with id: {}", id);
        return ResponseEntity.ok(orderService.getOrderById(id));
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<OrderDto> updateOrder(
            @PathVariable Long id,
            @Valid @RequestBody OrderRequest orderRequest) {
        log.info("Updating order with id: {}", id);
        return ResponseEntity.ok(orderService.updateOrder(orderRequest, id));
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id) {
        log.info("Deleting order with id: {}", id);
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }
}

