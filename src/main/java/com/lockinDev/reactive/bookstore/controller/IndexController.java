
package com.lockinDev.reactive.bookstore.controller;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.thymeleaf.spring5.context.webflux.IReactiveDataDriverContextVariable;
import org.thymeleaf.spring5.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;

import java.time.Duration;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;


@Controller
public class IndexController implements ApplicationContextAware {

	private ApplicationContext ctx;

	@Override
	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
		ctx = applicationContext;
	}

	@GetMapping(path = {"/", "index.htm"})
	public String index(final Model model) {
		List<String> beans = Arrays.stream(ctx.getBeanDefinitionNames()).sorted().collect(Collectors.toList());
		Flux<String> flux = Flux.fromIterable(beans).delayElements(Duration.ofMillis(200));
		IReactiveDataDriverContextVariable dataDriver =
				new ReactiveDataDriverContextVariable( flux,10);

		model.addAttribute("beans", dataDriver);

		return "index";
	}
}
