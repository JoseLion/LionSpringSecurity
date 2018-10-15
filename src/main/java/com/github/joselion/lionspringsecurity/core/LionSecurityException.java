package com.github.joselion.lionspringsecurity.core;

public class LionSecurityException extends Exception {

	private static final long serialVersionUID = 6441322160404553394L;

	public LionSecurityException() {
		super();
	}

	public LionSecurityException(
		String message,
		Throwable cause,
		boolean enableSuppression,
		boolean writableStackTrace
	) {
		super(message, cause, enableSuppression, writableStackTrace);
	}

	public LionSecurityException(String message, Throwable cause) {
		super(message, cause);
	}

	public LionSecurityException(String message) {
		super(message);
	}

	public LionSecurityException(Throwable cause) {
		super(cause);
	}
	
}
