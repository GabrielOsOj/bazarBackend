package com.gbInc.bazar.services.venta;

import com.gbInc.bazar.DTO.DTOventa;
import java.util.List;



public interface IventaService {
	
	public Boolean crearVenta(DTOventa venta);
	public List<DTOventa> traerVentas();
	public DTOventa traerVenta(Long idVenta);
	public Boolean eliminarVenta(Long idVenta);
	public Boolean editarVenta(DTOventa ventaEditada);
	
}
