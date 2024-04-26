package com.bookstore.orders.domain;

import com.bookstore.orders.domain.models.CreateOrderRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class OrderService {

    private static final Logger log= LoggerFactory.getLogger(OrderService.class);

    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public String createOrder(String loginUserName, CreateOrderRequest request) {
            OrderEntity  newOrder= OrderMapper.convertToEntity(request);
            newOrder.setUserName(loginUserName);
           OrderEntity savedOrder=  orderRepository.save(newOrder);

        log.info("Created order number: {}", savedOrder.getOrderNumber());
        return savedOrder.getOrderNumber();
    }
}
