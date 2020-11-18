
package com.lockinDev.reactive.bookstore.controller;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Created by lockinDev on 02/07/2020
 */
@RestController
public class IndexController implements ApplicationContextAware {

	private ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}


	@ResponseBody // The response payload for this request will be rendered in JSON
	@RequestMapping(value = "/beans", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
	public Flux<String> getBeanNames() {
		List<String> beans = Arrays.stream(ctx.getBeanDefinitionNames()).sorted().collect(Collectors.toList());
		return Flux.fromIterable(beans).delayElements(Duration.ofMillis(200));
	}

}
