package com.gbInc.bazar.services.venta;

import com.gbInc.bazar.DTO.DTOventa;
import com.gbInc.bazar.exception.venta.VentaException;
import com.gbInc.bazar.exception.venta.VentaExceptionCodigos;
import com.gbInc.bazar.mappers.VentaMapper;
import com.gbInc.bazar.persistence.models.Cliente;
import com.gbInc.bazar.persistence.models.Producto;
import com.gbInc.bazar.persistence.models.Venta;
import com.gbInc.bazar.persistence.repository.IventaRepository;
import com.gbInc.bazar.services.cliente.IclienteService;
import com.gbInc.bazar.services.producto.IproductoService;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collector;
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
		if (cli == null) {
			throw new VentaException(HttpStatus.BAD_REQUEST, VentaExceptionCodigos.BE400);
		}

		if (!this.productoSv.validarProductos(venta.getListaProductos())) {
			throw new VentaException(HttpStatus.BAD_REQUEST, VentaExceptionCodigos.BE401);
		}

		List<Producto> productos = new ArrayList<>();
		venta.getListaProductos().forEach(p -> {
			productos.add(this.productoSv.traerEntidadProducto(p.getCodigo_producto()));
		});

		Venta nuevaVenta = Venta.builder()
				.codigo_venta(null)
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

		if (this.ventaRepo.existsById(idVenta)) {
			return VentaMapper.aDTO(this.ventaRepo.findById(idVenta).get());
		}

		return null;

	}

	@Override
	public Boolean eliminarVenta(Long idVenta) {

		if (this.ventaRepo.existsById(idVenta)) {
			this.ventaRepo.deleteById(idVenta);
			return true;
		}

		return false;
	}

	@Override
	public Boolean editarVenta(DTOventa ventaEditada) {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from
																		// nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}

}
