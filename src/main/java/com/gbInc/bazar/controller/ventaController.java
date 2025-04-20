package com.gbInc.bazar.controller;

import com.gbInc.bazar.DTO.DTOclienteMayorCompra;
import com.gbInc.bazar.DTO.DTOproducto;
import com.gbInc.bazar.DTO.DTOventa;
import com.gbInc.bazar.DTO.DTOventaYmontoDia;
import com.gbInc.bazar.services.venta.IventaService;
import java.time.LocalDate;
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
import org.springframework.web.bind.annotation.RequestParam;
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
		return new ResponseEntity<>(venta, HttpStatus.FOUND);

	}

	@PostMapping("/crear")
	public ResponseEntity<Boolean> crearVenta(@RequestBody DTOventa venta) {

		venta.setCodigo_venta(null);
		this.ventaSv.crearVenta(venta);
		return new ResponseEntity<>(true, HttpStatus.CREATED);

	}

	@PutMapping("/editar/{codigo_venta}")
	public ResponseEntity<Boolean> editarVenta(@PathVariable Long codigo_venta, @RequestBody DTOventa ventaEditada) {

		ventaEditada.setCodigo_venta(codigo_venta);

		this.ventaSv.editarVenta(ventaEditada);
		return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);

	}

	@DeleteMapping("/eliminar/{codigo_venta}")
	public ResponseEntity<Boolean> eliminarVenta(@PathVariable Long codigo_venta) {

		this.ventaSv.eliminarVenta(codigo_venta);
		return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
		
	}
	
	@GetMapping("/productos/{codigo_venta}")
	public ResponseEntity<List<DTOproducto>> traerProductosDeVenta(
	@PathVariable Long codigo_venta ){
	
		return new ResponseEntity<>(this.ventaSv.listaDeProductos(codigo_venta),HttpStatus.OK);
	
	}

	@GetMapping("/mayor_venta")
	public ResponseEntity<DTOclienteMayorCompra> traerMayorVenta(){
		
		return new ResponseEntity<>(this.ventaSv.traerClienteMayorCompra(),HttpStatus.OK);

	}
	
	@GetMapping("/")
	public ResponseEntity<DTOventaYmontoDia> traerVentasSegunDia(
	@RequestParam LocalDate fecha){
		
		return new ResponseEntity<>(this.ventaSv.traerVentasSegunFecha(fecha),HttpStatus.FOUND);
		
	}
}
