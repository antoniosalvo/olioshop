package com.olioshop.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "ordini")
public class Ordine {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(nullable = false)
    private LocalDateTime dataCreazione;

    @Column(nullable = false)
    private BigDecimal prezzoTotale;





    @Column(nullable = false)
    private String stato;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "utente_id", nullable = false)
    private Utente utente;

    // Un ordine contiene molte righe (prodotti + quantità)
    // Se cancelli un ordine, cancelli a cascata anche le sue righe di dettaglio
    @OneToMany(mappedBy = "ordine", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<DettaglioOrdine> dettagli = new ArrayList<>();


    public Ordine(String stato, BigDecimal prezzoTotale, Utente utente) {
        this.stato = stato;
        this.dataCreazione = LocalDateTime.now();
        this.prezzoTotale = prezzoTotale;
        this.utente = utente;

    }

    public Ordine() {this.dataCreazione = LocalDateTime.now();}

}
