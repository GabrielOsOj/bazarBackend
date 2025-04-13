package com.gbInc.bazar.services.cliente;

import com.gbInc.bazar.DTO.DTOcliente;
import java.util.List;
import org.springframework.stereotype.Service;
import com.gbInc.bazar.mappers.ClienteMapper;
import com.gbInc.bazar.persistence.repository.IclienteRepository;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;

@Service
public class clienteService implements IclienteService {

	private IclienteRepository clienteRepo;

	@Autowired
	public clienteService(IclienteRepository clienteServ) {
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

		if (clienteRepo.existsById(id)) {
			return ClienteMapper.aDTO(clienteRepo.findById(id).get());
		}

		return null;
	}

	@Override
	public Boolean crearCliente(DTOcliente cliente) {

		cliente.setId_cliente(null);
		this.clienteRepo.save(ClienteMapper.aCliente(cliente));
		return true;

	}

	@Override
	public Boolean eliminarCliente(Long id) {

		if (!this.clienteRepo.existsById(id)) {
			return false;
		}

		this.clienteRepo.deleteById(id);
		return true;

	}

	@Override
	public Boolean editarCliente(DTOcliente cliente) {

		if (!this.clienteRepo.existsById(cliente.getId_cliente())) {
			return false;
		}

		this.clienteRepo.save(ClienteMapper.aCliente(cliente));

		return true;
	}

}
