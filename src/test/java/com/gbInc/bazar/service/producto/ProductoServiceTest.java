package com.gbInc.bazar.service.producto;

import com.gbInc.bazar.DTO.DTOproducto;
import com.gbInc.bazar.DataProvider;
import com.gbInc.bazar.exception.producto.ProductoException;
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
import org.springframework.dao.DataIntegrityViolationException;

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
		assertEquals(listaVacia, res);
		verify(this.productoRepo).findAll();

	}

	@Test
	public void traerProducto() {
		// given
		Long idProducto = 1L;
		// when
		when(this.productoRepo.existsById(idProducto)).thenReturn(true);
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
		when(this.productoRepo.existsById(idProducto)).thenReturn(false);

		// then
		assertThrows(ProductoException.class, () -> {
			this.productoSv.traerProducto(idProducto);
		});

		verify(this.productoRepo).existsById(idProducto);
	}

	@Test
	public void eliminarProducto() {
		// given
		Long idProducto = 1L;
		// when
		when(this.productoRepo.existsById(idProducto)).thenReturn(true);

		this.productoSv.eliminarProducto(idProducto);
		// then
		verify(this.productoRepo).existsById(idProducto);
		verify(this.productoRepo).deleteById(idProducto);
	}

	@Test
	public void eliminarProductoErrorNoExiste() {
		// given
		Long idProducto = 1L;
		// when
		when(this.productoRepo.existsById(idProducto)).thenReturn(false);
		// then
		assertThrows(ProductoException.class, () -> {
			this.productoSv.eliminarProducto(idProducto);
		});
		verify(this.productoRepo).existsById(idProducto);

	}
	
	@Test
	public void eliminarProductoErrorEnlazadoAVenta() {
		// given
		Long idProducto = 999L;
		// when
		when(this.productoRepo.existsById(idProducto)).thenReturn(true);
		
		doThrow(new DataIntegrityViolationException("")).when(this.productoRepo).deleteById(idProducto);
		// then
		assertThrows(ProductoException.class, () -> {
			this.productoSv.eliminarProducto(idProducto);
		});
		verify(this.productoRepo).existsById(idProducto);
	}

	@Test
	public void editarProducto() {
		// given
		DTOproducto productoEdit = DataProvider.getProductoEditado();
		// when
		when(this.productoRepo.existsById(productoEdit.getCodigo_producto())).thenReturn(true);
		this.productoSv.editarProducto(productoEdit);
		// then
		ArgumentCaptor<Producto> capturador = ArgumentCaptor.forClass(Producto.class);

		verify(this.productoRepo).existsById(productoEdit.getCodigo_producto());
		verify(this.productoRepo).save(capturador.capture());

	}

	@Test
	public void editarProductoError() {
		// given
		DTOproducto productoEdit = DataProvider.getProductoEditado();
		// when
		when(this.productoRepo.existsById(productoEdit.getCodigo_producto())).thenReturn(false);
		
		// then
		assertThrows(ProductoException.class, ()->{
		
			this.productoSv.editarProducto(productoEdit);
		});
		
		verify(this.productoRepo).existsById(productoEdit.getCodigo_producto());

	}

	@Test
	public void crearProductoTest() {
		// given
		DTOproducto nuevoProd = DataProvider.getNuevoProducto();
		// when
		this.productoSv.crearProducto(nuevoProd);
		// then
		ArgumentCaptor<Producto> capturador = ArgumentCaptor.forClass(Producto.class);
		verify(this.productoRepo).save(capturador.capture());
	}

	@Test
	public void validarProductosTest() {

		// given
		List<Producto> productos = DataProvider.getListaProductos();
		// when
		when(this.productoRepo.existsById(any())).thenReturn(true);
		Boolean resp = this.productoSv.validarProductos(productos);
		// them
		assertTrue(resp);
		verify(this.productoRepo, times(productos.size())).existsById(any());
	}

	@Test
	public void validarProductosTestErrorProductoNoExiste() {

		// given
		List<Producto> productos = DataProvider.getListaProductos();
		// when
		when(this.productoRepo.existsById(any())).thenReturn(false);
		Boolean resp = this.productoSv.validarProductos(productos);
		// them
		assertFalse(resp);
		verify(this.productoRepo).existsById(any());
	}

	@Test
	public void faltaStockTest(){
		//given
		//when
		when(this.productoRepo.faltaStock())
				.thenReturn(DataProvider.getListaProductos());
		List<DTOproducto> productos = this.productoSv.faltaStock();
		//then
		assertNotNull(productos);
		verify(this.productoRepo).faltaStock();
	}
	
		@Test
	public void faltaStockTestListaVacia(){
		//given
		//when
		when(this.productoRepo.faltaStock())
				.thenReturn(DataProvider.getListaProductosVacia());
		List<DTOproducto> productos = this.productoSv.faltaStock();
		//then
		assertEquals(List.of(),productos);
		verify(this.productoRepo).faltaStock();
	}
	
	@Test
	public void actualizarStockTest(){
		//given
		List<DTOproducto> productos = List.of(DataProvider.getProductoConOtroStockDTO());
		//when
		when(this.productoRepo.existsById(anyLong()))
				.thenReturn(true);
		when(this.productoRepo.findById(anyLong()))
				.thenReturn(Optional.of(DataProvider.getProductoConOtroStock()));
		this.productoSv.actualizarStock(productos);
		//then
		ArgumentCaptor capturador = ArgumentCaptor.forClass(Producto.class);
		verify(this.productoRepo).saveAll(any());
	}
	
	@Test
	public void actualizarStockTestErrorStockNoSuficiente(){
		//given
		List<DTOproducto> productos = List.of(DataProvider.getProductoConOtroStockErrorDTO());
		//when
		when(this.productoRepo.existsById(anyLong()))
				.thenReturn(true);
		when(this.productoRepo.findById(anyLong()))
				.thenReturn(Optional.of(DataProvider.getProductoConOtroStockError()));
		
		assertThrows(ProductoException.class, ()->{
			this.productoSv.actualizarStock(productos);
		});
		
		//then

	}
}
