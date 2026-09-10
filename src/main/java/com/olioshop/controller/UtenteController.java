package com.olioshop.controller;


import com.olioshop.model.Utente;
import com.olioshop.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;

@RestController
@RequestMapping("/utenti")
public class UtenteController {
    private final UtenteService utenteService;
    @Autowired
    public UtenteController(UtenteService utenteService) {
        this.utenteService=utenteService;
    }

    @PostMapping("/registrazione")
    //ResponseEntity<Utente> metodo affinche ogni voltra che prendo il token lo mando al backend
    // afficnhe sincronizzi il database con il token
    public ResponseEntity<?> sincronizza(@AuthenticationPrincipal Jwt jwt) {
        String email = jwt.getClaimAsString("email");
        String nome = jwt.getClaimAsString("given_name");
        String cognome = jwt.getClaimAsString("family_name");
        String indirizzo = jwt.getClaimAsString("indirizzo");

        // Fallback per evitare valori nulli
        if (nome == null || nome.isBlank()) {
            nome = jwt.getClaimAsString("preferred_username");
        }
        if (cognome == null) {
            cognome = "";
        }

        Utente salvato = utenteService.sincronizzaUtente(email, nome, cognome, indirizzo);

        return ResponseEntity.ok(Collections.singletonMap("messaggio", "Utente sincronizzato con successo"));
    }



}
