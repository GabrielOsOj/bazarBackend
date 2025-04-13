package com.gbInc.bazar.mappers;

import com.gbInc.bazar.persistence.models.Producto;
import com.gbInc.bazar.DTO.DTOproducto;

public class ProductoMapper {

	public static Producto aProducto(DTOproducto productoDTO){
	
		Producto producto = Producto.builder()
				.codigo_producto(productoDTO.getCodigo_producto())
				.nombre(productoDTO.getNombre())
				.marca(productoDTO.getMarca())
				.costo(productoDTO.getCosto())
				.cantidad_disponible(productoDTO.getCantidad_disponible())
				.build();
		
		return producto;
	}
	
	public static DTOproducto aDTO(Producto producto){
		DTOproducto dtoProducto = DTOproducto.builder()
				.codigo_producto(producto.getCodigo_producto())
				.nombre(producto.getNombre())
				.marca(producto.getMarca())
				.costo(producto.getCosto())
				.cantidad_disponible(producto.getCantidad_disponible())
				.build();
		
		return dtoProducto;
	}
}
