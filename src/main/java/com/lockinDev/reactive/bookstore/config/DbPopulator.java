
package com.lockinDev.reactive.bookstore.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import com.lockinDev.reactive.bookstore.document.Account;
import com.lockinDev.reactive.bookstore.document.Address;
import com.lockinDev.reactive.bookstore.document.Book;
import com.lockinDev.reactive.bookstore.repository.AccountRepository;
import com.lockinDev.reactive.bookstore.repository.BookRepository;

import static com.lockinDev.reactive.bookstore.document.Account.Authority;
import static com.lockinDev.reactive.bookstore.document.Book.Category;

import java.math.BigDecimal;
import java.util.List;


@Component
public class DbPopulator {

	private final Logger logger = LoggerFactory.getLogger(DbPopulator.class);

	//private final PasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

	private final BookRepository bookRepository;
	private final AccountRepository accountRepository;

	public DbPopulator(BookRepository bookRepository, AccountRepository accountRepository) {
		this.bookRepository = bookRepository;
		this.accountRepository = accountRepository;
	}

	@EventListener(ApplicationReadyEvent.class)
	public void init(){
		List<Book> books = List.of(
				Book.create("Clean Code", BigDecimal.valueOf(37.44), 2008,"Robert C. Martin", "9780132350884", Category.WEB),
				Book.create("Agile Software Development", BigDecimal.valueOf(54.99), 2002,"Robert C. Martin", "9780135974445", Category.WEB),
				Book.create("Refactoring", BigDecimal.valueOf(24.99), 2019,"Martin Fowler", "9780201485677", Category.JAVA),
				Book.create("Effective Java", BigDecimal.valueOf(54.99), 2017,"Joshua Bloch", "9780321356680", Category.JAVA)
		);

		bookRepository.deleteAll().thenMany(
				bookRepository.saveAll(books))
					.thenMany(bookRepository.findAll())
						.subscribe(
							data -> logger.info("found books: {}", bookRepository),
							error -> logger.error("" + error),
							() -> logger.info(" -->> done books initialization...")
						);

		Address address = new Address();

		address.setStreet("Liberty Street");
		address.setCity("of angels");
		address.setCountry("Europe");

		Account john = new Account();
		john.setFirstName("john");
		john.setUsername("john");
		john.setLastName("doe");
		john.setEmailAddress("john@doe.com");
		john.setPassword("doe");
		//john.setPassword(passwordEncoder.encode("doe"));
		john.setAddress(address);
		john.setRoles(List.of(Authority.ROLE_USER));

		Account jane = new Account();
		jane.setFirstName("jane");
		jane.setLastName("doe");
		jane.setUsername("jane");
		jane.setEmailAddress("jane@doe.com");
		jane.setPassword("doe");
		//jane.setPassword(passwordEncoder.encode("doe"));
		jane.setAddress(address);
		jane.setRoles(List.of(Authority.ROLE_USER, Authority.ROLE_ADMIN));

		Account admin = new Account();
		admin.setFirstName("admin");
		admin.setLastName("admin");
		admin.setUsername("admin");
		admin.setEmailAddress("admin@admin.com");
		admin.setPassword("admin");
		//admin.setPassword(passwordEncoder.encode("admin"));
		admin.setAddress(address);
		admin.setRoles(List.of(Authority.ROLE_ADMIN));

		accountRepository.deleteAll().thenMany(
				accountRepository.saveAll(List.of(john, jane, admin)))
					.thenMany(accountRepository.findAll())
						.subscribe(
							data -> logger.info("found accounts: {}", accountRepository),
							error -> logger.error("" + error),
							() -> logger.info(" -->> done accounts initialization...")
						);
	}
}

