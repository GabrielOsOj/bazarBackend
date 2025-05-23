package com.gbInc.bazar.services.cliente;

import com.gbInc.bazar.DTO.DTOcliente;
import com.gbInc.bazar.exception.CodigosExcepcion;
import com.gbInc.bazar.exception.cliente.ClienteException;
import java.util.List;
import org.springframework.stereotype.Service;
import com.gbInc.bazar.mappers.ClienteMapper;
import com.gbInc.bazar.persistence.models.Cliente;
import com.gbInc.bazar.persistence.repository.IclienteRepository;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;

@Service
public class ClienteService implements IclienteService {

	private IclienteRepository clienteRepo;

	@Autowired
	public ClienteService(IclienteRepository clienteServ) {
		this.clienteRepo = clienteServ;
	}

	@Override
	public List<DTOcliente> traerClientes() {

		return clienteRepo.findAll().stream().map(cli -> {
			return ClienteMapper.aDTO(cli);
		}).collect(Collectors.toList());
	}

	@Override
	public DTOcliente traerCliente(Long id) {
		return ClienteMapper.aDTO(this.traerEntidadCliente(id));
	}

	@Override
	public void crearCliente(DTOcliente cliente) {

		this.validarCliente(cliente);
		cliente.setId_cliente(null);
		this.clienteRepo.save(ClienteMapper.aCliente(cliente));

	}

	@Override
	public void eliminarCliente(Long id) {

		this.clienteExiste(id);
		try{
		this.clienteRepo.deleteById(id);
		}catch(DataIntegrityViolationException e){
			throw new ClienteException(HttpStatus.CONFLICT, CodigosExcepcion.BE104);
		}
	}

	@Override
	public void editarCliente(DTOcliente cliente) {

		this.clienteExiste(cliente.getId_cliente());
		this.validarCliente(cliente);
		this.clienteRepo.save(ClienteMapper.aCliente(cliente));

	}

	@Override
	public Cliente traerEntidadCliente(Long id) {

		this.clienteExiste(id);
		return this.clienteRepo.findById(id).get();

	}

	private void clienteExiste(Long idCliente) {

		if (!this.clienteRepo.existsById(idCliente)) {
			throw new ClienteException(HttpStatus.NOT_FOUND, CodigosExcepcion.BE100);
		}
	}

	private void validarCliente(DTOcliente cliente) {

		if (cliente.getNombre() == null||cliente.getNombre().isBlank()) {
			throw new ClienteException(HttpStatus.BAD_REQUEST, CodigosExcepcion.BE101);
		}

		if ( cliente.getApellido() == null||cliente.getApellido().isBlank()) {
			throw new ClienteException(HttpStatus.BAD_REQUEST, CodigosExcepcion.BE102);
		}
		if (cliente.getDni() == null || cliente.getDni().isBlank()) {
			throw new ClienteException(HttpStatus.BAD_REQUEST, CodigosExcepcion.BE103);
		}

	}

}