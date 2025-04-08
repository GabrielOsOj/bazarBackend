package com.gbInc.bazar.DTO;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Builder
public class DTOcliente {

	private final Long id_cliente;
	private final String nombre;
	private final String apellido;
	private final String dni;

	
	public DTOcliente(){
	}

	public DTOcliente(Long id_cliente, String nombre, String apellido, String dni) {
		this.id_cliente = id_cliente;
		this.nombre = nombre;
		this.apellido = apellido;
		this.dni = dni;
	}
	
	
	
}
