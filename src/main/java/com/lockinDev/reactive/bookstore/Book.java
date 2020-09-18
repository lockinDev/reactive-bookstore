
package com.lockinDev.reactive.bookstore;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Objects;


@Document(collection="book")
public class Book {
	@Id
	private String id;

	private String title;
	private String author;

	@Indexed(unique = true)
	private String isbn;

	public Book() {
	}

	// Just limit to {"Spring", "Java", "Web"}
	//just embed here
	private String category;

	public Book(String title, String author, String isbn, String category) {
		this.title = title;
		this.author = author;
		this.isbn = isbn;
		this.category = category;
	}

	@Override
	public String toString() {
		return "Book{" +
				"id='" + id + '\'' +
				", title='" + title + '\'' +
				", author='" + author + '\'' +
				", isbn='" + isbn + '\'' +
				", category='" + category + '\'' +
				'}';
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthor() {
		return author;
	}

	public void setAuthor(String author) {
		this.author = author;
	}

	public String getIsbn() {
		return isbn;
	}

	public void setIsbn(String isbn) {
		this.isbn = isbn;
	}

	public String getCategory() {
		return category;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Book book = (Book) o;
		return Objects.equals(id, book.id) &&
				Objects.equals(title, book.title) &&
				Objects.equals(author, book.author) &&
				Objects.equals(isbn, book.isbn) &&
				Objects.equals(category, book.category);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, author, isbn, category);
	}
}
