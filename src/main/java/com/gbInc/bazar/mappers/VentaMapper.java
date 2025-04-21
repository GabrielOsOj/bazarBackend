package com.gbInc.bazar.mappers;

import com.gbInc.bazar.DTO.DTOproducto;
import com.gbInc.bazar.DTO.DTOventa;
import com.gbInc.bazar.persistence.models.Producto;
import com.gbInc.bazar.persistence.models.Venta;
import java.util.List;
import java.util.stream.Collectors;

public class VentaMapper {

	public static DTOventa aDTO(Venta venta) {

		DTOventa dtoVenta = DTOventa.builder()
			.codigo_venta(venta.getCodigo_venta())
			.fecha_venta(venta.getFecha_venta())
			.total(venta.getTotal())
			.listaProductos(aProductosDTO(venta.getListaProductos()))
			.cliente(ClienteMapper.aDTO(venta.getCliente()))
			.build();

		return dtoVenta;
	}

	public static Venta aVenta(DTOventa dtoVenta) {

		Venta venta = Venta.builder()
			.codigo_venta(dtoVenta.getCodigo_venta())
			.fecha_venta(dtoVenta.getFecha_venta())
			.total(dtoVenta.getTotal())
			.listaProductos(aProducto(dtoVenta.getListaProductos()))
			.cliente(ClienteMapper.aCliente(dtoVenta.getCliente()))
			.build();

		return venta;

	}

	private static List<DTOproducto> aProductosDTO(List<Producto> productos) {
		return productos.stream().map(p -> ProductoMapper.aDTO(p)).collect(Collectors.toList());
	}

	private static List<Producto> aProducto(List<DTOproducto> productos) {

		return productos.stream().map(p -> ProductoMapper.aProducto(p)).collect(Collectors.toList());
	}

}
