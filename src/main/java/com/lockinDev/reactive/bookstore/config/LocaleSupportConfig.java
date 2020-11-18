package com.lockinDev.reactive.bookstore.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.config.DelegatingWebFluxConfiguration;
import org.springframework.web.server.i18n.LocaleContextResolver;

import com.lockinDev.reactive.bookstore.config.i18n.CookieParamLocaleResolver;

/**
 * Created by lockinDev on 22/07/2020
 */
@Configuration
public class LocaleSupportConfig extends DelegatingWebFluxConfiguration {

    @Override
    protected LocaleContextResolver createLocaleContextResolver() {
        return new CookieParamLocaleResolver("lang");
    }

}
