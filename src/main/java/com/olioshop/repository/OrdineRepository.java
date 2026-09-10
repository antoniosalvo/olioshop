package com.olioshop.repository;


import com.olioshop.model.Ordine;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;


@Repository
public interface OrdineRepository extends JpaRepository<Ordine, Long> {
    List<Ordine> findByUtenteId(Long utenteId);

    //lo ho gia built in dall interfaccia
    List<Ordine> findTop5ByUtenteIdOrderByDataCreazioneDesc(Long utenteId);
}
