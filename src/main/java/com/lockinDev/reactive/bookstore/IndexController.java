
package com.lockinDev.reactive.bookstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseCookie;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@RestController
public class IndexController implements ApplicationContextAware {
	private final Logger logger = LoggerFactory.getLogger(IndexController.class);

	private ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path="/", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Mono<String> index(){
		return Mono.just("It works!");
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path="/debug", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Pair<String,String>> debug(ServerWebExchange exchange) {
		if(Objects.requireNonNull(exchange.getRequest().getHeaders().get("user-agent")).stream().anyMatch(v-> v.startsWith("curl"))){
			logger.debug("Development request with id: {}", exchange.getRequest().getId());
			ResponseCookie devCookie = ResponseCookie.from("Invoking.Environment.Cookie", "dev").maxAge(Duration.ofMinutes(5)).build();
			exchange.getResponse().addCookie(devCookie);
		}
		List<Pair<String,String>> info = new ArrayList<>();
		Arrays.stream(ctx.getBeanDefinitionNames()).forEach(beanName ->
			info.add(Pair.of(beanName, ctx.getBean(beanName).getClass().getName()))
		);
		return Flux.fromIterable(info).zipWith(Flux.interval(Duration.ofSeconds(1))).map(Tuple2::getT1);
	}
}
