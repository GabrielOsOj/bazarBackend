
package com.gbInc.bazar.services.producto;

import com.gbInc.bazar.DTO.DTOproducto;
import java.util.List;
import com.gbInc.bazar.persistence.models.Producto;

public interface IproductoService {

	public List<DTOproducto> traerProductos();
	public DTOproducto traerProducto(Long id);
	public Boolean eliminarProducto(Long id);
	public Boolean editarProducto(DTOproducto productoDTO);
	public Boolean crearProducto(DTOproducto productoDTO);
	
	public Producto traerEntidadProducto(Long id);
			
}
