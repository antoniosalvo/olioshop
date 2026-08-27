package com.olioshop.model;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@Entity
@Table(name = "prodotti")
public class Prodotto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //ogni column è un attributo e posso renderlo non nullable
    @Column(nullable = false)
    private String nome;

    private String descrizione;

    @Column(nullable = false)
    private BigDecimal prezzo;

    @Column(nullable = false)
    private Integer scorta;

    @Column(nullable = false)
    private String formato;



    @Column(nullable = false)
    private String tipologia;

    @Column(nullable = false)
    private String immagineURL;



    public Prodotto(String nome, String descrizione, BigDecimal prezzo, Integer scorta, String formato, String tipologia, String immagineURL) {
        this.nome = nome;
        this.descrizione = descrizione;
        this.prezzo = prezzo;
        this.scorta = scorta;
        this.formato = formato;
        this.tipologia = tipologia;
        this.immagineURL = immagineURL;

    }
    public Prodotto() {}

}
