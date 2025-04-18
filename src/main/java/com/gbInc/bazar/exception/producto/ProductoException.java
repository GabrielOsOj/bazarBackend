package com.gbInc.bazar.exception.producto;

import org.springframework.http.HttpStatus;

/**
 * @author cuent
 */
public class ProductoException extends RuntimeException{

	private HttpStatus codigo;

	public ProductoException(HttpStatus codigo, String message) {
		super(message);
		this.codigo = codigo;
	}

	
	
}
