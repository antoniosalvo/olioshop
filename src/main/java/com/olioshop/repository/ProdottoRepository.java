package com.olioshop.repository;


import com.olioshop.model.Prodotto;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface ProdottoRepository extends JpaRepository<Prodotto, Long> {
    // Blocco per il lock pesismistico sul prodotto
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Prodotto p WHERE p.id = :id")
    Optional<Prodotto> findByIdWithLock(@Param("id") Long id);


}
