
package com.lockinDev.reactive.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;


@SpringBootApplication
public class BookstoreApplicationOne {

	public static void main(String... args) {
		SpringApplication springApplication = new SpringApplication(BookstoreApplicationOne.class);
		springApplication.setWebApplicationType(WebApplicationType.REACTIVE);
		springApplication.run(args);
	}
}