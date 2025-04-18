package com.gbInc.bazar.services.cliente;

import com.gbInc.bazar.DTO.DTOcliente;
import com.gbInc.bazar.persistence.models.Cliente;
import java.util.List;
import org.springframework.stereotype.Service;

public interface IclienteService {
	
	public List<DTOcliente> traerClientes();
	
	public void editarCliente(DTOcliente cliente);
	
	public DTOcliente traerCliente(Long id);
	
	public void crearCliente(DTOcliente cliente);
	
	public void eliminarCliente(Long id);
	
	public Cliente traerEntidadCliente(Long id);
	
}
