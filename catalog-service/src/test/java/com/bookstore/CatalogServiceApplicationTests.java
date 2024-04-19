package com.bookstore;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;

@SpringBootTest
@Import(ContainerConfig.class)
class CatalogServiceApplicationTests {


	@Test
	void contextLoads() {
	}

}
