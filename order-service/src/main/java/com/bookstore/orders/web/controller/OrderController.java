package com.bookstore.orders.web.controller;

import com.bookstore.orders.domain.OrderNotFoundException;
import com.bookstore.orders.domain.OrderService;
import com.bookstore.orders.domain.SecurityService;
import com.bookstore.orders.domain.models.CreateOrderRequest;
import com.bookstore.orders.domain.models.CreateOrderResponse;
import com.bookstore.orders.domain.models.OrderDTO;
import com.bookstore.orders.domain.models.OrderSummary;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/orders")
class OrderController {

    private static final Logger log = LoggerFactory.getLogger(OrderController.class);

    private final OrderService orderService;

    private final SecurityService securityService;


    public OrderController(OrderService orderService, SecurityService securityService) {
        this.orderService = orderService;
        this.securityService = securityService;
    }

    @PostMapping
    public CreateOrderResponse createOrder(@RequestBody @Valid CreateOrderRequest request) {
        log.info("Creating order for user: {}", securityService.getLoginUserName());
        return orderService.createOrder(
                securityService.getLoginUserName(),
                request);
    }

    @GetMapping
    public List<OrderSummary> getOrders() {
        return orderService.getOrders(securityService.getLoginUserName());
    }

    @GetMapping("/{orderNumber}")
    public OrderDTO getOrder(@PathVariable("orderNumber") String orderNumber) {
        return orderService.getOrderSummary(orderNumber, securityService.getLoginUserName())
                .orElseThrow(() -> new OrderNotFoundException("Order not found" + orderNumber));
    }


}
