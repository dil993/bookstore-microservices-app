package com.bookstore.catalog.domain;

class ProductMapper {

    static Product toProduct(ProductEntity product)
    {
        return new Product(product.getCode(), product.getName(), product.getDescription(), product.getImageUrl(), product.getPrice());
    }
}
