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
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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


    @Transactional(readOnly = true) //true perche devo solo leggere
    //parto dalal mail
    public List<Map<String, Object>> getUltimiOrdiniUtente(String emailUtente) {
        //trovo utente corrispondente e trovo gli ordini corrispondentei a utente con quella mail e certo id
        Utente utente = utenteRepository.findByEmail(emailUtente)
                .orElseThrow(() -> new RuntimeException("Utente non trovato con email: " + emailUtente));

        List<Ordine> ordini = ordineRepository.findTop5ByUtenteIdOrderByDataCreazioneDesc(utente.getId());

        List<java.util.Map<String, Object>> risultato = new ArrayList<>();

        for (Ordine o : ordini) {
            java.util.Map<String, Object> mappaOrdine = new HashMap<>();
            mappaOrdine.put("id", o.getId());
            mappaOrdine.put("dataCreazione", o.getDataCreazione());
            mappaOrdine.put("prezzoTotale", o.getPrezzoTotale());
            mappaOrdine.put("stato", o.getStato());

            List<Map<String, Object>> articoli = new ArrayList<>();
            for (DettaglioOrdine d : o.getDettagli()) {
                Map<String, Object> mappaDettaglio = new HashMap<>();
                mappaDettaglio.put("nomeProdotto", d.getProdotto().getNome());
                mappaDettaglio.put("quantita", d.getQuantita());
                mappaDettaglio.put("prezzoUnitario", d.getPrezzoUnitario());
                articoli.add(mappaDettaglio);
            }
            mappaOrdine.put("articoli", articoli);
            risultato.add(mappaOrdine);
        }

        return risultato;
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
            // ================= INIZIO PAUSA TEMPORANEA =================
            //questo l ho usato per fare il controllo se funzioanva il check per l overbooking (funziona pare)
            /*
            try {
                //System.out.println(">>> [LOCK ATTIVO] Utente: " + emailUtente + " - Metto in pausa per 5 secondi...");
                Thread.sleep(5000);
                //System.out.println(">>> [FINE PAUSA] Utente: " + emailUtente + " - Salvo e rilascio il lock.");
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }

             */
            // ================== FINE PAUSA TEMPORANEA ==================
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