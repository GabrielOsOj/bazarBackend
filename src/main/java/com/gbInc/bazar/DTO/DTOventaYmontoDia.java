package com.gbInc.bazar.DTO;

import java.time.LocalDate;
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
public class DTOventaYmontoDia {
	
	private LocalDate fecha;
	private Long cantidadVentasDia;
	private Double montoDelDia;
	
}
