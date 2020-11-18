
package com.lockinDev.reactive.bookstore.config;

import org.springframework.boot.autoconfigure.thymeleaf.ThymeleafProperties;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.format.FormatterRegistry;
import org.springframework.format.datetime.DateTimeFormatAnnotationFormatterFactory;
import org.springframework.web.reactive.config.EnableWebFlux;
import org.springframework.web.reactive.config.ViewResolverRegistry;
import org.springframework.web.reactive.config.WebFluxConfigurer;
import org.thymeleaf.spring5.ISpringWebFluxTemplateEngine;
import org.thymeleaf.spring5.view.reactive.ThymeleafReactiveViewResolver;

import com.lockinDev.reactive.bookstore.util.formatter.DateFormatAnnotationFormatterFactory;

/**
 * Created by lockinDev on 02/07/2020
 */
@Configuration
@EnableWebFlux // needed because we are also using routing functions
@EnableConfigurationProperties(ThymeleafProperties.class)
public class ReactiveThymeleafWebConfig implements WebFluxConfigurer {

	private final ISpringWebFluxTemplateEngine thymeleafTemplateEngine;

	public ReactiveThymeleafWebConfig(ISpringWebFluxTemplateEngine templateEngine) {
		this.thymeleafTemplateEngine = templateEngine;
	}

		@Bean
		public ThymeleafReactiveViewResolver thymeleafReactiveViewResolver() {
			var viewResolver = new ThymeleafReactiveViewResolver();
			viewResolver.setTemplateEngine(thymeleafTemplateEngine);
			viewResolver.setOrder(1);
			return viewResolver;
		}

		@Override
		public void configureViewResolvers(ViewResolverRegistry registry) {
			registry.viewResolver(thymeleafReactiveViewResolver());
		}

		@Bean
		public MessageSource messageSource() {
			var messageSource = new ResourceBundleMessageSource();
			messageSource.setBasenames("languages/messages");
			messageSource.setDefaultEncoding("UTF-8");
			return messageSource;
		}

	@Override
	public void addFormatters(FormatterRegistry registry) {
		registry.addFormatterForFieldAnnotation(new DateFormatAnnotationFormatterFactory());
	}

}
