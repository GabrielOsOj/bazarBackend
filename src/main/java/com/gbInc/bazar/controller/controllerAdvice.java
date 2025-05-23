package com.gbInc.bazar.controller;

import com.gbInc.bazar.exception.venta.VentaException;
import com.gbInc.bazar.exception.cliente.ClienteException;
import com.gbInc.bazar.exception.producto.ProductoException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class controllerAdvice {

	@ExceptionHandler(value = VentaException.class)
	public ResponseEntity<String> VentaExceptionHandler(VentaException ex) {

		return new ResponseEntity<>(ex.getMessage(), ex.getCodigo());
	}

	@ExceptionHandler(value = ClienteException.class)
	public ResponseEntity<String> ClienteExceptionHandler(ClienteException ex) {

		return new ResponseEntity<>(ex.getMessage(), ex.getCodigo());
	}

	@ExceptionHandler(value = ProductoException.class)
	public ResponseEntity<String> ProductoExceptionHandler(ProductoException ex) {

		return new ResponseEntity<>(ex.getMessage(), ex.getCodigo());
	}

}