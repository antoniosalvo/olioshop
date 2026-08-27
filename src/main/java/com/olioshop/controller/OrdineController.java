package com.olioshop.controller;

import com.olioshop.model.DettaglioOrdine;
import com.olioshop.model.Ordine;
import com.olioshop.service.OrdineService;

import com.olioshop.support.authentication.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/ordini")
public class OrdineController {
    private final OrdineService ordineService;
    @Autowired
    public OrdineController(OrdineService ordineService) {
        this.ordineService = ordineService;
    }

    @PostMapping("/paga")
    public ResponseEntity<?> paga( @RequestBody List<DettaglioOrdine> righeOrdine) {
        try{
            //uso la classe utils che ha il metodo per prendere l email dal token
            // che sta viaggiano d tanto per fare pagamento devo avere per froza il token
            String email = Utils.getEmail();
            Ordine o = ordineService.creaOrdine(email, righeOrdine);
            return new ResponseEntity<>(o, HttpStatus.CREATED);
        }
        catch(IllegalArgumentException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
        catch(RuntimeException e){
            return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


}
