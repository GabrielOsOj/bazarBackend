package com.gbInc.bazar.exception.venta;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class VentaException extends RuntimeException{
	
	private HttpStatus codigo;
	
	public VentaException(HttpStatus codigo,String message) {
		super(message);
		this.codigo = codigo;
	}
		
}
