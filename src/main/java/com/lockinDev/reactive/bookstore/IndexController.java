
package com.lockinDev.reactive.bookstore;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


@RestController
public class IndexController implements ApplicationContextAware {
	private ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path="/", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Pair<String,String>> index() {
		List<Pair<String,String>> info = new ArrayList<>();
		Arrays.stream(ctx.getBeanDefinitionNames()).forEach(beanName ->
			info.add(Pair.of(beanName, ctx.getBean(beanName).getClass().getName()))
		);
		return Flux.fromIterable(info);
	}
}
