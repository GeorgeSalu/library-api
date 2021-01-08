package com.library.exception;

public class BusinessException extends RuntimeException{

	private static final long serialVersionUID = 6827405651945003400L;

	public BusinessException(String s) {
		super(s);
	}
}
