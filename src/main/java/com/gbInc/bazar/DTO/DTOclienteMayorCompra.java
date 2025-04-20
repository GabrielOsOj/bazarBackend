package com.gbInc.bazar.DTO;

import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class DTOclienteMayorCompra {

	private Long codigo_venta;
	private Double total;
	private Integer cantidadProductos;
	private String nombreCliente;
	private String apellidoCliente;
	
}
