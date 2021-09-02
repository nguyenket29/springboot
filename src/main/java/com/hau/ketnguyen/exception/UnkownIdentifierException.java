package com.hau.ketnguyen.exception;

public class UnkownIdentifierException extends Exception {
	private static final long serialVersionUID = 1L;

	public UnkownIdentifierException() {
		super();
	}

	public UnkownIdentifierException(String message) {
		super(message);
	}

	public UnkownIdentifierException(String message, Throwable cause) {
		super(message, cause);
	}
}
