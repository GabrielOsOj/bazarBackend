package com.gbInc.bazar.services.venta;

import com.gbInc.bazar.DTO.DTOventa;
import com.gbInc.bazar.DataProvider;
import com.gbInc.bazar.persistence.models.Venta;
import com.gbInc.bazar.persistence.repository.IventaRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import org.mockito.ArgumentCaptor;
import static org.mockito.Mockito.*;


@ExtendWith(MockitoExtension.class)
public class ventaServiceTest {

	@Mock
	IventaRepository ventaRepo;
	
	@InjectMocks
	ventaService ventaSv;
	
	@Test
	public void crearVentaTest(){
		//given
		DTOventa nuevaVenta = DataProvider.getNuevaVenta();
		//when
		Boolean resp = this.ventaSv.crearVenta(nuevaVenta);
		//then
		assertTrue(resp);
		ArgumentCaptor<Venta> capturador = ArgumentCaptor.forClass(Venta.class);
		
		verify(this.ventaRepo).save(capturador.capture());
	}
	
	@Test
	public void crearVentaTestErrorProductoNoExiste(){
		//given
		//when
		//then
	}
}
