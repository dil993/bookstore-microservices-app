package com.bookstore.catalog.web.controller;

import com.bookstore.catalog.domain.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/products")
public class ProductController {

    @Autowired
    private ProductService productService;
    @GetMapping
    PagedResult<ProductEntity> getAllProducts(@RequestParam(name = "page", defaultValue = "1") int page) {
        return productService.getAllProducts(page);
    }

    @GetMapping("/{code}")
   ResponseEntity<Product> getProduct(@PathVariable(name = "code") String code) {
        return productService.getProductByCode(code)
                .map(ResponseEntity :: ok)
                .orElseThrow(() -> ProductNotFoundException.forCode( code));

    }
}
