package com.gbInc.bazar.persistence.models;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author cuent
 */
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Cliente {
		
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE)
	private Long id_cliente;
	private String nombre;
	private String apellido;
	private String dni;
	
}
