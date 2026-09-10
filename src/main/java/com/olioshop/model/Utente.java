package com.olioshop.model;

import jakarta.persistence.*;

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
//creo un entita chiamandola come voglio sia chiamata la tabella
@Entity
@Table(name = "utenti")
public class Utente {

    //definisco id e come generare id (Ogni volta che salvi un nuovo record, MySQL assegna in modo
    // progressivo e univoco il numero successivo (1, 2, 3, 4...) senza rischio di duplicati.
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //ogni column è un attributo e posso renderlo non nullable
    @Column(nullable = false)
    private String nome;

    @Column(nullable = false)
    private String cognome;

    //non possono esistere 2 utenti con stessa mail (che è unica)
    @Column(nullable = false, unique = true)
    private String email;




    @Column(nullable = false)
    private String indirizzo;

    //In JPA/Hibernate, qualsiasi variabile dichiarata dentro una classe @Entity diventa in automatico
    // una colonna della tabella SQL, anche se non metti esplicitamente l'annotazione @Column


    // Relazione: Un utente può avere molti ordini
    // Relazione 1-a-Molti: 1 utente ha molti ordini.
    // 'mappedBy': la chiave esterna (utente_id) sta fisicamente nella tabella 'ordini'.
    // 'cascade = ALL': se l'utente viene salvato/cancellato, l'operazione si riflette a cascata sui suoi ordini.
    @OneToMany(mappedBy = "utente", cascade = CascadeType.ALL)
    private List<Ordine> ordini = new ArrayList<>();

    //COSTRUTTORI VUOTO (OBBLIGATORIO PER JPA) E CON PARAMETRI E I SETTER E GETTER PER ACCEDERE
    // AI CAMPI acnhe se lo le annotazioni lo danno gia di per se
    public Utente() {}

    public Utente(String nome, String cognome, String email, String indirizzo) {
        this.nome = nome;
        this.cognome = cognome;
        this.email = email;
        this.indirizzo = indirizzo;
    }

}