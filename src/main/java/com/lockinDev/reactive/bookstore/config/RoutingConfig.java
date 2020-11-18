
package com.lockinDev.reactive.bookstore.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.RouterFunctions;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.lockinDev.reactive.bookstore.handler.BookHandler;
import com.lockinDev.reactive.bookstore.handler.IndexHandler;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;

/**
 * Created by lockinDev on 27/07/2020
 */
@Configuration
public class RoutingConfig {
	private static Logger logger = LoggerFactory.getLogger(RoutingConfig.class);

	@Bean
	public RouterFunction<ServerResponse> staticRouter() {
		return RouterFunctions
				.resources("/static/**", new ClassPathResource("static/"));
	}

	@Bean
	public RouterFunction<ServerResponse> bookRouter(IndexHandler indexHandler, BookHandler bookHandler) {

		return RouterFunctions
				.route(GET("/"), indexHandler::main)
				.andRoute(GET("/index.htm"), indexHandler::main)
				.andRoute(POST("/book/search"), bookHandler::search)
				.andRoute(GET("/book/random"), bookHandler.random)
				.filter((request, next) -> {
					logger.info("Before handler invocation: " + request.path());
					return next.handle(request);
				});
	}
}
