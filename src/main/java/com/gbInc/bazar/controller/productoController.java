package com.gbInc.bazar.controller;

import com.gbInc.bazar.DTO.DTOproducto;
import com.gbInc.bazar.services.producto.IproductoService;
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
@RequestMapping("/productos")
public class productoController {

	private IproductoService productoSv;

	@Autowired
	public productoController(IproductoService productoSv) {
		this.productoSv = productoSv;
	}

	@GetMapping("")
	public ResponseEntity<List<DTOproducto>> traerProductos() {
		return new ResponseEntity<>(productoSv.traerProductos(), HttpStatus.OK);
	}

	@GetMapping("/{idProducto}")
	public ResponseEntity<DTOproducto> traerProducto(@PathVariable Long idProducto) {

		DTOproducto respuesta = this.productoSv.traerProducto(idProducto);

		if (respuesta == null) {
			return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
		}
		else {
			return new ResponseEntity<>(respuesta, HttpStatus.OK);
		}

	}

	@PostMapping("/crear")
	public ResponseEntity<Boolean> crearProducto(@RequestBody DTOproducto producto) {
		this.productoSv.crearProducto(producto);
		return new ResponseEntity<>(true, HttpStatus.CREATED);
	}

	@PutMapping("/editar/{idProducto}")
	public ResponseEntity<Boolean> editarProducto(@PathVariable Long idProducto, @RequestBody DTOproducto producto) {

		producto.setCodigo_producto(idProducto);
		Boolean editado = this.productoSv.editarProducto(producto);

		if (editado) {
			return new ResponseEntity<>(true, HttpStatus.NO_CONTENT);
		}
		else {
			return new ResponseEntity<>(false, HttpStatus.NOT_FOUND);
		}
	}

	@DeleteMapping("/eliminar/{idProducto}")
	public ResponseEntity<Boolean> eliminarProducto(
	@PathVariable Long idProducto){
		
		Boolean eliminado = this.productoSv.eliminarProducto(idProducto);
		
		if(eliminado){
			return new ResponseEntity<>(true,HttpStatus.NO_CONTENT);
		}else{
			return new ResponseEntity<>(false,HttpStatus.NOT_FOUND);
		}
	}
}
