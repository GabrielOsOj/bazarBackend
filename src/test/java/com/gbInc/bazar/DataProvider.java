package com.gbInc.bazar;

import com.gbInc.bazar.DTO.DTOcliente;
import com.gbInc.bazar.persistence.models.Cliente;
import java.util.List;

/**
 * @author cuent
 */
public class DataProvider {

	public static List<Cliente> getListaClientes(){
		return List.of(
				new Cliente(1L,"gustavo","gonzales","23111025"),
				new Cliente(2L,"franco","peralta","4658891"),
				new Cliente(3L,"mirna","gustamante","78999875"),
				new Cliente(4L,"mariela","melgarejo","4255618"),
				new Cliente(5L,"facundo","senini","8785541"));
	}

	public static List<Cliente> getListaClientesVacia(){
		return List.of();
	}
	
	public static Cliente getClienteUno(){
		return new Cliente(1L,"gustavo","gonzales","23111025");
	}
	
	public static DTOcliente getNuevoClienteDTO(){
		return new DTOcliente(null,"cristian","cisneros","10444745");
	}
	
	public static Cliente getNuevoCliente(){
		return new Cliente(null,"cristian","cisneros","10444745");
	}
	
	public static DTOcliente getNuevoEditarCliente(){
		return new DTOcliente(1L,"gustavo","gonzales","23111025");
	}
	
	public static DTOcliente getNuevoEditarClienteError(){
		return new DTOcliente(800L,"gustavo","gonzales","23111025");
	}
}
