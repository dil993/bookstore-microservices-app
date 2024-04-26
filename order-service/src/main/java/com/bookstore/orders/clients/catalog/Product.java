package com.bookstore.orders.clients.catalog;

public record Product(
        String code,
        String name,
        String description,
        String imageUrl,
        double price
) {
}

