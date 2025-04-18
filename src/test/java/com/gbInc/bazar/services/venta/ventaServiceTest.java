package com.gbInc.bazar.services.venta;

import com.gbInc.bazar.DTO.DTOventa;
import com.gbInc.bazar.DataProvider;
import com.gbInc.bazar.exception.venta.VentaException;
import com.gbInc.bazar.persistence.models.Venta;
import com.gbInc.bazar.persistence.repository.IventaRepository;
import com.gbInc.bazar.services.cliente.IclienteService;
import com.gbInc.bazar.services.producto.IproductoService;
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
		when(this.productoSv.validarProductos(anyList())).thenReturn(Boolean.TRUE);
		when(this.clienteSv.traerCliente(anyLong())).thenReturn(DataProvider.getClienteUnoDTO());
		this.ventaSv.crearVenta(nuevaVenta);
		// then
		ArgumentCaptor<Venta> capturador = ArgumentCaptor.forClass(Venta.class);

		verify(this.ventaRepo).save(capturador.capture());
		verify(this.productoSv).validarProductos(anyList());
		verify(this.clienteSv).traerCliente(anyLong());

	}

	@Test
	public void crearVentaTestErrorClienteNull() {
		// given
		DTOventa nuevaVenta = DataProvider.getNuevaVenta();
		// when
		when(this.clienteSv.traerCliente(anyLong())).thenReturn(null);
		assertThrows(VentaException.class, () -> {
			this.ventaSv.crearVenta(nuevaVenta);
		});
	}

	@Test
	public void crearVentaTestErrorProductoNoExiste() {
		// given
		DTOventa nuevaVenta = DataProvider.getNuevaVenta();
		// when
		when(this.clienteSv.traerCliente(anyLong())).thenReturn(DataProvider.getClienteUnoDTO());
		when(this.productoSv.validarProductos(anyList())).thenReturn(false);
		assertThrows(VentaException.class, () -> {
			this.ventaSv.crearVenta(nuevaVenta);
		});
	}

	@Test
	public void traerVentasTest() {
		// given

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
	public void editarVentaTest(){
		//given
		Long idVenta = 1L;
		DTOventa ventaDTO = DataProvider.getVentaEditada();		//when
		when(this.ventaRepo.existsById(anyLong())).thenReturn(true);
		when(this.clienteSv.traerCliente(anyLong()))
				.thenReturn(DataProvider.getClienteUnoDTO());
		when(this.productoSv.validarProductos((anyList()))).thenReturn(true);
		this.ventaSv.editarVenta(ventaDTO);
		//then
		ArgumentCaptor<Venta> capturador = ArgumentCaptor.forClass(Venta.class);
		verify(this.ventaRepo).existsById(anyLong());
		verify(ventaRepo).save(capturador.capture());
	}
	
	@Test
	public void editarVentaTestErrorNoExiste(){
		//given
		DTOventa ventaDTO = DataProvider.getVentaEditada();			//when
		when(this.ventaRepo.existsById(ventaDTO.getCodigo_venta())).thenReturn(false);
		//then
		assertThrows(VentaException.class, ()->{
			this.ventaSv.editarVenta(ventaDTO);
		});
		verify(this.ventaRepo).existsById(ventaDTO.getCodigo_venta());
		
	}
}
