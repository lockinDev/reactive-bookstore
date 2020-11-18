
package com.lockinDev.reactive.bookstore.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;

import com.lockinDev.reactive.bookstore.document.Account;

import reactor.core.publisher.Mono;

/**
 * Created by lockinDev on 28/06/2020
 */
public interface AccountRepository  extends ReactiveMongoRepository<Account, String> {

	//@Query("{'username': ?0  }")
	Mono<Account> findByUsername(String username);

	@Query(value= "{'username': ?0  }", fields = "{'username': 1, 'password' :1, 'roles': 1 }")
	Mono<Account> findLightByUsername(String username);

}
