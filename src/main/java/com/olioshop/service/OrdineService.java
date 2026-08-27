package com.olioshop.service;

import com.olioshop.model.DettaglioOrdine;
import com.olioshop.model.Ordine;
import com.olioshop.model.Prodotto;
import com.olioshop.model.Utente;
import com.olioshop.repository.OrdineRepository;
import com.olioshop.repository.UtenteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrdineService {

    private final OrdineRepository ordineRepository;
    private final UtenteRepository utenteRepository;
    private final ProdottoService prodottoService;

    @Autowired
    public OrdineService(OrdineRepository ordineRepository, ProdottoService prodottoService, UtenteRepository utenteRepository) {
        this.ordineRepository = ordineRepository;
        this.prodottoService = prodottoService;
        this.utenteRepository = utenteRepository;
    }


    //una volta che ho tutti i pezzi ceh li ho scelti e voglio fare l ordine  chiamo i metodo creo ordine
    @Transactional
    public Ordine creaOrdine(String emailUtente, List<DettaglioOrdine> righeOrdine) {
        if (righeOrdine == null || righeOrdine.isEmpty()) {
            throw new IllegalArgumentException("Il carrello non può essere vuoto.");
        }
        // Cerca l'utente direttamente tramite l'email del token
        Utente utente = utenteRepository.findByEmail(emailUtente)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con email: " + emailUtente));

        //creo l rodine e popolo i suoi campi
        Ordine ordine = new Ordine();
        ordine.setUtente(utente);
        ordine.setDataCreazione(LocalDateTime.now());
        ordine.setStato("CONFERMATO");

        BigDecimal totale = BigDecimal.ZERO;
        //faccio il controllo sui vari prodotti del ordine affinche siano disponibili tutti
        for (DettaglioOrdine riga : righeOrdine) {
            Prodotto prodotto = prodottoService.trovaPerId(riga.getProdotto().getId());
            prodottoService.scalaScorte(prodotto.getId(), riga.getQuantita());
            riga.setPrezzoUnitario(prodotto.getPrezzo());
            riga.setOrdine(ordine);
            BigDecimal subtotale = prodotto.getPrezzo().multiply(BigDecimal.valueOf(riga.getQuantita()));
            totale = totale.add(subtotale);
        }
        ordine.setDettagli(righeOrdine);
        ordine.setPrezzoTotale(totale);
        return ordineRepository.save(ordine);
    }
}