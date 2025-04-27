package com.gbInc.bazar.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;
import com.gbInc.bazar.DTO.DTOcliente;
import com.gbInc.bazar.DataProvider;
import com.gbInc.bazar.exception.CodigosExcepcion;
import com.gbInc.bazar.persistence.repository.IclienteRepository;
import com.gbInc.bazar.services.cliente.IclienteService;
import jakarta.transaction.Transactional;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import static org.junit.jupiter.api.Assertions.*;
import org.springframework.http.MediaType;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
public class clienteControllerTest {

	@Autowired
	MockMvc mock;

	@Autowired
	IclienteService clienteSv;

	@Autowired
	IclienteRepository clienteRepo;

	JsonMapper mapper = new JsonMapper();

	@BeforeEach
	public void init() {

		DataProvider.getListaClientesDTO().forEach(c -> {
			this.clienteSv.crearCliente(c);
		});

	}

	@Test
	public void traerClienteTest() throws Exception {
		// given
		DTOcliente clienteDB = this.clienteSv.traerClientes().get(0);
		String url = "/clientes/" + clienteDB.getId_cliente();
		// when
		MvcResult respuesta = this.mock.perform(MockMvcRequestBuilders.get(url))
			.andExpect(status().isAccepted())
			.andReturn();

		// then
		String clienteString = respuesta.getResponse().getContentAsString();
		DTOcliente clienteResp = mapper.readValue(clienteString, DTOcliente.class);

		assertEquals(clienteDB.getDni(), clienteResp.getDni());

	}

	@Test
	public void traerClienteTestErrorClienteNoExiste() throws Exception {
		// given
		Long idClienteErroneo = 1000L;
		String url = "/clientes/" + idClienteErroneo;
		// when
		MvcResult respuesta = this.mock.perform(MockMvcRequestBuilders.get(url))
			.andExpect(status().isNotFound())
			.andReturn();

		// then
		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE100, mensaje);

	}

	@Test
	public void traerClientesTest() throws Exception {
		// given
		String url = "/clientes";
		// when
		MvcResult respuesta = this.mock.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk()).andReturn();

		// then
		String clienteString = respuesta.getResponse().getContentAsString();
		List<DTOcliente> clienteResp = mapper.readValue(clienteString, new TypeReference<List<DTOcliente>>() {
		});

		assertNotNull(clienteResp.get(0).getDni());
		assertNotNull(clienteResp.get(1).getDni());
		assertNotNull(clienteResp.get(2).getDni());
		assertNotNull(clienteResp.get(3).getDni());
		assertNotNull(clienteResp.get(4).getDni());
	}

	@Test
	public void traerClientesTestNoHayClientes() throws Exception {
		// given
		String url = "/clientes";
		this.clienteRepo.deleteAll();
		// when
		MvcResult respuesta = this.mock.perform(MockMvcRequestBuilders.get(url)).andExpect(status().isOk()).andReturn();

		// then
		String clienteString = respuesta.getResponse().getContentAsString();
		List<DTOcliente> clienteResp = mapper.readValue(clienteString, new TypeReference<List<DTOcliente>>() {
		});

		assertEquals(List.of(), clienteResp);
	}

	@Test
	public void crearClienteTest() throws Exception {
		// given
		String url = "/clientes/crear";
		DTOcliente nuevoCliente = DataProvider.getNuevoClienteDTO();
		// when
		this.mock.perform(MockMvcRequestBuilders.post(url)
			.content(mapper.writeValueAsString(nuevoCliente))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)

		).andExpect(status().isCreated());

	}

	@Test
	public void crearClienteTestErrorNombreVacio() throws Exception {
		// given
		String url = "/clientes/crear";
		DTOcliente nuevoCliente = DataProvider.getNuevoClienteDTO();
		nuevoCliente.setNombre("");
		// when
		MvcResult respuesta = this.mock.perform(MockMvcRequestBuilders.post(url)
			.content(mapper.writeValueAsString(nuevoCliente))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)

		).andExpect(status().isBadRequest()).andReturn();

		// then
		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE101, mensaje);
	}

	@Test
	public void crearClienteTestErrorApellidoVacio() throws Exception {
		// given
		String url = "/clientes/crear";
		DTOcliente nuevoCliente = DataProvider.getNuevoClienteDTO();
		nuevoCliente.setApellido("");
		// when
		MvcResult respuesta = this.mock.perform(MockMvcRequestBuilders.post(url)
			.content(mapper.writeValueAsString(nuevoCliente))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)

		).andExpect(status().isBadRequest()).andReturn();

		// then
		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE102, mensaje);
	}

	public void crearClienteTestErrorDniVacio() throws Exception {
		// given
		String url = "/clientes/crear";
		DTOcliente nuevoCliente = DataProvider.getNuevoClienteDTO();
		nuevoCliente.setDni("");
		// when
		MvcResult respuesta = this.mock.perform(MockMvcRequestBuilders.post(url)
			.content(mapper.writeValueAsString(nuevoCliente))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)

		).andExpect(status().isBadRequest()).andReturn();

		// then
		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE103, mensaje);
	}

	@Test
	public void editarClienteTest() throws Exception {
		// given
		DTOcliente clienteEditado = this.clienteSv.traerClientes().get(0);
		String nuevoNombre = "claudio";
		clienteEditado.setNombre(nuevoNombre);

		String url = "/clientes/editar/" + clienteEditado.getId_cliente();
		// when
		this.mock.perform(MockMvcRequestBuilders.put(url)
			.content(mapper.writeValueAsString(clienteEditado))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)

		).andExpect(status().isCreated());

		// then

		assertEquals(nuevoNombre, this.clienteSv.traerCliente(clienteEditado.getId_cliente()).getNombre());

	}

	@Test
	public void editarClienteTestErrorNombreVacio() throws Exception {
		// given
		DTOcliente clienteEditado = this.clienteSv.traerClientes().get(0);
		String nuevoNombre = "";
		clienteEditado.setNombre(nuevoNombre);

		String url = "/clientes/editar/" + clienteEditado.getId_cliente();
		// when
		MvcResult respuesta = this.mock.perform(MockMvcRequestBuilders.put(url)
			.content(mapper.writeValueAsString(clienteEditado))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)

		).andExpect(status().isBadRequest()).andReturn();

		// then

		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE101, mensaje);

	}

	@Test
	public void editarClienteTestErrorApellidoVacio() throws Exception {
		// given
		DTOcliente clienteEditado = this.clienteSv.traerClientes().get(0);
		String nuevoApellido = "";
		clienteEditado.setApellido(nuevoApellido);

		String url = "/clientes/editar/" + clienteEditado.getId_cliente();
		// when
		MvcResult respuesta = this.mock.perform(MockMvcRequestBuilders.put(url)
			.content(mapper.writeValueAsString(clienteEditado))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)

		).andExpect(status().isBadRequest()).andReturn();

		// then

		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE102, mensaje);

	}

	@Test
	public void editarClienteTestErrorDniVacio() throws Exception {
		// given
		DTOcliente clienteEditado = this.clienteSv.traerClientes().get(0);
		String nuevoDni = "";
		clienteEditado.setDni(nuevoDni);

		String url = "/clientes/editar/" + clienteEditado.getId_cliente();
		// when
		MvcResult respuesta = this.mock.perform(MockMvcRequestBuilders.put(url)
			.content(mapper.writeValueAsString(clienteEditado))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)

		).andExpect(status().isBadRequest()).andReturn();

		// then

		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE103, mensaje);

	}

	@Test
	public void editarClienteTestErrorNombreNull() throws Exception {
		// given
		DTOcliente clienteEditado = this.clienteSv.traerClientes().get(0);
		String nuevoNombre = null;
		clienteEditado.setNombre(nuevoNombre);

		String url = "/clientes/editar/" + clienteEditado.getId_cliente();
		// when
		MvcResult respuesta = this.mock.perform(MockMvcRequestBuilders.put(url)
			.content(mapper.writeValueAsString(clienteEditado))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)

		).andExpect(status().isBadRequest()).andReturn();

		// then

		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE101, mensaje);

	}

	@Test
	public void editarClienteTestErrorApellidoNull() throws Exception {
		// given
		DTOcliente clienteEditado = this.clienteSv.traerClientes().get(0);
		String nuevoApellido = null;
		clienteEditado.setApellido(nuevoApellido);

		String url = "/clientes/editar/" + clienteEditado.getId_cliente();
		// when
		MvcResult respuesta = this.mock.perform(MockMvcRequestBuilders.put(url)
			.content(mapper.writeValueAsString(clienteEditado))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)

		).andExpect(status().isBadRequest()).andReturn();

		// then

		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE102, mensaje);

	}

	@Test
	public void editarClienteTestErrorDniNull() throws Exception {
		// given
		DTOcliente clienteEditado = this.clienteSv.traerClientes().get(0);
		String nuevoDni = null;
		clienteEditado.setDni(nuevoDni);

		String url = "/clientes/editar/" + clienteEditado.getId_cliente();
		// when
		MvcResult respuesta = this.mock.perform(MockMvcRequestBuilders.put(url)
			.content(mapper.writeValueAsString(clienteEditado))
			.contentType(MediaType.APPLICATION_JSON)
			.accept(MediaType.APPLICATION_JSON)

		).andExpect(status().isBadRequest()).andReturn();

		// then

		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE103, mensaje);

	}

	@Test
	public void eliminarClienteTest() throws Exception {
		// given
		DTOcliente clienteDB = this.clienteSv.traerClientes().get(0);
		String url = "/clientes/eliminar/" + clienteDB.getId_cliente();
		// when
		this.mock.perform(MockMvcRequestBuilders.delete(url)).andExpect(status().isNoContent());
		// then

		assertFalse(this.clienteRepo.existsById(clienteDB.getId_cliente()));

	}

	@Test
	public void eliminarClienteTestErrorClienteNoExiste() throws Exception {
		// given
		Long idErroneo = 1000L;
		String url = "/clientes/eliminar/" + idErroneo;

		// when
		MvcResult respuesta = this.mock.perform(MockMvcRequestBuilders.delete(url))
			.andExpect(status().isNotFound())
			.andReturn();

		// then
		String mensaje = respuesta.getResponse().getContentAsString();
		assertEquals(CodigosExcepcion.BE100, mensaje);
	}

}
