package com.lockinDev.reactive.bookstore.util.validation;

import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;

import com.lockinDev.reactive.bookstore.document.Address;
import com.lockinDev.reactive.bookstore.document.Order;

/**
 * Validates {@link Order} domain objects
 * 
 * @author Lockin

 * 
 */
public class OrderValidator implements Validator {

	@Override
	public boolean supports(Class<?> clazz) {
		return (Order.class).isAssignableFrom(clazz);
	}

	@Override
	public void validate(Object target, Errors errors) {
		var order = (Order) target;
		validateAddress(order.getShippingAddress(), errors, "shippingAddress");
		if (!order.isBillingSameAsShipping()) {
			validateAddress(order.getShippingAddress(), errors, "billingAddress");
		}

	}

	private void validateAddress(Address address, Errors errors, String type) {
		ValidationUtils.rejectIfEmpty(errors, type + ".street", "required", new Object[] { "Street" });
		ValidationUtils.rejectIfEmpty(errors, type + ".city", "required", new Object[] { "City" });
		ValidationUtils.rejectIfEmpty(errors, type + ".country", "required", new Object[] { "Country" });

	}

}
