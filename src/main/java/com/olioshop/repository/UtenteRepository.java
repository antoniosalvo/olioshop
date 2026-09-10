package com.olioshop.repository;

import com.olioshop.model.Utente;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface UtenteRepository extends JpaRepository<Utente, Long> {

    //trovo l utente che corrisponde a un acerta mail!
    Optional<Utente> findByEmail(String email);

    //vedo se esiste gi aun utente con la stessa mail
    boolean existsByEmail(String email);
}
