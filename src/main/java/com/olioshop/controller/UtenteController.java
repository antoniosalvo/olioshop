package com.olioshop.controller;


import com.olioshop.model.Utente;
import com.olioshop.service.UtenteService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/utenti")
public class UtenteController {
    private final UtenteService utenteService;
    @Autowired
    public UtenteController(UtenteService utenteService) {
        this.utenteService=utenteService;
    }

    @PostMapping("/registrazione")
    public ResponseEntity<?> registraUtente(@RequestBody Utente utente){
        try {
            Utente u = utenteService.registerUser(utente);
            return new ResponseEntity<Utente>(u, HttpStatus.CREATED);
        }
        catch(Exception e) {
            return  ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    //POI VEDIAMO SE VOGLIAMO AGGIUNGERE IL METODO PER VEDERE IL RPOFILO DELL UTENTE

}
