package com.gbInc.bazar.DTO;

import com.gbInc.bazar.persistence.models.Cliente;
import com.gbInc.bazar.persistence.models.Producto;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DTOventa {
	
	private Long codigo_venta;
	private LocalDate fecha_venta;
	private Double total;
	
	private List<Producto> listaProductos;
	private Cliente cliente;
	
}
