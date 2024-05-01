package com.bookstore.orders.clients.catalog;

import com.bookstore.orders.ApplicationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class CatalogServiceClientConfig {

    @Bean
    RestClient restClient(ApplicationProperties properties)
    {
        return RestClient.builder()
                .baseUrl(properties.catalogServiceUrl())
                .build();
    }
}
