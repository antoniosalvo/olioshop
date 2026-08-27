package com.olioshop.service;

import com.olioshop.model.Prodotto;
import com.olioshop.repository.ProdottoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProdottoService {
    private final ProdottoRepository prodottoRepository;

    @Autowired
    public ProdottoService(ProdottoRepository prodottoRepository) {
        this.prodottoRepository = prodottoRepository;
    }

    //lo uso per trovare utti i prodotti in fase di apertura del sito
    public List<Prodotto> trovaTutti() {
        return prodottoRepository.findAll();
    }


    //dato un id prendo il prodottoassociato lo uso per vedere quanta capacita ho di quel tipo di prodotto
    public Prodotto trovaPerId(Long id) {
        return prodottoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prodotto non trovato con ID: " + id));
    }



    //nel moemnto in cui faccio l ordine che è fatto da piu dettagli ordine lo uso per scalare le scorte rimanenti una volta
    //che il cliente fa il checkout
    @Transactional
    public void scalaScorte(Long prodottoId, int quantitaRichiesta) {
        if (quantitaRichiesta <= 0) {
            throw new IllegalArgumentException("La quantità da acquistare deve essere maggiore di zero.");
        }
        Prodotto prodotto = trovaPerId(prodottoId);
        if (prodotto.getScorta() < quantitaRichiesta) {
            throw new IllegalStateException("Scorte insufficienti");
        }
        int nuoveScorte = prodotto.getScorta() - quantitaRichiesta;
        prodotto.setScorta(nuoveScorte);
        prodottoRepository.save(prodotto);
    }
}