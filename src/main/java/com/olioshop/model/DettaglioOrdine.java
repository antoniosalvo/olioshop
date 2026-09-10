package com.olioshop.model;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.math.BigDecimal;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;



@Getter
@Setter
@EqualsAndHashCode
@ToString
//prodotto ordine quantita e prezzoUnitario

@Entity
@Table(name = "dettagli_ordine")
public class DettaglioOrdine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Integer quantita;

    @Column(nullable = false)
    private BigDecimal prezzoUnitario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "prodotto_id", nullable = false)
    private Prodotto prodotto;

    // Indica a Jackson di non serializzare questo campo in JSON:
    // evita il loop infinito (ricorsione ciclica) tra Ordine e DettaglioOrdine quando si invia la risposta al frontend,
    // senza influire sul salvataggio della relazione nel database.
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ordine_id", nullable = false)
    private Ordine ordine;

    public DettaglioOrdine() {}

    public DettaglioOrdine( Integer quantita, BigDecimal prezzoUnitario, Prodotto prodotto, Ordine ordine) {

        this.quantita = quantita;
        this.prezzoUnitario = prezzoUnitario;
        this.prodotto = prodotto;
        this.ordine = ordine;
    }


}
