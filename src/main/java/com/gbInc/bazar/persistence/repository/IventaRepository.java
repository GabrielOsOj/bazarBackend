package com.gbInc.bazar.persistence.repository;

import com.gbInc.bazar.DTO.DTOventaYmontoDia;
import com.gbInc.bazar.persistence.models.Venta;
import java.time.LocalDate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IventaRepository extends JpaRepository<Venta, Long> {
	
	@Query("select v from Venta v where v.total=(select max(v.total) from Venta v)")
	public Venta traerMayorVenta();
	
	@Query("select new com.gbInc.bazar.DTO.DTOventaYmontoDia(v.fecha_venta,count(v),sum(v.total)) from Venta v where v.fecha_venta=:fecha")
	public DTOventaYmontoDia ventasDeUnDia(LocalDate fecha);
}
