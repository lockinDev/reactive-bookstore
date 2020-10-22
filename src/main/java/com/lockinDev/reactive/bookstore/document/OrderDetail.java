package com.lockinDev.reactive.bookstore.document;

import org.springframework.data.annotation.Transient;

import java.io.Serializable;
import java.math.BigDecimal;


@SuppressWarnings("serial")
public class OrderDetail implements Serializable {

    private String bookId;

    @Transient private Book book;

    private int quantity = 1;

    public OrderDetail() {
        super();
    }

    public OrderDetail(Book book, int quantity) {
        super();
        this.bookId = book.getId();
        this.book = book;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return this.quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getBookId() {
        return this.bookId;
    }

    public void setBook(String bookId) {
        this.bookId = bookId;
    }

    public BigDecimal getPrice() {
        if (this.book != null) {
            return this.book.getPrice().multiply(new BigDecimal(this.quantity));
        }
        return BigDecimal.ZERO;
    }

	@Override
	public String toString() {
		return "OrderDetail{" +
				"bookId='" + bookId + '\'' +
				", quantity=" + quantity +
				'}';
	}
}