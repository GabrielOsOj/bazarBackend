package com.gbInc.bazar.services.producto;

import com.gbInc.bazar.DTO.DTOproducto;
import com.gbInc.bazar.exception.CodigosExcepcion;
import com.gbInc.bazar.exception.producto.ProductoException;
import com.gbInc.bazar.mappers.ProductoMapper;
import com.gbInc.bazar.persistence.models.Producto;
import com.gbInc.bazar.persistence.repository.IproductoRepository;
import java.util.List;
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

		this.productoExiste(id);
		this.productoRepo.deleteById(id);

	}

	@Override
	public void editarProducto(DTOproducto productoDTO) {

		this.productoExiste(productoDTO.getCodigo_producto());
		this.validarCampos(productoDTO);
		this.productoRepo.save(ProductoMapper.aProducto(productoDTO));

	}

	@Override
	public void crearProducto(DTOproducto productoDTO) {

		productoDTO.setCodigo_producto(null);
		this.validarCampos(productoDTO);
		this.productoRepo.save(ProductoMapper.aProducto(productoDTO));

	}

	@Override
	public Producto traerEntidadProducto(Long id) {

		this.productoExiste(id);
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

	private void productoExiste(Long idProducto) {
		if (!this.productoRepo.existsById(idProducto)) {
			throw new ProductoException(HttpStatus.NOT_FOUND, CodigosExcepcion.BE200);
		}
	}

	@Override
	public List<DTOproducto> faltaStock() {
		return this.productoRepo.faltaStock().stream().map(p -> ProductoMapper.aDTO(p)).collect(Collectors.toList());
	}

	private void validarCampos(DTOproducto producto) {

		if (producto.getNombre() == null || producto.getNombre().isBlank()) {
			throw new ProductoException(HttpStatus.BAD_REQUEST, CodigosExcepcion.BE203);
		}

		if (producto.getMarca() == null||producto.getMarca().isBlank()) {
			throw new ProductoException(HttpStatus.BAD_REQUEST, CodigosExcepcion.BE204);
		}
	}

	private void validarStock(Double cantidadDisponible, Double cantidadComprada, String nombre) {
		if (cantidadDisponible < cantidadComprada) {
			throw new ProductoException(HttpStatus.BAD_REQUEST, CodigosExcepcion.BE201 + ": " + nombre);
		}

	}

	@Override
	public void actualizarStock(List<DTOproducto> productos) {

		List<Producto> productosValidos = productos.stream().map(p -> {
			DTOproducto dtoP = this.traerProducto(p.getCodigo_producto());
			this.validarStock(dtoP.getCantidad_disponible(), p.getCantidad_comprada(), dtoP.getNombre());
			dtoP.setCantidad_comprada(p.getCantidad_comprada());
			return dtoP;
		}).map(p -> {
			p.setCantidad_disponible(p.getCantidad_disponible() - p.getCantidad_comprada());
			return ProductoMapper.aProducto(p);
		}).collect(Collectors.toList());

		this.productoRepo.saveAll(productosValidos);
	}

}