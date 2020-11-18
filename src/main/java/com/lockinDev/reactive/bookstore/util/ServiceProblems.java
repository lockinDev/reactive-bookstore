
package com.lockinDev.reactive.bookstore.util;

/**
 * Created by lockinDev on 29/07/2020
 */
@SuppressWarnings("serial")
public class ServiceProblems {

	public static class ServiceDownException extends  RuntimeException {
		public ServiceDownException(String message) {
			super(message);
		}
	}

	public static class ServiceDeniedException extends  RuntimeException {
		public ServiceDeniedException(String message) {
			super(message);
		}
	}
}
