
package com.lockinDev.reactive.bookstore.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.*;

import com.lockinDev.reactive.bookstore.document.Account;
import com.lockinDev.reactive.bookstore.service.AccountService;
import com.lockinDev.reactive.bookstore.util.validation.AccountValidator;

import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.util.Locale;
import java.util.Map;
import java.util.TreeMap;


@Controller
@RequestMapping("/customer/register")
public class RegistrationController {
	private final Logger logger = LoggerFactory.getLogger(RegistrationController.class);

	private final AccountService accountService;

	public RegistrationController(AccountService accountService) {
		this.accountService = accountService;
	}

	@ModelAttribute("countries")
	public Map<String, String> countries(Locale currentLocale) {
		var countries = new TreeMap<String, String>();
		for (var locale : Locale.getAvailableLocales()) {
			countries.put(locale.getCountry(), locale.getDisplayCountry(currentLocale));
		}
		return countries;
	}

	@GetMapping
	@ModelAttribute
	public Account register(Locale currentLocale) {
		var account = new Account();
		account.getAddress().setCountry(currentLocale.getCountry());
		return account;
	}

	@RequestMapping(method = { RequestMethod.POST, RequestMethod.PUT })
	public String handleRegistration(@Valid @ModelAttribute Account account, BindingResult result) {
		if (result.hasErrors()) {
			return "customer/register";
		}
		// we are not interested in the result of this pipeline, we just want to make sure the user is saved,
		// and we do not care how fast that happens
		this.accountService.save(account).doOnNext(acc ->  logger.debug("Account saved")).then(Mono.empty()).subscribe();
		return "redirect:/login";
	}

}
