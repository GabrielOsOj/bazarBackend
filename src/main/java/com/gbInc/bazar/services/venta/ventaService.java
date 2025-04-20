package com.gbInc.bazar.services.venta;

import com.gbInc.bazar.DTO.DTOproducto;
import com.gbInc.bazar.DTO.DTOventa;
import com.gbInc.bazar.exception.venta.VentaException;
import com.gbInc.bazar.exception.CodigosExcepcion;
import com.gbInc.bazar.mappers.ProductoMapper;
import com.gbInc.bazar.mappers.VentaMapper;
import com.gbInc.bazar.persistence.models.Cliente;
import com.gbInc.bazar.persistence.models.Producto;
import com.gbInc.bazar.persistence.models.Venta;
import com.gbInc.bazar.persistence.repository.IventaRepository;
import com.gbInc.bazar.services.cliente.IclienteService;
import com.gbInc.bazar.services.producto.IproductoService;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class VentaService implements IventaService {

	IventaRepository ventaRepo;

	IclienteService clienteSv;

	IproductoService productoSv;

	@Autowired
	public VentaService(IventaRepository ventaRepo, IclienteService clienteSv, IproductoService productoSv) {
		this.ventaRepo = ventaRepo;
		this.clienteSv = clienteSv;
		this.productoSv = productoSv;
	}

	@Override
	public void crearVenta(DTOventa venta) {

		this.validaciones(venta.getCliente(), venta.getListaProductos());

		Cliente cli = this.clienteSv.traerEntidadCliente(venta.getCliente().getId_cliente());

		List<Producto> productos = venta.getListaProductos().stream().map(p -> {
			return this.productoSv.traerEntidadProducto(p.getCodigo_producto());
		}).collect(Collectors.toList());

		Venta nuevaVenta = Venta.builder()
			.codigo_venta(venta.getCodigo_venta())
			.fecha_venta(venta.getFecha_venta())
			.cliente(cli)
			.listaProductos(productos)
			.total(venta.getTotal())
			.build();

		this.ventaRepo.save(nuevaVenta);
	}

	@Override
	public List<DTOventa> traerVentas() {

		return this.ventaRepo.findAll().stream().map(venta -> {
			return VentaMapper.aDTO(venta);
		}).collect(Collectors.toList());

	}

	@Override
	public DTOventa traerVenta(Long idVenta) {

		this.ventaExiste(idVenta);
		return VentaMapper.aDTO(this.ventaRepo.findById(idVenta).get());

	}

	@Override
	public void eliminarVenta(Long idVenta) {

		this.ventaExiste(idVenta);
		this.ventaRepo.deleteById(idVenta);

	}

	@Override
	public void editarVenta(DTOventa ventaEditada) {

		this.ventaExiste(ventaEditada.getCodigo_venta());
		this.crearVenta(ventaEditada);

	}
	
	@Override
	public List<DTOproducto> listaDeProductos(Long idVenta) {
		this.ventaExiste(idVenta);
		return this.traerVenta(idVenta).getListaProductos()
				.stream()
				.map(p-> ProductoMapper.aDTO(p))
				.collect(Collectors.toList());
	
	}
	

	private void ventaExiste(Long idVenta) {
		if (!this.ventaRepo.existsById(idVenta)) {
			throw new VentaException(HttpStatus.NOT_FOUND, CodigosExcepcion.BE302);
		}
	}

	private void validaciones(Cliente cliente, List<Producto> productos) {

		if (this.clienteSv.traerCliente(cliente.getId_cliente()) == null) {
			throw new VentaException(HttpStatus.BAD_REQUEST, CodigosExcepcion.BE300);
		}

		if (!this.productoSv.validarProductos(productos)) {
			throw new VentaException(HttpStatus.BAD_REQUEST, CodigosExcepcion.BE301);
		}
	}



}