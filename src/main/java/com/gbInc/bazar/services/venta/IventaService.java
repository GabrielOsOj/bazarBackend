package com.gbInc.bazar.services.venta;

import com.gbInc.bazar.DTO.DTOventa;
import java.util.List;



public interface IventaService {
	
	public void crearVenta(DTOventa venta);
	public List<DTOventa> traerVentas();
	public DTOventa traerVenta(Long idVenta);
	public Boolean eliminarVenta(Long idVenta);
	public void editarVenta(DTOventa ventaEditada);
	
}
