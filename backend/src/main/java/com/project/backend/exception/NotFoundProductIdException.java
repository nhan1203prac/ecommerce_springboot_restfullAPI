package com.project.backend.exception;

public class NotFoundProductIdException extends RuntimeException{
	public NotFoundProductIdException(Long id) {
		super("Not found product with id "+id);
	}
}
