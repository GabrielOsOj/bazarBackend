package com.gbInc.bazar.exception;

/*0 - 99 codigos genericos*/
/*100 - 199 codigos para cliente*/
/*200 - 299 codigos para producto*/
/*300 - 399 codigos para venta*/

public class CodigosExcepcion {
	
	/*Clientes*/
	public static final String BE100 = "El cliente no existe en la base de datos";
	
	/*Productos*/
	public static final String BE200 = "El producto no existe en la base de datos";
	public static final String BE201 = "No hay suficiente stock del producto para completar la venta";
	
	/* Ventas */
	public static final String BE300 = "Error con el cliente";
	public static final String BE301 = "Error con el producto";
	public static final String BE302 = "No existe una venta asociada a ese id";
	public static final String BE303 = "No hay ventas registradas en la base de datos";
			
}
