package com.gbInc.bazar.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.gbInc.bazar.DTO.DTOclienteMayorCompra;
import com.gbInc.bazar.DTO.DTOproducto;
import com.gbInc.bazar.DTO.DTOventa;
import com.gbInc.bazar.DTO.DTOventaYmontoDia;
import com.gbInc.bazar.DataProvider;
import com.gbInc.bazar.exception.CodigosExcepcion;
import com.gbInc.bazar.persistence.repository.IventaRepository;
import com.gbInc.bazar.services.cliente.IclienteService;
import com.gbInc.bazar.services.producto.IproductoService;
import com.gbInc.bazar.services.venta.IventaService;
import jakarta.transaction.Transactional;
import java.time.LocalDate;
import java.time.Month;
import java.util.List;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class ventaControllerTest {

	@Autowired
	private MockMvc mvc;

	@Autowired
	private IclienteService clienteSv;

	@Autowired
	private IproductoService productoSv;

	@Autowired
	private IventaService ventaSv;

	@Autowired
	private IventaRepository ventaRepo;

	private ObjectMapper mapper;

	@BeforeEach
	public void init() {

		this.mapper = JsonMapper.builder().addModule(new JavaTimeModule()).build();

		this.clienteSv.crearCliente(DataProvider.getClienteUnoDTO());
		this.productoSv.crearProducto(DataProvider.getProductoUnoDTO());

		DTOventa nuevaVenta = DataProvider.getNuevaVenta();
		List<DTOproducto> listaProductos = List.of(this.productoSv.traerProductos().get(0));
		listaProductos.get(0).setCantidad_comprada(1D);
		nuevaVenta.setCliente(this.clienteSv.traerClientes().get(0));
		nuevaVenta.setListaProductos(listaProductos);
		this.ventaSv.crearVenta(nuevaVenta);
	}

	@Test
	public void traerVentasTest() throws Exception {

		mvc.perform(MockMvcRequestBuilders.get("/ventas")).andExpect(status().isOk());
	}

	@Test
	public void traerVenta() throws Exception {

		// given
		Long idVenta = this.ventaSv.traerVentas().get(0).getCodigo_venta();
		String url = "/ventas/" + idVenta;
		// when
		MvcResult respuesta = mvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isFound()).andReturn();
		// then
		String dto = respuesta.getResponse().getContentAsString();
		DTOventa venta = mapper.readValue(dto, DTOventa.class);
		assertEquals(idVenta, venta.getCodigo_venta());
	}

	@Test
	public void traerVentaErrorNoExiste() throws Exception {

		// given
		Long idVentaError = 1000L;
		String url = "/ventas/" + idVentaError;
		// when
		MvcResult respuesta = mvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isNotFound()).andReturn();
		// then
		String response = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE302, response);
	}

	@Test
	public void crearVentaTest() throws Exception {
		// given
		DTOventa nuevaVenta = DataProvider.getNuevaVenta();
		List<DTOproducto> listaProductos = List.of(this.productoSv.traerProductos().get(0));
		listaProductos.get(0).setCantidad_comprada(1D);
		nuevaVenta.setCliente(this.clienteSv.traerClientes().get(0));
		nuevaVenta.setListaProductos(listaProductos);

		// then
		mvc.perform(MockMvcRequestBuilders.post("/ventas/crear")
			.content(mapper.writeValueAsString(nuevaVenta))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)).andExpect(status().isCreated());
	}

	@Test
	public void crearVentaTestErrorClienteNull() throws Exception {
		// given
		DTOventa nuevaVenta = DataProvider.getNuevaVentaErrorClienteNull();
		// then
		MvcResult respuesta = mvc
			.perform(MockMvcRequestBuilders.post("/ventas/crear")
				.content(mapper.writeValueAsString(nuevaVenta))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andReturn();

		// then
		String mensaje = respuesta.getResponse().getContentAsString();

		assertEquals(CodigosExcepcion.BE304, mensaje);
	}

	@Test
	public void crearVentaTestErrorProductosNull() throws Exception {
		// given
		DTOventa nuevaVenta = DataProvider.getNuevaVentaErrorProductosNull();
		// when
		MvcResult respuesta = mvc
			.perform(MockMvcRequestBuilders.post("/ventas/crear")
				.content(mapper.writeValueAsString(nuevaVenta))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andReturn();
		// then
		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE305, mensaje);

	}

	@Test
	public void crearVentaTestErrorProductosVacios() throws Exception {
		// given
		DTOventa nuevaVenta = DataProvider.getNuevaVentaErrorProductosVacios();
		// when
		MvcResult respuesta = mvc
			.perform(MockMvcRequestBuilders.post("/ventas/crear")
				.content(mapper.writeValueAsString(nuevaVenta))
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON))
			.andExpect(status().isBadRequest())
			.andReturn();
		// then
		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE306, mensaje);
	}

	@Test
	public void editarVentaTest() throws Exception {
		// given
		DTOventa venta = this.ventaSv.traerVentas().get(0);
		venta.setTotal(1D);
		venta.getListaProductos().get(0).setCantidad_comprada(1D);
		String url = "/ventas/editar/" + venta.getCodigo_venta();
		// when
		this.mvc
			.perform(MockMvcRequestBuilders.put(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(venta)))
			.andExpect(status().isNoContent());
		// then
		DTOventa ventaEditada = this.ventaSv.traerVenta(venta.getCodigo_venta());
		assertEquals(1D, ventaEditada.getTotal());
	}

	@Test
	public void editarVentaTestErrorVentaNoExiste() throws Exception {
		// given
		Long idErroneo = 1000L;
		DTOventa venta = this.ventaSv.traerVentas().get(0);
		venta.setTotal(1D);
		String url = "/ventas/editar/" + idErroneo;
		// when
		MvcResult respuesta = this.mvc
			.perform(MockMvcRequestBuilders.put(url)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(this.mapper.writeValueAsString(venta)))
			.andExpect(status().isNotFound())
			.andReturn();
		// then
		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE302, mensaje);
	}

	@Test
	public void eliminarVentaTest() throws Exception {
		// given
		String idVenta = this.ventaSv.traerVentas().get(0).getCodigo_venta().toString();
		String url = "/ventas/eliminar/" + idVenta;
		// then
		mvc.perform(MockMvcRequestBuilders.delete(url)).andExpect(status().isNoContent());
	}

	@Test
	public void eliminarVentaTestErrorNoExiste() throws Exception {

		// given
		Long idErroneo = 1000L;
		String url = "/ventas/eliminar/" + idErroneo;

		// when
		MvcResult respuesta = mvc.perform(MockMvcRequestBuilders.delete(url))
			.andExpect(status().isNotFound())
			.andReturn();
		// then
		String response = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE302, response);
	}

	@Test
	public void traerProductosDeVenta() throws Exception {
		// given
		DTOventa venta = this.ventaSv.traerVentas().get(0);
		String url = "/ventas/productos/" + venta.getCodigo_venta();

		// when
		MvcResult resp = mvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk()).andReturn();

		// then
		String respuesta = resp.getResponse().getContentAsString();
		List<DTOproducto> ventaResp = this.mapper.readValue(respuesta, new TypeReference<List<DTOproducto>>() {
		});

		assertEquals(venta.getListaProductos().get(0).getNombre(), ventaResp.get(0).getNombre());

	}

	@Test
	public void traerProductosVentaErrorNoExisteVenta() throws Exception {
		// given
		Long idVentaError = 1000L;
		String url = "/ventas/productos/" + idVentaError;
		// when
		MvcResult respuesta = mvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isNotFound()).andReturn();
		// then
		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE302, mensaje);
	}

	@Test
	public void traerMayorVentaTest() throws Exception {
		// given
		String url = "/ventas/mayor_venta";
		// when
		MvcResult respuesta = this.mvc.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk()).andReturn();
		// then
		String respuestaDTO = respuesta.getResponse().getContentAsString();
		DTOclienteMayorCompra mayorCompra = mapper.readValue(respuestaDTO, DTOclienteMayorCompra.class);
		assertNotNull(mayorCompra);
	}

	@Test
	public void traerMayorVentaTestErrorNoHayCompras() throws Exception {
		// given
		this.ventaRepo.deleteAll();
		String url = "/ventas/mayor_venta";
		// when
		MvcResult respuesta = this.mvc.perform(MockMvcRequestBuilders.get(url))
			.andExpect(status().isNotFound())
			.andReturn();
		// then
		String respuestaDTO = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE303, respuestaDTO);
	}

	@Test
	public void traerVentaSegunDiaTest() throws Exception {
		// given

		LocalDate fecha = LocalDate.of(2025, 04, 15);
		String url = "/ventas/";

		// when
		MvcResult respuesta = this.mvc.perform(MockMvcRequestBuilders.get(url).param("fecha", fecha.toString()))
			.andExpect(status().isFound())
			.andReturn();

		// then
		String mensaje = respuesta.getResponse().getContentAsString();
		DTOventaYmontoDia mensajeDTO = this.mapper.readValue(mensaje, DTOventaYmontoDia.class);

	}

	@Test
	public void traerVentaSegunDiaTestErrorFecha() throws Exception {
		// given

		LocalDate fechaError = LocalDate.of(2021, 10, 20);
		String url = "/ventas/?fecha=" + fechaError.toString();

		// when
		MvcResult respuesta = this.mvc.perform(MockMvcRequestBuilders.get(url))
			.andExpect(status().isNotFound())
			.andReturn();

		// then
		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE307, mensaje);

	}

}
