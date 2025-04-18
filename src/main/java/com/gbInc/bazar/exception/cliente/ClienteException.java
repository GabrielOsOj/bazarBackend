package com.gbInc.bazar.exception.cliente;

import lombok.Getter;
import org.springframework.http.HttpStatus;


@Getter
public class ClienteException extends RuntimeException{

	private HttpStatus codigo;

	public ClienteException(HttpStatus codigo, String message) {
		super(message);
		this.codigo = codigo;
	}

	
	
	
}
