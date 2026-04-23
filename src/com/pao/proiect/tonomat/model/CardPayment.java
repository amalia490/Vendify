package com.pao.proiect.tonomat.model;

public class CardPayment extends PaymentMethod {
    private String numarCard;
    private String numeTitular;

    // Constructor pentru initializarea datelor de pe card
    public CardPayment(double suma, String numarCard, String numeTitular) {
        super(suma);
        this.numarCard = numarCard;
        this.numeTitular = numeTitular;
    }

    public CardPayment() {
        super();
        this.numarCard = "nespecificat";
        this.numeTitular = "nespecificat";
    }

    // Returneaza o clona a obiectului curent
    @Override
    public PaymentMethod duplica() {
        CardPayment copie = new CardPayment(this.suma, this.numarCard, this.numeTitular);
        copie.esteReusita = this.esteReusita;
        return copie;
    }

    // Simuleaza procesarea platii
    @Override
    public boolean proceseazaPlata() {
        System.out.println("Se proceseaza cardul " + numarCard + "...");
        this.esteReusita = true;
        return this.esteReusita;
    }
}