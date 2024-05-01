package com.bookstore.orders.domain;

import com.bookstore.orders.domain.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Component
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private static final List<String> DELIVERED_COUNTRIES = Arrays.asList("US", "CA", "INDIA", "CHINA");
    private OrderRepository orderRepository;
    private OrderValidator orderValidator;
    private OrderEventService orderEventService;

    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator, OrderEventService orderEventService) {
        this.orderEventService = orderEventService;
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
    }

    public List<OrderSummary> getOrders(String loginUserName) {
        return orderRepository.findByUserName(loginUserName).stream()
                .toList();
    }


    public CreateOrderResponse createOrder(String loginUserName, CreateOrderRequest request) {
        orderValidator.validateOrder(request);
        OrderEntity newOrder = OrderMapper.convertToEntity(request);
        newOrder.setUserName(loginUserName);
        OrderEntity savedOrder = orderRepository.save(newOrder);
        log.info("Created order number: {}", savedOrder.getOrderNumber());
        OrderCreatedEvent orderCreatedEvent = OrderEventMapper.buildOrderCreatedEvent(savedOrder);
        orderEventService.saveOrder(orderCreatedEvent);
        return new CreateOrderResponse(savedOrder.getOrderNumber());
    }

    public void processNewOrders() {

        List<OrderEntity> newOrders = orderRepository.findByStatus(OrderStatus.NEW);
        log.info("Found {} new orders", newOrders.size());
        for (OrderEntity order : newOrders) {
            this.processOrder(order);
        }

    }

    private void processOrder(OrderEntity order) {
        try {
            if (canBeDelivered(order)) {
                log.info("Order {} can be delivered", order.getOrderNumber());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.DELIVERED);
                orderEventService.saveDeliveredOrder(OrderEventMapper.buildOrderDeliveredEvent(order));
            } else {
                log.error("Order {} cannot be delivered", order.getOrderNumber());
                orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.CANCELLED);
                orderEventService.saveCancelOrder(OrderEventMapper.buildOrderCancelledEvent(order, "Cannot deliver to this country"));
            }
        } catch (Exception e) {
            log.error("Error processing order {} - message: {}", order.getOrderNumber(), e.getMessage());
            orderRepository.updateOrderStatus(order.getOrderNumber(), OrderStatus.ERROR);
            orderEventService.saveErrorOrder(OrderEventMapper.buildOrderErrorEvent(order, e.getMessage()));

        }
    }

    private boolean canBeDelivered(OrderEntity order) {
        return DELIVERED_COUNTRIES.contains(order.getDeliveryAddress().country().toUpperCase());
    }

    public Optional<OrderDTO> getOrderSummary(String orderNumber, String loginUserName) {

        return orderRepository.findByUserNameAndOrderNumber(loginUserName, orderNumber)
                .map(OrderMapper::convertToDTO);
    }
}
