package com.pao.proiect.tonomat.model;

public class CashPayment extends PaymentMethod {
    private double baniIntrodusi;
    private double restDeDat;

    // Constructor care calculeaza restul in functie de fondurile primite
    public CashPayment(double suma, double baniIntrodusi) {
        super(suma);
        this.baniIntrodusi = baniIntrodusi;
        if (baniIntrodusi >= suma) {
            this.restDeDat = baniIntrodusi - suma;
        } else {
            this.restDeDat = 0.0;
        }
    }

    public CashPayment(){
        super();
        this.baniIntrodusi = 0.00;
        this.restDeDat = 0.00;
    }

    // Metoda pentru crearea unei copii independente a tranzactiei
    @Override
    public PaymentMethod duplica() {
        CashPayment copie = new CashPayment(this.suma, this.baniIntrodusi);
        copie.esteReusita = this.esteReusita;
        return copie;
    }

    // Valideaza daca suma introdusa de client acopera costul produsului
    @Override
    public boolean proceseazaPlata() {
        if (baniIntrodusi >= suma) {
            System.out.println("Plata cash acceptata. Rest de dat: " + restDeDat + " RON");
            this.esteReusita = true;
        } else {
            System.out.println("Fonduri insuficiente! Mai trebuie: " + (suma - baniIntrodusi) + " RON");
            this.esteReusita = false;
        }
        return this.esteReusita;
    }
}