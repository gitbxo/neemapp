package org.neem.neemapp.api;

public class UserNotFoundException extends RuntimeException {

	private static final long serialVersionUID = 101L;

	UserNotFoundException(Long id) {
		super("Could not find user " + id);
	}
}
