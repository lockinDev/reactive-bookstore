
package com.lockinDev.reactive.bookstore.util;

import java.math.BigDecimal;
import java.util.List;
import java.util.Random;

import com.lockinDev.reactive.bookstore.document.Book;

/**
 * Created by lockinDev on 29/07/2020
 */
public class BookNewReleasesUtil {

	private static final Random RANDOM = new Random(System.currentTimeMillis());

	public static final List<Book> NEW_BOOKS = List.of(
			new Book("Spring Boot 3 Recipes", BigDecimal.valueOf(38.44), 2022,"Lockin", "9781484227899", Book.Category.SPRING),
			new Book("Spring WebFlux for Dummies", BigDecimal.valueOf(39.44), 2021,"lockinDev", "9781484227888", Book.Category.SPRING),
			new Book("Reactive Java Recipes", BigDecimal.valueOf(49.44), 2022,"lockinDev", "97814842278944", Book.Category.JAVA),
			new Book("JavaScript for the Backend Developer", BigDecimal.valueOf(51.44), 2020,"James Crook", "9781484227822", Book.Category.WEB),
			new Book("Pro Spring 6", BigDecimal.valueOf(59.44), 2022,"lockinDev", "9781484227893", Book.Category.SPRING),
			new Book("Reactive Spring", BigDecimal.valueOf(25.44), 2020,"Josh Long", "9781484227111", Book.Category.SPRING),
			new Book("Spring MVC and WebFlux", BigDecimal.valueOf(50.44), 2020,"Lockin & lockinDev", "9781484227222", Book.Category.WEB)
	);

	public static Book randomRelease() {
		return NEW_BOOKS.get(RANDOM.nextInt(NEW_BOOKS.size()));
	}

	public static final List<String> TECH_NEWS = List.of(
			"Apress merged with Springer.",
			"VMWare buys Pivotal for a ridiculous amount of money!",
			"Twitter was hacked!",
			"Amazon launches reactive API for DynamoDB.",
			"Java 17 will be released in September 2021.",
			"The JavaScript world is still 'The Wild Wild West'.",
			"Java modules, still a topic that developers frown upon."
			);

	public static String randomNews() {
		return TECH_NEWS.get(RANDOM.nextInt(TECH_NEWS.size()));
	}


}
