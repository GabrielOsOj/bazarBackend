package com.gbInc.bazar.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.databind.type.ReferenceType;
import com.gbInc.bazar.DTO.DTOproducto;
import com.gbInc.bazar.DataProvider;
import com.gbInc.bazar.exception.CodigosExcepcion;
import com.gbInc.bazar.persistence.models.Producto;
import com.gbInc.bazar.persistence.repository.IproductoRepository;
import com.gbInc.bazar.services.producto.IproductoService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class productoControllerTest {

	@Autowired
	MockMvc mvc;

	@Autowired
	IproductoRepository productoRepo;

	@Autowired
	IproductoService productoSv;

	private JsonMapper mapper;

	@BeforeEach
	public void init() {

		DataProvider.getListaProductosDTO().forEach(p -> {
			this.productoSv.crearProducto(p);
		});

		this.mapper = new JsonMapper();

	}

	@Test
	public void traerProducto() throws Exception {

		// given
		DTOproducto productoExistente = this.productoSv.traerProductos().get(0);
		String url = "/productos/" + productoExistente.getCodigo_producto();
		// when
		MvcResult respuesta = this.mvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk()).andReturn();
		// then
		String stringDto = respuesta.getResponse().getContentAsString();
		DTOproducto productoDTO = this.mapper.readValue(stringDto, DTOproducto.class);
		assertEquals(productoExistente.getNombre(), productoDTO.getNombre());
	}

	@Test
	public void traerProductoErrorNoExiste() throws Exception {

		// given
		Long productoNoExistente = 1000L;
		String url = "/productos/" + productoNoExistente;
		// when
		MvcResult respuesta = this.mvc.perform(MockMvcRequestBuilders.get(url))
			.andExpect(status().isNotFound())
			.andReturn();
		// then
		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE200, mensaje);
	}

	@Test
	public void traerProductosTest() throws Exception {
		// given
		String url = "/productos";
		// when
		MvcResult respuesta = this.mvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk()).andReturn();
		// then
		String contenido = respuesta.getResponse().getContentAsString();

		List<DTOproducto> productosResp = this.mapper.readValue(contenido, new TypeReference<List<DTOproducto>>() {
		});

		assertNotEquals(List.of(), productosResp);
	}

	@Test
	public void traerProductosTestNoHayProductos() throws Exception {
		// given
		this.productoRepo.deleteAll();
		String url = "/productos";
		// when
		MvcResult respuesta = this.mvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk()).andReturn();
		// then
		String contenido = respuesta.getResponse().getContentAsString();

		List<DTOproducto> productosResp = this.mapper.readValue(contenido, new TypeReference<List<DTOproducto>>() {
		});

		assertEquals(List.of(), productosResp);

	}

	@Test
	public void crearProductoTest() throws Exception {

		// given
		String url = "/productos/crear";
		DTOproducto nuevoProducto = DataProvider.getNuevoProducto();

		// then
		this.mvc
			.perform(MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(nuevoProducto)))
			.andExpect(status().isCreated());

	}

	@Test
	public void crearProductoTestErrorNombreNull() throws Exception {

		// given
		String url = "/productos/crear";
		DTOproducto nuevoProducto = DataProvider.getNuevoProducto();
		nuevoProducto.setNombre(null);
		// then
		this.mvc
			.perform(MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(nuevoProducto)))
			.andExpect(status().isBadRequest());

	}

	@Test
	public void crearProductoTestErrorMarcaNull() throws Exception {
		// given
		String url = "/productos/crear";
		DTOproducto nuevoProducto = DataProvider.getNuevoProducto();
		nuevoProducto.setMarca(null);
		// then
		this.mvc
			.perform(MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(nuevoProducto)))
			.andExpect(status().isBadRequest());

	}
	
		@Test
	public void crearProductoTestErrorNombreVacio() throws Exception {

		// given
		String url = "/productos/crear";
		DTOproducto nuevoProducto = DataProvider.getNuevoProducto();
		nuevoProducto.setNombre("");
		// then
		this.mvc
			.perform(MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(nuevoProducto)))
			.andExpect(status().isBadRequest());

	}

	@Test
	public void crearProductoTestErrorMarcaVacio() throws Exception {
		// given
		String url = "/productos/crear";
		DTOproducto nuevoProducto = DataProvider.getNuevoProducto();
		nuevoProducto.setMarca("");
		// then
		this.mvc
			.perform(MockMvcRequestBuilders.post(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(nuevoProducto)))
			.andExpect(status().isBadRequest());

	}

	@Test
	public void eliminarProductoTest() throws Exception {

		// given

		Long productoExistente = this.productoSv.traerProductos().get(0).getCodigo_producto();
		String url = "/productos/eliminar/" + productoExistente;

		// when
		this.mvc.perform(MockMvcRequestBuilders.delete(url)).andExpect(status().isNoContent());
		// then

	}

	@Test
	public void eliminarProductoTestErrorNoExisteElProducto() throws Exception {

		// given
		Long productoExistente = 800L;
		String url = "/productos/eliminar/" + productoExistente;

		// when
		MvcResult respuesta = this.mvc.perform(MockMvcRequestBuilders.delete(url))
			.andExpect(status().isNotFound())
			.andReturn();
		// then

		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE200, mensaje);

	}

	@Test
	public void faltaStockTest() throws Exception {

		// given
		String url = "/productos/falta_stock";
		// when
		MvcResult respuesta = this.mvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk()).andReturn();
		// then

		String dtoresp = respuesta.getResponse().getContentAsString();
		List<DTOproducto> productos = this.mapper.readValue(dtoresp, new TypeReference<List<DTOproducto>>() {
		});
		assertNotNull(productos.get(0));
		assertNotNull(productos.get(1));
	}

	@Test
	public void faltaStockTestNoFaltaStock() throws Exception {

		// given
		String url = "/productos/falta_stock";
		this.productoRepo.deleteAll();
		// when
		MvcResult respuesta = this.mvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk()).andReturn();
		// then

		String dtoresp = respuesta.getResponse().getContentAsString();
		List<DTOproducto> productos = this.mapper.readValue(dtoresp, new TypeReference<List<DTOproducto>>() {
		});

		assertTrue(productos.isEmpty());

	}

	@Test
	public void editarProductoTest() throws Exception {
		// given
		DTOproducto productoEditado = this.productoSv.traerProductos().get(0);
		String nuevoNombreProducto = "Frascos de vidrio 1L";
		productoEditado.setNombre(nuevoNombreProducto);
		String url = "/productos/editar/" + productoEditado.getCodigo_producto();
		// when
		MvcResult respuesta = this.mvc
			.perform(MockMvcRequestBuilders.put(url)
				.content(this.mapper.writeValueAsString(productoEditado))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNoContent())
			.andReturn();
		// then
		assertEquals(nuevoNombreProducto, this.productoSv.traerProducto(productoEditado.getCodigo_producto()).getNombre());
	}

	@Test
	public void editarProductoTestErrorNoExiste() throws Exception {
		// given
		DTOproducto productoEditado = DataProvider.getNuevoProducto();
		Long idNoExiste = 1000L;
		String url = "/productos/editar/" + idNoExiste;
		// when
		MvcResult respuesta = this.mvc
			.perform(MockMvcRequestBuilders.put(url)
				.content(this.mapper.writeValueAsString(productoEditado))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isNotFound())
			.andReturn();
		// then
		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE200, mensaje);
	}

}