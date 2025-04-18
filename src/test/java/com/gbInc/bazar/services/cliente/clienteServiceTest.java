package com.gbInc.bazar.services.cliente;

import com.gbInc.bazar.DTO.DTOcliente;
import com.gbInc.bazar.DataProvider;
import com.gbInc.bazar.exception.cliente.ClienteException;
import com.gbInc.bazar.persistence.models.Cliente;
import com.gbInc.bazar.persistence.repository.IclienteRepository;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.ArgumentCaptor;

@ExtendWith(MockitoExtension.class)
public class ClienteServiceTest {

	@Mock
	private IclienteRepository clienteRepo;

	@InjectMocks
	private ClienteService clienteSv;

	@Test
	public void traerClientesTest() {

		// when
		when(this.clienteRepo.findAll()).thenReturn(DataProvider.getListaClientes());
		List<DTOcliente> result = this.clienteSv.traerClientes();

		// then
		assertNotNull(result);
		assertFalse(result.isEmpty());
		verify(this.clienteRepo).findAll();

	}

	@Test
	public void traerClientesTestVacio() {

		List<DTOcliente> listaVacia = List.of();

		// when
		when(this.clienteRepo.findAll()).thenReturn(DataProvider.getListaClientesVacia());
		List<DTOcliente> result = this.clienteSv.traerClientes();

		// then
		assertEquals(listaVacia, result);
		assertTrue(result.isEmpty());
		verify(this.clienteRepo).findAll();

	}

	@Test
	public void traerClienteTest() {

		// given
		Long idCliente = 1L;
		when(this.clienteRepo.existsById(idCliente)).thenReturn(true);
		when(this.clienteRepo.findById(idCliente)).thenReturn(Optional.of(DataProvider.getClienteUno()));
		// when
		DTOcliente result = this.clienteSv.traerCliente(idCliente);
		// then
		assertNotNull(result);
		verify(this.clienteRepo).existsById(idCliente);
		verify(this.clienteRepo).findById(idCliente);

	}

	@Test
	public void traerClienteTestNoExiste() {

		// given
		Long idNoValido = 25L;

		// when
		when(this.clienteRepo.existsById(idNoValido)).thenReturn(false);

		assertThrows(ClienteException.class, ()->{
			this.clienteSv.traerCliente(idNoValido);
		});
		// then
		verify(this.clienteRepo).existsById(idNoValido);
	}

	@Test
	public void crearClienteTest() {
		// given
		DTOcliente nuevoCliente = DataProvider.getNuevoClienteDTO();
		// when
		this.clienteSv.crearCliente(nuevoCliente);
		// then
		ArgumentCaptor<Cliente> capturador = ArgumentCaptor.forClass(Cliente.class);
		verify(this.clienteRepo).save(capturador.capture());
		assertEquals(null, capturador.getValue().getId_cliente());
	}

	@Test
	public void eliminarClienteTest() {
		// given
		Long idCliente = 1L;
		// when
		when(this.clienteRepo.existsById(idCliente)).thenReturn(true);
		this.clienteSv.eliminarCliente(idCliente);
		// then
		verify(this.clienteRepo).existsById(idCliente);
		verify(this.clienteRepo).deleteById(idCliente);

	}

	@Test
	public void eliminarClienteTestError() {
		// given
		Long idCliente = 999L;
		// when
		when(this.clienteRepo.existsById(idCliente)).thenReturn(false);		
		assertThrows(ClienteException.class, () -> {
			this.clienteSv.eliminarCliente(idCliente);
		});
		// then
		verify(this.clienteRepo).existsById(idCliente);
	}

	@Test
	public void editarClienteTest() {
		// given
		DTOcliente clienteAeditar = DataProvider.getNuevoEditarCliente();
		// when
		when(this.clienteRepo.existsById(clienteAeditar.getId_cliente())).thenReturn(true);
		this.clienteSv.editarCliente(clienteAeditar);
		// them
		ArgumentCaptor<Cliente> capturador = ArgumentCaptor.forClass(Cliente.class);
		verify(this.clienteRepo).existsById(clienteAeditar.getId_cliente());
		verify(this.clienteRepo).save(capturador.capture());

	}

	@Test
	public void editarClienteTestError() {
		// given
		DTOcliente clienteAeditar = DataProvider.getNuevoEditarClienteError();
		// when
		when(this.clienteRepo.existsById(clienteAeditar.getId_cliente())).thenReturn(false);

		assertThrows(ClienteException.class, ()->{
			this.clienteSv.editarCliente(clienteAeditar);
		});
		// them
		verify(this.clienteRepo).existsById(clienteAeditar.getId_cliente());

	}

}

/*
 * public Boolean editarCliente(DTOcliente cliente);
 * 
 * 
 * public Boolean eliminarCliente(Long id);
 */
