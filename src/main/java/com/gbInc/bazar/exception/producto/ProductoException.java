package com.gbInc.bazar.exception.producto;

import lombok.Getter;
import org.springframework.http.HttpStatus;

/**
 * @author cuent
 */
@Getter
public class ProductoException extends RuntimeException{

	private HttpStatus codigo;

	public ProductoException(HttpStatus codigo, String message) {
		super(message);
		this.codigo = codigo;
	}

	
	
}
