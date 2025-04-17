package com.gbInc.bazar.controller;

import com.gbInc.bazar.exception.venta.VentaException;
import com.gbInc.bazar.exception.venta.VentaExceptionCodigos;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class controllerAdvice {

	@ExceptionHandler(value = VentaException.class)
	public ResponseEntity<String> runtimeExceptionHandler(
	VentaException ex){
		
		return new ResponseEntity<>(ex.getMessage(),ex.getCodigo());
	}
	
	
}
