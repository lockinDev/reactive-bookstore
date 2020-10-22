
package com.lockinDev.reactive.bookstore.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.server.PathContainer;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.reactive.function.server.support.ServerRequestWrapper;

import com.lockinDev.reactive.bookstore.handler.BookHandler;

import java.net.URI;
import static org.springframework.web.reactive.function.server.RequestPredicates.*;


@Configuration
public class RoutingConfig {
	private static Logger logger = LoggerFactory.getLogger(RoutingConfig.class);

	@Bean
	public RouterFunction<ServerResponse> staticRouter() {
		return RouterFunctions
				.resources("/static/**", new ClassPathResource("static/"));
	}
	
	private static RequestPredicate i(RequestPredicate target) {
		return new CaseInsensitiveRequestPredicate(target);
	}
}

class CaseInsensitiveRequestPredicate implements RequestPredicate {

	private final RequestPredicate target;

	CaseInsensitiveRequestPredicate(RequestPredicate target) {
		this.target = target;
	}

	@Override
	public boolean test(ServerRequest request) {
		return this.target.test(new LowerCaseUriServerRequestWrapper(request));
	}

	@Override
	public String toString() {
		return this.target.toString();
	}
}


class LowerCaseUriServerRequestWrapper extends ServerRequestWrapper {

	LowerCaseUriServerRequestWrapper(ServerRequest delegate) {
		super(delegate);
	}

	@Override
	public URI uri() {
		return URI.create(super.uri().toString().toLowerCase());
	}

	@Override
	public String path() {
		return uri().getRawPath();
	}

	@Override
	public PathContainer pathContainer() {
		return PathContainer.parsePath(path());
	}
}
