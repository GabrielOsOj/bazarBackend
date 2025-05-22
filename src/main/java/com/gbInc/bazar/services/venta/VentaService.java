package com.gbInc.bazar.services.venta;

import com.gbInc.bazar.DTO.DTOclienteMayorCompra;
import com.gbInc.bazar.DTO.DTOproducto;
import com.gbInc.bazar.DTO.DTOventa;
import com.gbInc.bazar.DTO.DTOventaYmontoDia;
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
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;

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

		this.validacionesVenta(venta);

		Cliente cli = this.clienteSv.traerEntidadCliente(venta.getCliente().getId_cliente());

		this.productoSv.actualizarStock(venta.getListaProductos());

		List<Producto> productosPlanos = new ArrayList<>();
		for (DTOproducto p : venta.getListaProductos()) {
			Producto productoBDD = this.productoSv.traerEntidadProducto(p.getCodigo_producto());
			for (int i = 0; i < p.getCantidad_comprada(); i++) {
				productosPlanos.add(productoBDD);
			}
		}

		Venta nuevaVenta = Venta.builder()
			.codigo_venta(venta.getCodigo_venta())
			.fecha_venta(venta.getFecha_venta())
			.cliente(cli)
			.listaProductos(productosPlanos)
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
		this.validacionesVenta(ventaEditada);
		
		Cliente cli = this.clienteSv.traerEntidadCliente(ventaEditada.getCliente().getId_cliente());

		
		List<DTOproducto> productosAgregados = 
				this.parseadorProductos(ventaEditada.getCodigo_venta(),ventaEditada.getListaProductos());
		

		this.productoSv.actualizarStock(productosAgregados);

		List<Producto> productosPlanos = new ArrayList<>();
		for (DTOproducto p : ventaEditada.getListaProductos()) {
			Producto productoBDD = this.productoSv.traerEntidadProducto(p.getCodigo_producto());
			for (int i = 0; i < p.getCantidad_comprada(); i++) {
				productosPlanos.add(productoBDD);
			}
		}
		
		Venta nuevaVenta = Venta.builder()
			.codigo_venta(ventaEditada.getCodigo_venta())
			.fecha_venta(ventaEditada.getFecha_venta())
			.cliente(cli)
			.listaProductos(productosPlanos)
			.total(ventaEditada.getTotal())
			.build();

		this.ventaRepo.save(nuevaVenta);
		
	}
	
	private List<DTOproducto> parseadorProductos(Long idVenta, List<DTOproducto> listaProductosEntrantes){

		List<DTOproducto> productosDeLaVentaGuardados = this
			.compactadorProductos(this.listaDeProductos(idVenta));
		
		List<Long> idProductoGuardado = productosDeLaVentaGuardados.stream()
			.map(pg -> pg.getCodigo_producto())
			.collect(Collectors.toList());

		List<DTOproducto> productosNuevos = new ArrayList<>();
		List<DTOproducto> productosAgregados = new ArrayList<>();

		List<DTOproducto> copiaProductos =listaProductosEntrantes.stream().map((p) -> {
			return DTOproducto.builder()
				.codigo_producto(p.getCodigo_producto())
				.nombre(p.getNombre())
				.marca(p.getMarca())
				.costo(p.getCosto())
				.cantidad_disponible(p.getCantidad_disponible())
				.cantidad_comprada(p.getCantidad_comprada())
				.build();
		}).collect(Collectors.toList());

		copiaProductos.forEach((p) -> {
			int index = idProductoGuardado.indexOf(p.getCodigo_producto());
			// si es -1, el producto no existia en la venta
			if (index == -1) {
				productosNuevos.add(p);
				return;
			}
			// si existia, compruebo si se agregaron productos
			if (p.getCantidad_comprada() > productosDeLaVentaGuardados.get(index).getCantidad_comprada()) {
				p.setCantidad_comprada(
						p.getCantidad_comprada() - productosDeLaVentaGuardados.get(index).getCantidad_comprada());
				productosAgregados.add(p);
				return;
			}
			// si existia y se mantiene igual, lo descarto
		});

		productosAgregados.addAll(productosNuevos);
		return productosAgregados;
	}

	@Override
	public List<DTOproducto> listaDeProductos(Long idVenta) {
		return this.traerVenta(idVenta).getListaProductos();
	}

	@Override
	public DTOclienteMayorCompra traerClienteMayorCompra() {

		Venta ventaMax = this.ventaRepo.traerMayorVenta();

		if (ventaMax == null) {
			throw new VentaException(HttpStatus.NOT_FOUND, CodigosExcepcion.BE303);
		}
		return DTOclienteMayorCompra.builder()
			.codigo_venta(ventaMax.getCodigo_venta())
			.total(ventaMax.getTotal())
			.cantidadProductos(ventaMax.getListaProductos().size())
			.nombreCliente(ventaMax.getCliente().getNombre())
			.apellidoCliente(ventaMax.getCliente().getApellido())
			.build();
	}

	@Override
	public DTOventaYmontoDia traerVentasSegunFecha(LocalDate fecha) {
		DTOventaYmontoDia ventaYmonto = this.ventaRepo.ventasDeUnDia(fecha);

		if (ventaYmonto == null || ventaYmonto.getMontoDelDia() == null) {
			throw new VentaException(HttpStatus.NOT_FOUND, CodigosExcepcion.BE307);
		}

		return ventaYmonto;
	}

	/* Utilidades */

	private void validacionesVenta(DTOventa venta) {
		if (venta.getCliente() == null) {
			throw new VentaException(HttpStatus.BAD_REQUEST, CodigosExcepcion.BE304);
		}

		if (venta.getListaProductos() == null) {
			throw new VentaException(HttpStatus.BAD_REQUEST, CodigosExcepcion.BE305);
		}

		if (venta.getListaProductos().size() == 0) {
			throw new VentaException(HttpStatus.BAD_REQUEST, CodigosExcepcion.BE306);
		}
	}

	private void ventaExiste(Long idVenta) {
		if (!this.ventaRepo.existsById(idVenta)) {
			throw new VentaException(HttpStatus.NOT_FOUND, CodigosExcepcion.BE302);
		}
	}

	private List<DTOproducto> compactadorProductos(List<DTOproducto> productos) {

		List<DTOproducto> productosCompactos = new ArrayList<>();
		List<Long> productosCompactosId = new ArrayList<>();

		for (DTOproducto p : productos) {

			if (productosCompactosId.contains(p.getCodigo_producto())) {
				int index = productosCompactosId.indexOf(p.getCodigo_producto());
				productosCompactos.get(index)
					.setCantidad_comprada(productosCompactos.get(index).getCantidad_comprada() + 1);
			}
			else {
				p.setCantidad_comprada(1D);
				productosCompactos.add(p);
				productosCompactosId.add(p.getCodigo_producto());
			}

		}

		return productosCompactos;
	}

}