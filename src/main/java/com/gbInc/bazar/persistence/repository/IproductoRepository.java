package com.gbInc.bazar.persistence.repository;

import com.gbInc.bazar.persistence.models.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IproductoRepository extends JpaRepository<Producto, Long> {

}
