
package com.lockinDev.reactive.bookstore;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.Duration;


@Service
public class BookServiceImpl implements  BookService {
	private static Logger logger = LoggerFactory.getLogger(BookServiceImpl.class);

	private final BookRepository bookRepository;

	public BookServiceImpl(BookRepository bookRepository) {
		this.bookRepository = bookRepository;
	}

	@Override
	public Mono<Book> findByIsbn(String isbn) {
		return bookRepository.findByIsbn(isbn);
	}

	@Override
	public Flux<Book> findAll() {
		return bookRepository.findAll();
				//.zipWith(Flux.interval(Duration.ofSeconds(2))).map(Tuple2::getT1);
	}

	@Override
	public Mono<Book> save(Book book) {
		return bookRepository.save(book);
	}

	@Override
	public Mono<Void> update(String id, Mono<Book> bookMono) {
		return bookRepository.findById(id).doOnNext(original -> bookMono.doOnNext( updatedBook -> {
			original.setIsbn(updatedBook.getIsbn());
			original.setAuthor(updatedBook.getAuthor());
			original.setTitle(updatedBook.getTitle());
			original.setCategory(updatedBook.getCategory());
			bookRepository.save(original);
		})
		).then(Mono.empty());
	}

	@Override
	public Mono<Void> delete(String id) {
		return bookRepository.deleteById(id);
	}
}
