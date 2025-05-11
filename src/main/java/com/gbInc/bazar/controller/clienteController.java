package com.gbInc.bazar.controller;

import com.gbInc.bazar.DTO.DTOcliente;
import com.gbInc.bazar.services.cliente.IclienteService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@CrossOrigin(origins = { "http://127.0.0.1:5500", "http://localhost:5500/" })
@RestController
@RequestMapping("/clientes")
public class clienteController {

	@Autowired
	IclienteService clienteSv;

	@GetMapping("")
	public ResponseEntity<List<DTOcliente>> traerClientes() {

		return new ResponseEntity<>(this.clienteSv.traerClientes(), HttpStatus.OK);

	}

	@GetMapping("/{id_cliente}")
	public ResponseEntity<DTOcliente> traerCliente(@PathVariable("id_cliente") Long id) {

		DTOcliente cli = this.clienteSv.traerCliente(id);
		return new ResponseEntity<>(cli, HttpStatus.ACCEPTED);

	}

	@PostMapping("/crear")
	public ResponseEntity<Boolean> crearCliente(@RequestBody DTOcliente nuevoCliente) {

		this.clienteSv.crearCliente(nuevoCliente);

		return new ResponseEntity<>(true, HttpStatus.CREATED);

	}

	@PutMapping("/editar/{id_cliente}")
	public ResponseEntity<Boolean> editarCliente(@RequestBody DTOcliente cliente, @PathVariable Long id_cliente) {

		cliente.setId_cliente(id_cliente);
		this.clienteSv.editarCliente(cliente);

		return new ResponseEntity<>(true, HttpStatus.CREATED);
	}

	@DeleteMapping("/eliminar/{id_cliente}")
	public ResponseEntity<Boolean> eliminarCliente(@PathVariable Long id_cliente) {

		this.clienteSv.eliminarCliente(id_cliente);

		return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
	}

}
