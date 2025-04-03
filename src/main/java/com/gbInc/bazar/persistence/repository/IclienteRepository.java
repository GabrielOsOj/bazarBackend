package com.gbInc.bazar.persistence.repository;

import com.gbInc.bazar.persistence.models.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IclienteRepository extends JpaRepository<Cliente, Long> {
	
}
