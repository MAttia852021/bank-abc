package com.abcbank.accountservices.util.exception;

public class EntityNotFoundException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EntityNotFoundException(Long entityId, String className) {
		super(String.format("Entity %s ,with Id %s not found", className, entityId));
	}

}
