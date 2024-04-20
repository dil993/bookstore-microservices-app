package com.bookstore.catalog.domain;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.jdbc.Sql;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest(properties = {
		"spring.test.database.replace=none",
		"spring.datasource.url=jdbc:tc:postgresql:latest:///db"
		})

@Sql("/test-data.sql")
class ProductRepositoryTests{

	@Autowired
	private ProductRepository productRepository;


	@Test
	void shouldGetProductByCode(){
	  ProductEntity product = productRepository.findByCode("P102").orElseThrow();
	    assertThat(product.getCode()).isEqualTo("P102");
		assertThat(product.getName()).isEqualTo("The Winter is coming");
		assertThat(product.getDescription()).isEqualTo("decription of book");
		assertThat(product.getPrice()).isEqualTo(45.0);
	}

	@Test
	void shouldReturnEmptyWhenProductByCodeNotFound(){
		assertThat(productRepository.findByCode("P102323")).isEmpty();
	}
}
