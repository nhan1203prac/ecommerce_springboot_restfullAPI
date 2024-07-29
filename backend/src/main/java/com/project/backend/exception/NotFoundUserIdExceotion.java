package com.project.backend.exception;

public class NotFoundUserIdExceotion extends RuntimeException{
	public NotFoundUserIdExceotion(Long id) {
		super("Not found user with id "+id);
	}
}
