package com.bookstore.orders.domain;

import com.bookstore.orders.clients.catalog.ProductServiceClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class OrderValidator {

    private static final Logger logger = LoggerFactory.getLogger(OrderValidator.class);

    private final ProductServiceClient  productServiceClient;

    OrderValidator(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }


    public void validateOrder(OrderEntity order) {
        for (OrderItemEntity item : order.getItems()) {
            productServiceClient.getProduct(item.getCode());
        }
}
