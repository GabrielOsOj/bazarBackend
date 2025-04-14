package com.gbInc.bazar.controller;

import com.gbInc.bazar.DTO.DTOventa;
import com.gbInc.bazar.services.venta.IventaService;
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
@RequestMapping("/ventas")
public class ventaController {

	IventaService ventaSv;

	@Autowired
	public ventaController(IventaService ventaSv) {
		this.ventaSv = ventaSv;
	}

	@GetMapping("")
	public ResponseEntity<List<DTOventa>> traerVentas() {
		return new ResponseEntity<>(this.ventaSv.traerVentas(), HttpStatus.OK);
	}

	@GetMapping("/{codigo_venta}")
	public ResponseEntity<DTOventa> traerVenta(@PathVariable Long codigo_venta) {

		DTOventa venta = this.ventaSv.traerVenta(codigo_venta);

		if (venta == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(venta, HttpStatus.FOUND);

	}

	@PostMapping("/crear")
	public ResponseEntity<Boolean> crearVenta(@RequestBody DTOventa venta) {

		Boolean creado = this.ventaSv.crearVenta(venta);

		if (creado) {
			return new ResponseEntity<>(creado, HttpStatus.CREATED);
		}
		return new ResponseEntity<>(creado, HttpStatus.INTERNAL_SERVER_ERROR);

	}

	@PutMapping("/editar/{codigo_venta}")
	public ResponseEntity<Boolean> editarVenta(
	@PathVariable Long codigo_venta,
	@RequestBody DTOventa ventaEditada){
		
		ventaEditada.setCodigo_venta(codigo_venta);
		
		Boolean editado = this.ventaSv.editarVenta(ventaEditada);
		
		if (editado) {
			return new ResponseEntity<>(editado, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(editado, HttpStatus.NOT_FOUND);

	}
	
	@DeleteMapping("/eliminar/{codigo_venta}")
	public ResponseEntity<Boolean> eliminarVenta(
	@PathVariable Long codigo_venta){
		
		Boolean eliminado = this.ventaSv.eliminarVenta(codigo_venta);
	
		if (eliminado) {
			return new ResponseEntity<>(eliminado, HttpStatus.NO_CONTENT);
		}
		return new ResponseEntity<>(eliminado, HttpStatus.NOT_FOUND);
		
	}

}
