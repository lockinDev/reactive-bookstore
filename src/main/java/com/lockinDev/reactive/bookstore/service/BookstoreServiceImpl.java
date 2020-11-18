
package com.lockinDev.reactive.bookstore.service;

import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.lockinDev.reactive.bookstore.document.*;
import com.lockinDev.reactive.bookstore.repository.AccountRepository;
import com.lockinDev.reactive.bookstore.repository.BookRepository;
import com.lockinDev.reactive.bookstore.util.BookNewReleasesUtil;
import com.lockinDev.reactive.bookstore.util.BookSearchCriteria;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;

/**
 * Created by lockinDev on 27/07/2020
 */
@Service
@Transactional(readOnly = true)
public class BookstoreServiceImpl implements  BookstoreService {
	private static final int RANDOM_BOOKS = 2;

	private final BookRepository bookRepository;
	private final AccountRepository accountRepository;

	public BookstoreServiceImpl(BookRepository bookRepository, AccountRepository accountRepository) {
		this.bookRepository = bookRepository;
		this.accountRepository = accountRepository;
	}

	@Override
	public Flux<Book> findBooksByCategory(String category) {
		return this.bookRepository.findByCategory(category);
	}

	@Override
	public Mono<Book> findBook(String id) {
		return this.bookRepository.findById(id);
	}

	@Override
	public Mono<Book> findBookByIsbn(String isbn) {
		return this.bookRepository.findByIsbn(isbn);
	}

	@Override
	public Flux<Book> findRandomBooks() {
		PageRequest request = PageRequest.of(0, RANDOM_BOOKS);
		return this.bookRepository.findRandom(request);
	}

	@Override
	public Mono<Order> findOrder(String id) {
		// TODO not sure if this is needed anymore
		return null;
	}

	@Override
	public Mono<List<Order>> findOrdersForAccountId(String accountId) {
		return this.accountRepository.findById(accountId).map(Account::getOrders);
	}

	@Override
	public Flux<Book> findBooks(BookSearchCriteria bookSearchCriteria) {
		if(bookSearchCriteria.isEmpty()) {
			return Flux.empty();
		}
		Query query = new Query();
		if (StringUtils.isNotEmpty(bookSearchCriteria.getTitle())) {
			query.addCriteria(Criteria.where("title").is(bookSearchCriteria.getTitle()));
		}
		if (StringUtils.isNotEmpty(bookSearchCriteria.getCategory())) {
			query.addCriteria(Criteria.where("category").is(bookSearchCriteria.getCategory()));
		}
		return bookRepository.findAll(query);
	}

	@Override
	public Mono<Order> createOrder(Cart cart, Account customer) {
		var order = new Order();
		for (Map.Entry<Book, Integer> line : cart.getBooks().entrySet()) {
			order.addOrderDetail(new OrderDetail(line.getKey(), line.getValue()));
		}
		order.setId(UUID.randomUUID().toString());
		order.setOrderDate(new Date());
		customer.addOrder(order);
		return Mono.just(order);
	}

	@Override
	public Mono<Book> addBook(Book book) {
		return this.bookRepository.save(book);
	}

	@Override
	public Mono<Void> deleteBook(String bookIsbn) {
		return bookRepository.findByIsbn(bookIsbn).doOnNext(bookRepository::delete).then(Mono.empty());
	}

	@Override
	public Flux<Book> randomBookNews() {
		return Flux.interval(Duration.ofSeconds(5)).map(delay -> BookNewReleasesUtil.randomRelease());
	}
}
