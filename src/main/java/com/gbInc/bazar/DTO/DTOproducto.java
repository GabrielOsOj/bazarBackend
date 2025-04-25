package com.gbInc.bazar.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@NoArgsConstructor
public class DTOproducto {
	
	private Long codigo_producto;
	private String nombre;
	private String marca;
	private Double costo;
	private Double cantidad_disponible;
	private Double cantidad_comprada;

	public DTOproducto(Long codigo_producto, String nombre, String marca, Double costo, Double cantidad_disponible, Double cantidad_comprada) {
		this.codigo_producto = codigo_producto;
		this.nombre = nombre;
		this.marca = marca;
		this.costo = costo;
		this.cantidad_disponible = cantidad_disponible;
		this.cantidad_comprada = cantidad_comprada;
	}

	public DTOproducto(Long codigo_producto, String nombre, String marca, Double costo, Double cantidad_disponible) {
		this.codigo_producto = codigo_producto;
		this.nombre = nombre;
		this.marca = marca;
		this.costo = costo;
		this.cantidad_disponible = cantidad_disponible;
	}
}
