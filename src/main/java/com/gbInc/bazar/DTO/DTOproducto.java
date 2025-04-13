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
public class DTOproducto {
	
	private Long codigo_producto;
	private String nombre;
	private String marca;
	private Double costo;
	private Double cantidad_disponible;

}
