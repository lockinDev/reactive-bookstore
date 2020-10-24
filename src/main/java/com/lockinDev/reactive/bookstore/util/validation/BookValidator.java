
package com.lockinDev.reactive.bookstore.util.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.lockinDev.reactive.bookstore.document.Book;


public class BookValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return (Book.class).isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		ValidationUtils.rejectIfEmpty(errors, "title", "required", new Object[] { "Title" });
		ValidationUtils.rejectIfEmpty(errors, "author", "required", new Object[] { "Author" });
		ValidationUtils.rejectIfEmpty(errors, "isbn", "required", new Object[] { "Isbn" });
		ValidationUtils.rejectIfEmpty(errors, "category", "required", new Object[] { "Category" });
	}
}
