package com.gbInc.bazar.mappers;

import com.gbInc.bazar.DTO.DTOventa;
import com.gbInc.bazar.persistence.models.Venta;

public class VentaMapper {

	public static DTOventa aDTO(Venta venta){
		
		DTOventa dtoVenta = DTOventa.builder()
				.codigo_venta(venta.getCodigo_venta())
				.fecha_venta(venta.getFecha_venta())
				.total(venta.getTotal())
				.listaProductos(venta.getListaProductos())
				.cliente(venta.getCliente())
				.build();
	
		return dtoVenta;
	}
	
	public static Venta aVenta(DTOventa dtoVenta){
		
	Venta venta = Venta.builder()
				.codigo_venta(dtoVenta.getCodigo_venta())
				.fecha_venta(dtoVenta.getFecha_venta())
				.total(dtoVenta.getTotal())
				.listaProductos(dtoVenta.getListaProductos())
				.cliente(dtoVenta.getCliente())
				.build();
	
		return venta;
		
	}
	
}
