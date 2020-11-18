
package com.lockinDev.reactive.bookstore.repository;

import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

import com.lockinDev.reactive.bookstore.document.Book;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;


/**
 * Created by lockinDev on 28/06/2020
 */
public interface BookRepository extends ReactiveMongoRepository<Book, String>, CustomBookRepository  {

	@Query("{'category': { '$regex' : ?0 } }")
	Flux<Book> findByCategory(String category);

	Mono<Book> findByIsbn(String isbn);

	@Query(value= "{}", fields ="{'id': 1, 'isbn' : 1, 'category'  :1 }")
	Flux<Book> findAllLight();
}
