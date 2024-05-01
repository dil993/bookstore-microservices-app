package com.bookstore.orders.domain;

import com.bookstore.orders.domain.models.*;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

public class OrderMapper {
    static OrderEntity convertToEntity(CreateOrderRequest request) {
        OrderEntity newOrder = new OrderEntity();
        newOrder.setOrderNumber(UUID.randomUUID().toString());
        newOrder.setStatus(OrderStatus.NEW);
        newOrder.setCustomer(request.customer());
        newOrder.setDeliveryAddress(request.deliveryAddress());
        Set<OrderItemEntity> orderItems = new HashSet<>();
        for (OrderItem item : request.items()) {
            OrderItemEntity orderItem = new OrderItemEntity();
            orderItem.setCode(item.code());
            orderItem.setName(item.name());
            orderItem.setPrice(item.price());
            orderItem.setQuantity(item.quantity());
            orderItem.setOrder(newOrder);
            orderItems.add(orderItem);
        }
        newOrder.setItems(orderItems);
        return newOrder;
    }

    public static OrderDTO convertToDTO(OrderEntity orderEntity) {
      Set<OrderItem> items =orderEntity.getItems().stream()
                .map(item -> new OrderItem(item.getCode(), item.getName(), item.getPrice(), item.getQuantity()))
                .collect(Collectors.toSet());

        return new OrderDTO(
                orderEntity.getOrderNumber(),
                orderEntity.getUserName(),
                items,
                orderEntity.getCustomer(),
                orderEntity.getDeliveryAddress(),
                orderEntity.getStatus(),
                orderEntity.getComments(),
                orderEntity.getCreatedAt()
        );
    }
}
