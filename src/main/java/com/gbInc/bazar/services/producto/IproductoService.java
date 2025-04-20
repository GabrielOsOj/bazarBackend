
package com.gbInc.bazar.services.producto;

import com.gbInc.bazar.DTO.DTOproducto;
import java.util.List;
import com.gbInc.bazar.persistence.models.Producto;

public interface IproductoService {

	public List<DTOproducto> traerProductos();
	public DTOproducto traerProducto(Long id);
	public void eliminarProducto(Long id);
	public void editarProducto(DTOproducto productoDTO);
	public void crearProducto(DTOproducto productoDTO);
	
	public Producto traerEntidadProducto(Long id);
	public Boolean validarProductos(List<Producto> productos);
}
