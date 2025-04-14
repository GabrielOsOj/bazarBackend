package com.gbInc.bazar.persistence.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import java.time.LocalDate;
import java.util.List;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Venta {

	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long codigo_venta;
	private LocalDate fecha_venta;
	private Double total;
	
	@OneToMany
	private List<Producto> listaProductos;
	
	@OneToOne
	@JoinColumn(name = "cliente",referencedColumnName = "id_cliente")
	private Cliente cliente;

	
}
