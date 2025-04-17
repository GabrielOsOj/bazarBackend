package com.gbInc.bazar.service.producto;

import com.gbInc.bazar.DTO.DTOproducto;
import com.gbInc.bazar.DataProvider;
import com.gbInc.bazar.persistence.models.Producto;
import com.gbInc.bazar.persistence.repository.IproductoRepository;
import com.gbInc.bazar.services.producto.ProductoService;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductoServiceTest {

	@Mock
	private IproductoRepository productoRepo;

	@InjectMocks
	private ProductoService productoSv;

	@Test
	public void traerProductosTest() {
		// given

		// when
		when(this.productoRepo.findAll()).thenReturn(DataProvider.getListaProductos());
		List<DTOproducto> res = this.productoSv.traerProductos();

		// then
		assertNotNull(res);
		verify(this.productoRepo).findAll();

	}

	@Test
	public void traerProductosTestListaVacia() {
		// given
		List<DTOproducto> listaVacia = List.of();
		// when
		when(this.productoRepo.findAll()).thenReturn(DataProvider.getListaProductosVacia());
		List<DTOproducto> res = this.productoSv.traerProductos();

		// then
		assertEquals(listaVacia,res);
		verify(this.productoRepo).findAll();

	}

	@Test
	public void traerProducto() {
		// given
		Long idProducto = 1L;
		// when
		when(this.productoRepo.existsById(idProducto))
				.thenReturn(true);
		when(this.productoRepo.findById(idProducto)).thenReturn(Optional.of(DataProvider.getProductoUno()));
		DTOproducto res = this.productoSv.traerProducto(idProducto);
		// then
		assertNotNull(res);
		verify(this.productoRepo).existsById(idProducto);
		verify(this.productoRepo).findById(idProducto);
		assertEquals(idProducto, res.getCodigo_producto());

	}

	@Test
	public void traerProductoError() {
		// given
		Long idProducto = 99L;

		// when
		when(this.productoRepo.existsById(idProducto))
				.thenReturn(false);
		DTOproducto res = this.productoSv.traerProducto(idProducto);

		// then
		assertNull(res);
		verify(this.productoRepo).existsById(idProducto);
	}

	@Test
	public void eliminarProducto() {
		// given
		Long idProducto = 1L;
		// when
		when(this.productoRepo.existsById(idProducto)).thenReturn(true);
		Boolean res = this.productoSv.eliminarProducto(idProducto);
		// then
		assertTrue(res);
		verify(this.productoRepo).existsById(idProducto);
		verify(this.productoRepo).deleteById(idProducto);
	}

	@Test
	public void eliminarProductoError() {
		// given
		Long idProducto = 1L;
		// when
		when(this.productoRepo.existsById(idProducto)).thenReturn(false);
		Boolean res = this.productoSv.eliminarProducto(idProducto);
		// then
		assertFalse(res);
		verify(this.productoRepo).existsById(idProducto);

	}

	@Test
	public void editarProducto() {
		// given
		DTOproducto productoEdit = DataProvider.getProductoEditado();
		// when
		when(this.productoRepo.existsById(productoEdit.getCodigo_producto())).thenReturn(true);
		Boolean res = this.productoSv.editarProducto(productoEdit);
		// then
		ArgumentCaptor<Producto> capturador = ArgumentCaptor.forClass(Producto.class);

		assertTrue(res);
		verify(this.productoRepo).existsById(productoEdit.getCodigo_producto());
		verify(this.productoRepo).save(capturador.capture());

	}

	@Test
	public void editarProductoError() {
		// given
		DTOproducto productoEdit = DataProvider.getProductoEditado();
		// when
		when(this.productoRepo.existsById(productoEdit.getCodigo_producto()))
				.thenReturn(false);
		Boolean res = this.productoSv.editarProducto(productoEdit);
		// then
		assertFalse(res);
		verify(this.productoRepo).existsById(productoEdit.getCodigo_producto());
	
	}
	
	@Test
	public void crearProductoTest(){
		//given
		DTOproducto nuevoProd = DataProvider.getNuevoProducto();
		//when
		Boolean resp = this.productoSv.crearProducto(nuevoProd);
		//then
		ArgumentCaptor<Producto> capturador = ArgumentCaptor.forClass(Producto.class);
		assertTrue(resp);
		verify(this.productoRepo).save(capturador.capture());
	}

}
