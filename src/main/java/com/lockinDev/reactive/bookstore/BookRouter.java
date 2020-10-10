
package com.lockinDev.reactive.bookstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.*;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;


@Configuration
public class BookRouter {
	private final Logger logger = LoggerFactory.getLogger(BookRouter.class);

	@Bean
	RouterFunction<ServerResponse> routerFunction(BookHandler bookHandler) {
		return route(GET("/books"), bookHandler.list)
				.andRoute(GET("/books/{isbn}"), bookHandler::findByIsbn)
				.andRoute(POST("/books"), bookHandler::save)
				.andRoute(DELETE("/books/{id}"), bookHandler.delete)
				.filter((request, next) -> {
					logger.info("Before handler invocation: " + request.path());
					return next.handle(request);
				});
	}
}
