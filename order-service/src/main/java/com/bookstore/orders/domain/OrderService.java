package com.bookstore.orders.domain;

import com.bookstore.orders.domain.models.CreateOrderRequest;
import com.bookstore.orders.domain.models.CreateOrderResponse;
import com.bookstore.orders.domain.models.OrderCreatedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderService {

    private static final Logger log = LoggerFactory.getLogger(OrderService.class);

    private OrderRepository orderRepository;
    private OrderValidator orderValidator;
    private OrderEventService orderEventService;

    public OrderService(OrderRepository orderRepository, OrderValidator orderValidator,OrderEventService orderEventService) {
        this.orderEventService = orderEventService;
        this.orderValidator = orderValidator;
        this.orderRepository = orderRepository;
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
}
