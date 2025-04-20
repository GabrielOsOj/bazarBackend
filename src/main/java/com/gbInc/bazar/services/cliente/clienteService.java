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

		cliente.setId_cliente(null);
		this.clienteRepo.save(ClienteMapper.aCliente(cliente));
		
	}

	@Override
	public void eliminarCliente(Long id) {

		this.clienteExiste(id);
		this.clienteRepo.deleteById(id);

	}

	@Override
	public void editarCliente(DTOcliente cliente) {

		this.clienteExiste(cliente.getId_cliente());
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

}