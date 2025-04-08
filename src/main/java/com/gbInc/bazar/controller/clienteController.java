package com.gbInc.bazar.controller;

import com.gbInc.bazar.DTO.DTOcliente;
import com.gbInc.bazar.services.cliente.IclienteService;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

		if (cli.equals(null)) {
			return new ResponseEntity<>(cli, HttpStatus.ACCEPTED);
		}
		else {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/crear")
	public ResponseEntity<Boolean> crearCliente(@RequestBody DTOcliente nuevoCliente) {

		Boolean creado = this.clienteSv.crearCliente(nuevoCliente);

		if (creado) {
			return new ResponseEntity<>(creado, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(creado, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@PutMapping("/editar/{id_cliente}")
	public ResponseEntity<Boolean> editarCliente(@RequestBody DTOcliente cliente) {

		Boolean editado = this.clienteSv.editarCliente(cliente);

		if (editado) {
			return new ResponseEntity<>(editado, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(editado, HttpStatus.INTERNAL_SERVER_ERROR);
		}

	}

	@DeleteMapping("/eliminar/{id_cliente}")
	public ResponseEntity<Boolean> eliminarCliente(@PathVariable Long id) {

		Boolean eliminado = this.clienteSv.eliminarCliente(id);
		if (eliminado) {
			return new ResponseEntity<>(eliminado, HttpStatus.CREATED);
		}
		else {
			return new ResponseEntity<>(eliminado, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}
