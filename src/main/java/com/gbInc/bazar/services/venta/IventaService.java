package com.gbInc.bazar.services.venta;

import com.gbInc.bazar.DTO.DTOclienteMayorCompra;
import com.gbInc.bazar.DTO.DTOproducto;
import com.gbInc.bazar.DTO.DTOventa;
import com.gbInc.bazar.DTO.DTOventaYmontoDia;
import java.time.LocalDate;
import java.util.List;



public interface IventaService {
	
	public void crearVenta(DTOventa venta);
	public List<DTOventa> traerVentas();
	public DTOventa traerVenta(Long idVenta);
	public void eliminarVenta(Long idVenta);
	public void editarVenta(DTOventa ventaEditada);
	
	public List<DTOproducto> listaDeProductos(Long idVenta);
	public DTOclienteMayorCompra traerClienteMayorCompra();
	public DTOventaYmontoDia traerVentasSegunFecha(LocalDate fecha);
	
}
