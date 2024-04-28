package com.bookstore.orders.domain;

import com.bookstore.orders.clients.catalog.Product;
import com.bookstore.orders.clients.catalog.ProductServiceClient;
import com.bookstore.orders.domain.models.CreateOrderRequest;
import com.bookstore.orders.domain.models.OrderItem;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;


@Component
public class OrderValidator {

    private static final Logger logger = LoggerFactory.getLogger(OrderValidator.class);

    private final ProductServiceClient productServiceClient;

    OrderValidator(ProductServiceClient productServiceClient) {
        this.productServiceClient = productServiceClient;
    }


    public void validateOrder(CreateOrderRequest order) {
        for (OrderItem item : order.items()) {
            Product product = productServiceClient.getProductByCode(item.code())
                    .orElseThrow(() -> new OrderNotFoundException("Order not found for product code: " + item.code()));
            if (item.price().compareTo(product.price()) != 0) {
                logger.error("Product price not matching Actual price for product code: {}, received price: {}", item.code(), product.price());
                throw new IllegalArgumentException("Price mismatch for product code: " + item.code());
            }
        }
    }
}
