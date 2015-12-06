package org.mobile.gqz.exception;

public class FileException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public FileException() {
	}

	public FileException(String detailMessage) {
		super(detailMessage);
	}

	public FileException(String detailMessage, Throwable throwable) {
		super(detailMessage, throwable);
	}

	public FileException(Throwable throwable) {
		super(throwable);
	}
}
