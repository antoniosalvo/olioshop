package com.olioshop.repository;

import com.olioshop.model.DettaglioOrdine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DettaglioOrdineRepository extends JpaRepository<DettaglioOrdine, Long> {
}
