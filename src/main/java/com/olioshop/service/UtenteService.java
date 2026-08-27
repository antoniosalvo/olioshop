package com.olioshop.service;

import com.olioshop.model.Utente;
import com.olioshop.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;

    @Transactional(readOnly = false, propagation = Propagation.REQUIRED)
    public Utente registerUser(Utente utente) {
        if (utenteRepository.existsByEmail(utente.getEmail())) {
            throw new IllegalArgumentException("Errore: un utente con questa email esiste già.");
        }
        return utenteRepository.save(utente);
    }

    @Transactional(readOnly = true)
    public List<Utente> getAllUsers() {
        return utenteRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Utente getByEmail(String email) {
        return utenteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con email: " + email));
    }
}