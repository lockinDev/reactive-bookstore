package com.lockinDev.reactive.bookstore.service;

import com.lockinDev.reactive.bookstore.document.Account;

import reactor.core.publisher.Mono;

/**
 * Created by lockinDev on 27/07/2020
 */
public interface AccountService {

	Mono<Account> save(Account account);

	Mono<Account> getAccount(String username);

	/**
	 * Loading only data for Authentication and Authorization
	 * @param username
	 * @return
	 */
	Mono<Account> getAccountLight(String username);
}
