package com.project.backend.exception;


	public class NotFoundOrderIdException extends RuntimeException{
		public NotFoundOrderIdException(Long id) {
			super("Not found Order with id "+id);
		}
	}


