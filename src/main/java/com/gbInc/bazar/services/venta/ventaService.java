package com.gbInc.bazar.services.venta;

import com.gbInc.bazar.DTO.DTOventa;
import com.gbInc.bazar.mappers.VentaMapper;
import com.gbInc.bazar.persistence.repository.IventaRepository;
import java.util.List;
import java.util.stream.Collector;
import java.util.stream.Collectors;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class ventaService implements IventaService{

	
	IventaRepository ventaRepo;

	@Autowired
	public ventaService(IventaRepository ventaRepo) {
		this.ventaRepo = ventaRepo;
	}
	
	@Override
	public Boolean crearVenta(DTOventa venta) {
		

	}

	
	@Override
	public List<DTOventa> traerVentas() {
		
		return this.ventaRepo.findAll()
				.stream()
				.map(venta -> {
					return VentaMapper.aDTO(venta);
				})
				.collect(Collectors.toList());
	}

	@Override
	public DTOventa traerVenta(Long idVenta) {
		
		if(this.ventaRepo.existsById(idVenta)){
			return VentaMapper.aDTO(this.ventaRepo.findById(idVenta).get());
		}
		
		return null;
		
	}

	@Override
	public Boolean eliminarVenta(Long idVenta) {
		
		if(this.ventaRepo.existsById(idVenta)){
			this.ventaRepo.deleteById(idVenta);
			return true;
		}
		
		return false;
	}

	@Override
	public Boolean editarVenta(DTOventa ventaEditada) {
		throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
	}

}
