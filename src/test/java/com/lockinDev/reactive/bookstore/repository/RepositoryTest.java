
package com.lockinDev.reactive.bookstore.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.lockinDev.reactive.bookstore.document.*;
import com.lockinDev.reactive.bookstore.repository.AccountRepository;
import com.lockinDev.reactive.bookstore.repository.BookRepository;
import com.lockinDev.reactive.bookstore.util.BookSearchCriteria;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@DataMongoTest
public class RepositoryTest {
	private final Logger logger = LoggerFactory.getLogger(RepositoryTest.class);

	@Autowired BookRepository bookRepository;
	@Autowired AccountRepository accountRepository;

	@BeforeEach
	public void setup() {
		var book1 = Book.create("Dummy Book One", BigDecimal.valueOf(23.39), 1983, "Dum Dum", "1111484227893", "Dum");
		var book2 = Book.create("Dummy Book Two", BigDecimal.valueOf(30.99), 1974, "Dim Dim", "1111484229999", "Dim");

		Address address = new Address();
		address.setStreet("Test Street");
		address.setCity("Somewhere");
		address.setCountry("Space");

		Account test = new Account();
		test.setFirstName("test");
		test.setUsername("test");
		test.setLastName("test");
		test.setEmailAddress("test@doe.com");
		test.setPassword("test");
		test.setAddress(address);
		test.setRoles(List.of("ROLE_TEST"));

		Order order = new Order();
		order.setOrderDate(new Date());
		order.setShippingAddress(test.getAddress());
		order.setDeliveryDate(new Date());
		order.setBillingSameAsShipping(true);

		bookRepository.saveAll(Flux.just(book1, book2))
				.thenMany(bookRepository.findAll())
				.thenMany(bookRepository.findByIsbn("1111484229999"))
				.doOnNext(
						book -> {
							OrderDetail orderDetail = new OrderDetail(book, 2);
							order.addOrderDetail(orderDetail);
							test.setOrders(List.of(order));
						}
				).then(accountRepository.save(test))
				.thenMany(accountRepository.findAll())
				.blockLast();  // accepted in a test context
	}

	@Test
	void testBooksIds() {
		 bookRepository.findAllLight().as(StepVerifier::create)
				 .expectNextCount(2)
				 .verifyComplete();
	}

	@Test
	void testFindByCategory() {
		bookRepository.findByCategory("Dum").as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void testFindRandom(){
		PageRequest request = PageRequest.of(0, 1);
		bookRepository.findRandom(request)
				.log()
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void testQuery(){
		BookSearchCriteria bookSearchCriteria  = new BookSearchCriteria();
		bookSearchCriteria.setCategory("Dim");
		Query query = new Query();
		if (bookSearchCriteria.getTitle() != null) {
			query.addCriteria(Criteria.where("title").is(bookSearchCriteria.getTitle()));
		}
		if (bookSearchCriteria.getCategory() != null) {
			query.addCriteria(Criteria.where("category").is(bookSearchCriteria.getCategory()));
		}

		bookRepository.findAll(query)
				.log()
				.as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void testFindByUsername(){
		accountRepository.findByUsername("test")
				.log()
				.as(StepVerifier::create)
				//.expectNextCount(1)
				.expectNextMatches(acc -> acc.getUsername().equals("test"))
				.verifyComplete();
	}

	@Test
	void testFindLightByUsername(){
		accountRepository.findLightByUsername("test")
				.as(StepVerifier::create)
				.expectNextMatches(acc -> acc.getOrders() == null)
				.verifyComplete();
	}

	@Test
	void testOrdersByAccount(){
		accountRepository.findByUsername("test")
				.log()
				.as(StepVerifier::create)
				.expectNextMatches(acc -> acc.getOrders().size() == 1)
				.verifyComplete();
	}

	@AfterEach
	public void tearDown() {
		bookRepository.deleteAll()
				.thenMany(accountRepository.deleteAll())
				.subscribe(
						unused -> logger.info("Cleanup done!"),
						error -> logger.error("Cleanup failed! ", error)
				);
	}
}
