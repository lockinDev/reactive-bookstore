
package com.lockinDev.reactive.bookstore;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.BaseSubscriber;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;

import static org.springframework.http.MediaType.APPLICATION_STREAM_JSON_VALUE;


@RestController
public class BookController {

	private final BookRepository bookRepository;

	public BookController(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/index")
	public Mono<String> index() {
		return Mono.just("It works");
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path="/books", produces = MediaType.APPLICATION_STREAM_JSON_VALUE)
	public Flux<Book> books() {
		Flux<Book> books = bookRepository.findAll();
		Flux<Long> periodFlux = Flux.interval(Duration.ofSeconds(2)); // slowing the stream down
		return books.zipWith(periodFlux).map(Tuple2::getT1);
	}

	@PutMapping
	@ResponseStatus(HttpStatus.CREATED)
	@PostMapping(value="/books")
	public Mono<Book> save(@RequestBody Book book){
		 return bookRepository.save(book);
	}

	@ResponseStatus(HttpStatus.OK)
	@GetMapping(path="books/{isbn}")
	public Mono<Book> show(@PathVariable String isbn) {
		return bookRepository.findByIsbn(isbn);
	}


}
