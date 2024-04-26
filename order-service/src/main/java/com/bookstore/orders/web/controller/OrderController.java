package com.bookstore.orders.web.controller;

import com.bookstore.orders.domain.OrderService;
import com.bookstore.orders.domain.SecurityService;
import com.bookstore.orders.domain.models.CreateOrderRequest;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/orders")
class OrderController {

    private static final Logger log= LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    private final SecurityService securityService;


    public OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping
    public String createOrder(@RequestBody @Valid CreateOrderRequest request) {
        log.info("Creating order for user: {}", securityService.getLoginUserName());
        return orderService.createOrder(securityService.getLoginUserName(),request);
    }

}
