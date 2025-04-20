package com.gbInc.bazar.persistence.repository;

import com.gbInc.bazar.persistence.models.Producto;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface IproductoRepository extends JpaRepository<Producto, Long> {

	@Query("SELECT p FROM Producto p WHERE p.cantidad_disponible < 5")
	public List<Producto> faltaStock();

	
}
