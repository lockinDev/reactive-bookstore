
package com.lockinDev.reactive.bookstore.handler;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

/**
 * Created by lockinDev on 30/07/2020
 */
@Component
public class IndexHandler {

	public Mono<ServerResponse> main(ServerRequest request) {
		return ServerResponse
				.ok()
				.contentType(MediaType.TEXT_HTML)
				.render("index");
	}
}
