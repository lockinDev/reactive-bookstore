
package com.lockinDev.reactive.bookstore.repository;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;

import com.lockinDev.reactive.bookstore.document.*;
import com.lockinDev.reactive.bookstore.repository.AccountRepository;
import com.lockinDev.reactive.bookstore.repository.BookRepository;
import com.lockinDev.reactive.bookstore.service.BookstoreService;
import com.lockinDev.reactive.bookstore.service.BookstoreServiceImpl;
import com.lockinDev.reactive.bookstore.util.BookSearchCriteria;

import reactor.core.publisher.Flux;
import reactor.test.StepVerifier;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;


@DataMongoTest
@Import(BookstoreServiceImpl.class)
public class BookstoreServiceTest {
	private final Logger logger = LoggerFactory.getLogger(BookstoreServiceTest.class);

	@Autowired BookRepository bookRepository;
	@Autowired AccountRepository accountRepository;
	@Autowired BookstoreService bookstoreService;

	@BeforeEach
	public void setup() {
		var book1 = Book.create("Dummy Book One", BigDecimal.valueOf(23.39), 1983, "Dum Dum", "1111484227893", "Dum");
		var book2 = Book.create("Dummy Book Two", BigDecimal.valueOf(30.99), 1974, "Dim Dim", "1111484229999", "Dim");
		var book3 = Book.create("Dummy Book Three", BigDecimal.valueOf(50.99), 1942, "Dom Dom", "1111484228888", "Dim");

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

		bookRepository.saveAll(Flux.just(book1, book2, book3))
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
	void testFindBooksByCategory(){
		bookstoreService.findBooksByCategory("Dim").log().as(StepVerifier::create)
				.expectNextCount(2)
				.verifyComplete();
	}

	@Test
	void testFindRandomBooks(){
		bookstoreService.findRandomBooks().log().as(StepVerifier::create)
				.expectNextCount(2)
				.verifyComplete();
	}

	@Test
	void testFindBooksNone(){
		bookstoreService.findBooks(new BookSearchCriteria()).log().as(StepVerifier::create)
				.expectNextCount(0)
				.verifyComplete();
	}

	@Test
	void testFindBookByIsbn() {
		bookstoreService.findBookByIsbn("1111484228888").log().as(StepVerifier::create)
				.expectNextCount(1)
				.verifyComplete();
	}

	@Test
	void testFindNoBookByIsbn() {
		bookstoreService.findBookByIsbn("1111484221111").log().as(StepVerifier::create)
				.expectNextCount(0)
				.verifyComplete();
	}

	@Test
	void testQueryForBooks(){
		BookSearchCriteria criteria = new BookSearchCriteria();
		criteria.setCategory("Dim");
		bookstoreService.findBooks(criteria).log().as(StepVerifier::create)
				.expectNextCount(2)
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
