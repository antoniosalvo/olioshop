package com.olioshop.service;

import com.olioshop.model.Utente;
import com.olioshop.repository.ProdottoRepository;
import com.olioshop.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class UtenteService {

    @Autowired
    private UtenteRepository utenteRepository;
    @Autowired
    public UtenteService(UtenteRepository utenteRepository) {
        this.utenteRepository = utenteRepository;
    }

    @Transactional(readOnly = true)
    public List<Utente> getAllUsers() {
        return utenteRepository.findAll();
    }



    //metodo per salvare l utente nel databse subito dopo che si è registrato su keycloak prendo i dati dal token
    @Transactional
    public Utente sincronizzaUtente(String email, String nome, String cognome, String indirizzo) {
        Optional<Utente> optUtente = utenteRepository.findByEmail(email);

        // Se l'utente è già salvato nel database locale, restituiscilo senza toccare nulla
        if (optUtente.isPresent()) {
            return optUtente.get();
        }

        // Altrimenti viene inserito una volta sola alla prima registrazione
        Utente nuovoUtente = new Utente();
        nuovoUtente.setEmail(email);
        nuovoUtente.setNome(nome);
        nuovoUtente.setCognome(cognome);
        nuovoUtente.setIndirizzo(indirizzo);

        return utenteRepository.save(nuovoUtente);
    }



    @Transactional(readOnly = true)
    public Utente getByEmail(String email) {
        return utenteRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con email: " + email));
    }
}