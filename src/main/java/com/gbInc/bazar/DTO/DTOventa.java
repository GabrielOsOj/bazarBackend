package com.gbInc.bazar.DTO;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.gbInc.bazar.persistence.models.Cliente;
import com.gbInc.bazar.persistence.models.Producto;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DTOventa {
	
	private Long codigo_venta;
	@JsonFormat(pattern="yyyy-MM-dd")
	private LocalDate fecha_venta;
	private Double total;
	
	private List<DTOproducto> listaProductos;
	private DTOcliente cliente;
	
}
