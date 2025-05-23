package com.gbInc.bazar.exception;

/*0 - 99 codigos genericos*/
/*100 - 199 codigos para cliente*/
/*200 - 299 codigos para producto*/
/*300 - 399 codigos para venta*/

public class CodigosExcepcion {
	
	/*Clientes*/
	public static final String BE100 = "El cliente no existe en la base de datos";
	public static final String BE101 = "El nombre del cliente no es valido";
	public static final String BE102 = "El apellido del cliente no es valido";
	public static final String BE103 = "El dni del cliente no es valido";
	public static final String BE104 = "El cliente esta asociado a una venta";
	/*Productos*/
	public static final String BE200 = "El producto no existe en la base de datos";
	public static final String BE201 = "No hay suficiente stock del producto para completar la venta";
	public static final String BE203 = "El nombre del producto no es valido";
	public static final String BE204 = "La marca del producto no es valida";
	public static final String BE205 = "El producto esta asociado a una venta";
	
	/* Ventas */
	public static final String BE300 = "Error con el cliente";
	public static final String BE301 = "Error con el producto";
	public static final String BE302 = "No existe una venta asociada a ese id";
	public static final String BE303 = "No hay ventas registradas en la base de datos";
	public static final String BE304 = "El cliente no puede ser null";
	public static final String BE305 = "La lista de productos no puede ser null";
	public static final String BE306 = "La lista de productos no puede estar vacia";
	public static final String BE307 = "No hay ventas registradas en esa fecha";
	
	
			
}
