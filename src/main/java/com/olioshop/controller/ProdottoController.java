package com.olioshop.controller;


import com.olioshop.model.Prodotto;
import com.olioshop.service.ProdottoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/prodotti")
public class ProdottoController {
    private final ProdottoService prodottoService;

    @Autowired
    public ProdottoController(ProdottoService prodottoService) {
        this.prodottoService = prodottoService;
    }

    @GetMapping
    public ResponseEntity<List<Prodotto>> trovaTuttiProdotto() {
        List<Prodotto> catalogo = prodottoService.trovaTutti();
        return new ResponseEntity<>(catalogo, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Prodotto> trovaProdotto(@PathVariable Long id) {
        try {
            Prodotto p = prodottoService.trovaPerId(id);
            return new ResponseEntity<>(p, HttpStatus.OK);
        } catch(RuntimeException e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
}
