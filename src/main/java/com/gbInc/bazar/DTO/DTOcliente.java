package com.gbInc.bazar.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DTOcliente {

	private Long id_cliente;
	private String nombre;
	private String apellido;
	private String dni;

}