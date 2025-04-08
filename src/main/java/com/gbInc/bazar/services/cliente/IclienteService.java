package com.gbInc.bazar.services.cliente;

import com.gbInc.bazar.DTO.DTOcliente;
import java.util.List;
import org.springframework.stereotype.Service;

public interface IclienteService {
	
	public List<DTOcliente> traerClientes();
	
	public Boolean editarCliente(DTOcliente cliente);
	
	public DTOcliente traerCliente(Long id);
	
	public Boolean crearCliente(DTOcliente cliente);
	
	public Boolean eliminarCliente(Long id);
	
}
