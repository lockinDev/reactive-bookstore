package com.lockinDev.reactive.bookstore.service;

import com.lockinDev.reactive.bookstore.document.Account;

import reactor.core.publisher.Mono;


public interface AccountService {

	Mono<Account> save(Account account);

	Mono<Account> getAccount(String username);

	
	Mono<Account> getAccountLight(String username);
}
