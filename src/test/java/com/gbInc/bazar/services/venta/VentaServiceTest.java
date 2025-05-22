package com.gbInc.bazar.services.venta;

import com.gbInc.bazar.DTO.DTOclienteMayorCompra;
import com.gbInc.bazar.DTO.DTOproducto;
import com.gbInc.bazar.DTO.DTOventa;
import com.gbInc.bazar.DTO.DTOventaYmontoDia;
import com.gbInc.bazar.DataProvider;
import com.gbInc.bazar.exception.cliente.ClienteException;
import com.gbInc.bazar.exception.venta.VentaException;
import com.gbInc.bazar.persistence.models.Cliente;
import com.gbInc.bazar.persistence.models.Venta;
import com.gbInc.bazar.persistence.repository.IventaRepository;
import com.gbInc.bazar.services.cliente.IclienteService;
import com.gbInc.bazar.services.producto.IproductoService;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VentaServiceTest {

	@Mock
	IventaRepository ventaRepo;

	@InjectMocks
	VentaService ventaSv;

	@Mock
	IproductoService productoSv;

	@Mock
	IclienteService clienteSv;

	@Test
	public void crearVentaTest() {
		// given
		DTOventa nuevaVenta = DataProvider.getNuevaVenta();
		// when
		when(this.clienteSv.traerEntidadCliente(anyLong())).thenReturn(DataProvider.getClienteUno());
		when(this.productoSv.traerEntidadProducto(anyLong())).thenReturn(DataProvider.getProductoUno());
		this.ventaSv.crearVenta(nuevaVenta);
		// then
		ArgumentCaptor<Venta> capturador = ArgumentCaptor.forClass(Venta.class);

		verify(this.ventaRepo).save(capturador.capture());
		verify(this.productoSv, times(nuevaVenta.getListaProductos().size())).traerEntidadProducto(anyLong());
		verify(this.clienteSv).traerEntidadCliente(anyLong());

	}

	@Test
	public void traerVentasTest() {
		// when
		when(this.ventaRepo.findAll()).thenReturn(DataProvider.getListaDeVentas());
		List<DTOventa> ventas = this.ventaSv.traerVentas();
		// then
		assertNotNull(ventas);
		verify(this.ventaRepo).findAll();
	}

	@Test
	public void traerVentasTestListaVacia() {
		// when
		when(this.ventaRepo.findAll()).thenReturn(DataProvider.getListaDeVentasVacia());
		List<DTOventa> ventas = this.ventaSv.traerVentas();
		// then
		assertEquals(List.of(), ventas);
		verify(this.ventaRepo).findAll();
	}

	@Test
	public void traerVentaTest() {
		// given
		Long idVenta = 1L;
		// when
		when(this.ventaRepo.existsById(anyLong())).thenReturn(true);
		when(this.ventaRepo.findById(idVenta)).thenReturn(Optional.of(DataProvider.getVentaUno()));
		DTOventa venta = this.ventaSv.traerVenta(idVenta);
		// then
		assertNotNull(venta);
		verify(this.ventaRepo).existsById(idVenta);
		verify(this.ventaRepo).findById(idVenta);
	}

	@Test
	public void traerVentaTestErrorNoExiste() {
		// given
		Long idVenta = 1L;
		// then
		when(this.ventaRepo.existsById(idVenta)).thenReturn(false);

		assertThrows(VentaException.class, () -> {
			this.ventaSv.traerVenta(idVenta);
		});
		verify(this.ventaRepo).existsById(idVenta);

	}

	@Test
	public void eliminarVentaTest() {
		// given
		Long idVenta = 1L;
		// when
		when(this.ventaRepo.existsById(anyLong())).thenReturn(true);
		this.ventaSv.eliminarVenta(idVenta);
		// then
		verify(this.ventaRepo).deleteById(idVenta);
	}

	public void eliminarVentaTestErrorNoExiste() {
		// given
		Long idVenta = 1L;
		// when
		when(this.ventaRepo.existsById(anyLong())).thenReturn(false);
		this.ventaSv.eliminarVenta(idVenta);
		// then
		assertThrows(VentaException.class, () -> {
			this.ventaSv.eliminarVenta(idVenta);
		});
		verify(this.ventaRepo).deleteById(idVenta);
	}

	@Test
	public void editarVentaTest() {
		// given
		DTOventa ventaEditada = DataProvider.ventaParaEditar();
		// when
		when(this.ventaRepo.existsById(anyLong())).thenReturn(true);
		when(this.clienteSv.traerEntidadCliente(anyLong())).thenReturn(DataProvider.getClienteUno());
		when(this.productoSv.traerEntidadProducto(anyLong())).thenReturn(DataProvider.getProductoUno());
		when(this.ventaRepo.findById(anyLong()))
				.thenReturn(Optional.of(DataProvider.ventaOriginalParaEditar()));
		
		this.ventaSv.editarVenta(ventaEditada);
		// then

		verify(this.ventaRepo,times(2)).existsById(anyLong());
		verify(this.clienteSv).traerEntidadCliente(anyLong());
		verify(this.productoSv, times(ventaEditada.getListaProductos().size())).traerEntidadProducto(anyLong());

	}
	
		@Test
public void editarVentaTestMasProductos() {
		// given
		DTOventa ventaEditada = DataProvider.ventaParaEditarMasProductos();

		// when
		when(this.ventaRepo.existsById(anyLong())).thenReturn(true);
when(this.ventaRepo.findById(anyLong()))
				.thenReturn(Optional.of(DataProvider.ventaOriginalParaEditar()));
		when(this.clienteSv.traerEntidadCliente(anyLong())).thenReturn(DataProvider.getClienteUno());
		when(this.productoSv.traerEntidadProducto(anyLong())).thenReturn(DataProvider.getProductoUno());

		
		this.ventaSv.editarVenta(ventaEditada);
		// then

		verify(this.ventaRepo,atLeastOnce()).existsById(anyLong());
		verify(this.clienteSv).traerEntidadCliente(anyLong());
		verify(this.productoSv,atLeastOnce()).traerEntidadProducto(anyLong());

	}

	@Test
	public void editarVentaTestCompactador() {
		// given
		DTOventa ventaEditada = DataProvider.ventaParaEditar();
		// when
		when(this.ventaRepo.existsById(anyLong())).thenReturn(true);
		when(this.clienteSv.traerEntidadCliente(anyLong())).thenReturn(DataProvider.getClienteUno());
		when(this.productoSv.traerEntidadProducto(anyLong())).thenReturn(DataProvider.getProductoUno());
		when(this.ventaRepo.findById(anyLong()))
				.thenReturn(Optional.of(DataProvider.ventaParaEditarProductosDobles()));
		
		this.ventaSv.editarVenta(ventaEditada);
		// then

		verify(this.ventaRepo,times(2)).existsById(anyLong());
		verify(this.clienteSv).traerEntidadCliente(anyLong());
		verify(this.productoSv, times(ventaEditada.getListaProductos().size())).traerEntidadProducto(anyLong());

	}

	@Test
	public void editarVentaTestErrorNoExiste() {
		// given
		DTOventa ventaDTO = DataProvider.ventaParaEditar(); 
// when
		when(this.ventaRepo.existsById(ventaDTO.getCodigo_venta())).thenReturn(false);
		// then
		assertThrows(VentaException.class, () -> {
			this.ventaSv.editarVenta(ventaDTO);
		});
		verify(this.ventaRepo).existsById(ventaDTO.getCodigo_venta());

	}

	@Test
	public void listaDeProductosTest() {
		// given
		Long idVenta = 1L;
		// when
		when(this.ventaRepo.existsById(anyLong())).thenReturn(true);
		when(this.ventaRepo.findById(anyLong())).thenReturn(Optional.of(DataProvider.getVentaUno()));
		List<DTOproducto> productos = this.ventaSv.listaDeProductos(idVenta);
		// then
		assertNotNull(productos);
		verify(this.ventaRepo).existsById(anyLong());
		verify(this.ventaRepo).findById(anyLong());

	}

	@Test
	public void listaDeProductosTestErrorVentaNoExiste() {
		// given
		Long idVenta = 1L;
		// when
		when(this.ventaRepo.existsById(anyLong())).thenReturn(false);
		assertThrows(VentaException.class, () -> {
			this.ventaSv.listaDeProductos(idVenta);
		});
		// then
		verify(this.ventaRepo).existsById(anyLong());
	}

	@Test
	public void traerClienteMayorCompraTest() {
		// when
		when(this.ventaRepo.traerMayorVenta()).thenReturn(DataProvider.getVentaUno());
		DTOclienteMayorCompra dto = this.ventaSv.traerClienteMayorCompra();
		// then
		assertNotNull(dto);
		assertInstanceOf(DTOclienteMayorCompra.class, dto);
		verify(this.ventaRepo).traerMayorVenta();
	}

	@Test
	public void traerClienteMayorCompraTestNoHayVenta() {
		// when
		when(this.ventaRepo.traerMayorVenta()).thenReturn(null);
		// then
		assertThrows(VentaException.class, () -> {
			DTOclienteMayorCompra dto = this.ventaSv.traerClienteMayorCompra();
		});
		verify(this.ventaRepo).traerMayorVenta();
	}

	@Test
	public void traerVentaSegunFechaTest(){
		//given
		LocalDate fecha = LocalDate.now();
		//when
		when(this.ventaRepo.ventasDeUnDia(fecha))
				.thenReturn(DataProvider.getVentaYmontoDTO());
	    DTOventaYmontoDia dto = this.ventaSv.traerVentasSegunFecha(fecha);
		//then
		assertNotNull(dto);
		assertInstanceOf(DTOventaYmontoDia.class,dto);
		verify(this.ventaRepo).ventasDeUnDia(fecha);
	}
	
	@Test
	public void traerVentaSegunFechaTestError(){
		//given
		LocalDate fecha = LocalDate.now();
		//when
		when(this.ventaRepo.ventasDeUnDia(fecha))
				.thenReturn(null);
	    assertThrows(VentaException.class,()->{
			this.ventaSv.traerVentasSegunFecha(fecha);
		});
		//then
		verify(this.ventaRepo).ventasDeUnDia(fecha);
	}
	
}
