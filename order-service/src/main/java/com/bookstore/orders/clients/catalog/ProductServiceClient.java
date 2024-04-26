package com.bookstore.orders.clients.catalog;


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

    public Optional<Product> getProductByCode(String code) {
        LOGGER.info("Get product by code: {}", code);
        var product= restClient
                .get()
                .uri("/products/{code}", code)
                .retrieve()
                .body(Product.class);
        return  Optional.ofNullable(product);

    }


}
