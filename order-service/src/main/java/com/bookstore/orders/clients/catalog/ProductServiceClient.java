package com.bookstore.orders.clients.catalog;


import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import io.github.resilience4j.retry.annotation.Retry;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;

import java.util.Optional;

@Component
public class ProductServiceClient {

    private static final Logger LOGGER = LoggerFactory.getLogger(ProductServiceClient.class);

    private final RestClient restClient;

    public ProductServiceClient(RestClient restClient) {
        this.restClient = restClient;
    }

    @CircuitBreaker(name = "catalog-service")
    @Retry(name = "catalog-service", fallbackMethod = "getProductByCodeFallback")
    public Optional<Product> getProductByCode(String code) {
            LOGGER.info("Get product by code: {}", code);
            var product = restClient
                    .get()
                    .uri("/products/{code}", code)
                    .retrieve()
                    .body(Product.class);
            return Optional.ofNullable(product);

    }


    public Optional<Product> getProductByCodeFallback(String code, Exception e) {
        LOGGER.error("Error while getting product by code: {} , error: {}", code, e.getMessage());
        return Optional.empty();
    }

}
