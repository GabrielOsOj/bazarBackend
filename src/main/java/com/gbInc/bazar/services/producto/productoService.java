package com.gbInc.bazar.services.producto;

import com.gbInc.bazar.DTO.DTOproducto;
import com.gbInc.bazar.mappers.ProductoMapper;
import com.gbInc.bazar.persistence.models.Producto;
import com.gbInc.bazar.persistence.repository.IproductoRepository;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ProductoService implements IproductoService{

	IproductoRepository productoRepo;

	@Autowired
	public ProductoService(IproductoRepository productoRepo) {
		this.productoRepo = productoRepo;
	}
	
	@Override
	public List<DTOproducto> traerProductos() {
		
		return this.productoRepo.findAll()
				.stream()
				.map(producto->{
					return ProductoMapper.aDTO(producto);})
				.collect(Collectors.toList());
	}

	@Override
	public DTOproducto traerProducto(Long id) {
		
		if(!this.productoRepo.existsById(id)){
			return null;
		}
		
		return ProductoMapper.aDTO(this.productoRepo.findById(id).get());
	
	}

	@Override
	public Boolean eliminarProducto(Long id) {
		
		if(!this.productoRepo.existsById(id)){
			return false;
		}
		
		this.productoRepo.deleteById(id);
		return true;
	}

	@Override
	public Boolean editarProducto(DTOproducto productoDTO) {
			
		if(!this.productoRepo.existsById(productoDTO.getCodigo_producto())){
			return false;
		}
		
		this.productoRepo
			.save(ProductoMapper.aProducto(productoDTO));
		return true;
	
	}

	@Override
	public Boolean crearProducto(DTOproducto productoDTO) {
		
		productoDTO.setCodigo_producto(null);
		this.productoRepo.save(ProductoMapper.aProducto(productoDTO));
		return true;
	
	}

}
