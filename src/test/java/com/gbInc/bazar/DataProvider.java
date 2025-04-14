package com.gbInc.bazar;

import com.gbInc.bazar.DTO.DTOcliente;
import com.gbInc.bazar.DTO.DTOproducto;
import com.gbInc.bazar.persistence.models.Cliente;
import com.gbInc.bazar.persistence.models.Producto;
import java.util.List;

/**
 * @author cuent
 */
public class DataProvider {

	public static List<Cliente> getListaClientes() {
		return List.of(new Cliente(1L, "gustavo", "gonzales", "23111025"),
				new Cliente(2L, "franco", "peralta", "4658891"), new Cliente(3L, "mirna", "gustamante", "78999875"),
				new Cliente(4L, "mariela", "melgarejo", "4255618"), new Cliente(5L, "facundo", "senini", "8785541"));
	}

	public static List<Cliente> getListaClientesVacia() {
		return List.of();
	}

	public static Cliente getClienteUno() {
		return new Cliente(1L, "gustavo", "gonzales", "23111025");
	}

	public static DTOcliente getNuevoClienteDTO() {
		return new DTOcliente(null, "cristian", "cisneros", "10444745");
	}

	public static Cliente getNuevoCliente() {
		return new Cliente(null, "cristian", "cisneros", "10444745");
	}

	public static DTOcliente getNuevoEditarCliente() {
		return new DTOcliente(1L, "gustavo", "gonzales", "23111025");
	}

	public static DTOcliente getNuevoEditarClienteError() {
		return new DTOcliente(800L, "gustavo", "gonzales", "23111025");
	}

	// ---------------- datos para productos ----------------
	public static List<Producto> getListaProductos() {

		return List.of(new Producto(1L, "Vaso plástico 350cc", "Colombraro", 1000D, 10D),
				new Producto(2L, "Plato hondo cerámica", "Cerámicas del Sur", 2500D, 20D),
				new Producto(3L, "Taza de vidrio", "Vidrios Argentinos", 1800D, 15D),
				new Producto(4L, "Cubiertos acero inox", "AceroPlus", 3200D, 50D),
				new Producto(5L, "Jarra medidora 1L", "Plásticos Norte", 1200D, 25D),
				new Producto(6L, "Fuente para horno", "Cerámica y Fuego", 4500D, 8D));
	}
	
	public static List<Producto> getListaProductosVacia(){
		return List.of();
	}

	public static Producto getProductoUno(){
		return getListaProductos().get(0);
	}
	
	public static DTOproducto getProductoEditado(){
		return new DTOproducto(5L, "Jarra medidora 1L", "Colombrado", 1200D, 25D);
	}
	
	public static DTOproducto getProductoEditadoError(){
		return new DTOproducto(99L, "Jarra medidora 1L", "Colombrado", 1200D, 25D);
	}
	
	public static DTOproducto getNuevoProducto(){
		return new DTOproducto(null, "Fuente para horno", "Cerámica y Fuego", 4500D, 8D);
	}
}
