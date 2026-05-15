package com.project.ecommarcemodernapp.mapper;

import com.project.ecommarcemodernapp.dto.OrderDto;
import com.project.ecommarcemodernapp.dto.request.OrderRequest;
import com.project.ecommarcemodernapp.dto.response.OrderResponse;
import com.project.ecommarcemodernapp.model.PurchaseOrder;
import org.mapstruct.Mapper;
import org.mapstruct.MappingTarget;

import java.util.List;

/**
 * Mapper for Order entity to/from DTOs.
 */
@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {

    // Request to Entity
    PurchaseOrder toEntity(OrderRequest request);

    // Entity to DTO
    OrderDto toDto(PurchaseOrder purchaseOrder);


    // List conversions
    List<OrderDto> toDtoList(List<PurchaseOrder> purchaseOrders);

    List<OrderResponse> toResponseList(List<PurchaseOrder> purchaseOrders);

    // Update mapping
    void updateEntityFromRequest(OrderRequest request, @MappingTarget PurchaseOrder entity);
}
