
package com.lockinDev.reactive.bookstore.service;

import org.springframework.stereotype.Service;

import com.lockinDev.reactive.bookstore.document.Account;
import com.lockinDev.reactive.bookstore.repository.AccountRepository;

import reactor.core.publisher.Mono;


@Service
public class AccountServiceImpl implements  AccountService {

	private final AccountRepository accountRepository;

	public AccountServiceImpl(AccountRepository accountRepository) {
		this.accountRepository = accountRepository;
	}

	@Override
	public Mono<Account> save(Account account) {
		return accountRepository.save(account);
	}

	@Override
	public Mono<Account> getAccount(String username) {
		return accountRepository.findByUsername(username);
	}

	@Override
	public Mono<Account> getAccountLight(String username) {
		return accountRepository.findLightByUsername(username);
	}

}
