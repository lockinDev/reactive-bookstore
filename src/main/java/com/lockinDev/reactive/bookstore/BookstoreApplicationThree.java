
package com.lockinDev.reactive.bookstore;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Hooks;

/**
 * Created by lockinDev on 22/07/2020
 */
@SpringBootApplication
public class BookstoreApplicationThree {

	public static void main(String... args) {
		Hooks.onOperatorDebug();
		SpringApplication springApplication = new SpringApplication(BookstoreApplicationThree.class);
		springApplication.setWebApplicationType(WebApplicationType.REACTIVE);
		springApplication.run(args);
	}
}