
package com.lockinDev.reactive.bookstore.util;

/**
 * Created by lockinDev on 27/07/2020
 */
public class BookSearchCriteria {

	private String title;
	private String category;

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setCategory(String category) {
		this.category = category;
	}

	public String getCategory() {
		return this.category;
	}

	public boolean isEmpty(){
		return title == null && category == null;
	}

	@Override
	public String toString() {
		return "BookSearchCriteria{" +
				"title='" + title + '\'' +
				", category='" + category + '\'' +
				'}';
	}
}
