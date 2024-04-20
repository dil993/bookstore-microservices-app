package com.bookstore.catalog.domain;

public class ProductNotFoundException extends RuntimeException{

    public ProductNotFoundException(String message) {
        super(message);
    }

    public static RuntimeException forCode(String s) {
        return new ProductNotFoundException("Invalid product code: " +s);
    }
}
