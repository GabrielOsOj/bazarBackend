package com.gbInc.bazar.mappers;

import com.gbInc.bazar.DTO.DTOcliente;
import com.gbInc.bazar.persistence.models.Cliente;

public class ClienteMapper {

	public static DTOcliente aDTO(Cliente cliente){
		
		DTOcliente clienteDTO = DTOcliente.builder()
				.id_cliente(cliente.getId_cliente())
				.nombre(cliente.getNombre())
				.apellido(cliente.getApellido())
				.dni(cliente.getDni())
				.build();
		
		return clienteDTO;
	}
	
	public static Cliente aCliente(DTOcliente clienteDTO){
		Cliente cli = Cliente.builder()
				.id_cliente(clienteDTO.getId_cliente())
				.nombre(clienteDTO.getNombre())
				.apellido(clienteDTO.getApellido())
				.dni(clienteDTO.getDni())
				.build();
		return cli;
	}
}
