package com.gbInc.bazar.services.producto;

import com.gbInc.bazar.DTO.DTOproducto;
import com.gbInc.bazar.exception.CodigosExcepcion;
import com.gbInc.bazar.exception.producto.ProductoException;
import com.gbInc.bazar.mappers.ProductoMapper;
import com.gbInc.bazar.persistence.models.Producto;
import com.gbInc.bazar.persistence.repository.IproductoRepository;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class ProductoService implements IproductoService {

	IproductoRepository productoRepo;

	@Autowired
	public ProductoService(IproductoRepository productoRepo) {
		this.productoRepo = productoRepo;
	}

	@Override
	public List<DTOproducto> traerProductos() {

		return this.productoRepo.findAll().stream().map(producto -> {
			return ProductoMapper.aDTO(producto);
		}).collect(Collectors.toList());
	}

	@Override
	public DTOproducto traerProducto(Long id) {

		return ProductoMapper.aDTO(this.traerEntidadProducto(id));

	}

	@Override
	public void eliminarProducto(Long id) {

		if (!this.validarProducto(id)) {
			throw new ProductoException(HttpStatus.BAD_REQUEST, CodigosExcepcion.BE200);
		}
		this.productoRepo.deleteById(id);

	}

	@Override
	public void editarProducto(DTOproducto productoDTO) {

		if (!this.validarProducto(productoDTO.getCodigo_producto())) {
			throw new ProductoException(HttpStatus.BAD_REQUEST, CodigosExcepcion.BE200);
		}
		this.productoRepo.save(ProductoMapper.aProducto(productoDTO));

	}

	@Override
	public void crearProducto(DTOproducto productoDTO) {

		productoDTO.setCodigo_producto(null);
		this.productoRepo.save(ProductoMapper.aProducto(productoDTO));

	}

	@Override
	public Producto traerEntidadProducto(Long id) {

		if (!this.validarProducto(id)) {
			throw new ProductoException(HttpStatus.NOT_FOUND, CodigosExcepcion.BE200);
		}
		return this.productoRepo.findById(id).get();

	}

	@Override
	public Boolean validarProductos(List<Producto> productos) {

		for (Producto p : productos) {
			if (!this.productoRepo.existsById(p.getCodigo_producto())) {
				return false;
			}
		}

		return true;

	}

	private Boolean validarProducto(Long idProducto) {
		return this.productoRepo.existsById(idProducto);
	}

}
