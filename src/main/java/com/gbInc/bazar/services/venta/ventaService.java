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

		Cliente cli = this.clienteSv.traerEntidadCliente(venta.getCliente().getId_cliente());

		this.productoSv.actualizarStock(venta.getListaProductos());

		List<Producto> productos = venta.getListaProductos()
			.stream()
			.map(p -> this.productoSv.traerEntidadProducto(p.getCodigo_producto()))
			.collect(Collectors.toList());

		
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
		return this.traerVenta(idVenta)
			.getListaProductos();
	}
	
	private void ventaExiste(Long idVenta) {
		if (!this.ventaRepo.existsById(idVenta)) {
			throw new VentaException(HttpStatus.NOT_FOUND, CodigosExcepcion.BE302);
		}
	}

	@Override
	public DTOclienteMayorCompra traerClienteMayorCompra() {
				
		Venta ventaMax = this.ventaRepo.traerMayorVenta();

		if(ventaMax==null){
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
		
		if(ventaYmonto == null){
			throw new VentaException(HttpStatus.NOT_FOUND,
			CodigosExcepcion.BE303);
		}
		
		return ventaYmonto;
	}
	
}