package com.lockinDev.reactive.bookstore.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.query.Query;

import com.lockinDev.reactive.bookstore.document.Book;

import reactor.core.publisher.Flux;

/**
 * Created by lockinDev on 26/07/2020
 */
public interface CustomBookRepository {
	Flux<Book> findRandom(Pageable pageable);

	Flux<Book> findAll(Query query);
}
