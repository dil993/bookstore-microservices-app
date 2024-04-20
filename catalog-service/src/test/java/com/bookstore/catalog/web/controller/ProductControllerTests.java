package com.bookstore.catalog.web.controller;

import com.bookstore.catalog.AbstractIT;
import com.bookstore.catalog.domain.Product;
import com.bookstore.catalog.domain.ProductEntity;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import org.springframework.test.context.jdbc.Sql;

import static io.restassured.RestAssured.given;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

@Sql("/test-data.sql")
class ProductControllerTests extends AbstractIT {


	@Test
	void shouldGetProducts() {
		given().contentType(ContentType.JSON)
				.when()
				.get("/api/products")
				.then()
				.statusCode(200)
				//.body("totalElements", hasSize(3))
				.body("pageNumber", is(1))
				.body("totalPages", is(1))
				.body("isfirst",is(true))
				.body("islast",is(true))
				.body("hasNext", is(false))
				.body("hasPrev", is(false));
	}

	@Test
	void shouldGetProductByCode() {
	Product product= given().contentType(ContentType.JSON)
				.when()
				.get("/api/products/{code}","P102")
				.then()
				.statusCode(200)
				.assertThat()
				.extract()
				.body()
				.as(Product.class);

		assertThat(product.code()).isEqualTo("P102");
		assertThat(product.name()).isEqualTo("The Winter is coming");
		assertThat(product.description()).isEqualTo("decription of book");
		assertThat(product.price()).isEqualTo(45.0);
	}

	@Test
	void shouldGetProductByCodeNotFound() {
		 given().contentType(ContentType.JSON)
				.when()
				.get("/api/products/{code}","P10232")
				.then()
				.statusCode(404)
				 .body("status",is(404))
				 .body("title",is("Product Not Found"));
	}
}
