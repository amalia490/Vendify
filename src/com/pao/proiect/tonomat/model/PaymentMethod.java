package com.pao.proiect.tonomat.model;

public abstract class PaymentMethod {
    protected double suma;
    protected boolean esteReusita;

    // Constructor principal pentru setarea sumei de plata
    public PaymentMethod(double suma) {
        this.suma = suma;
        this.esteReusita = false;
    }

    public PaymentMethod(){
        this.suma = 0;
        this.esteReusita = false;
    }

    // Metoda abstracta care forteaza crearea unei copii in clasele derivate
    public abstract PaymentMethod duplica();

    public abstract boolean proceseazaPlata();

    public double getSuma() {
        return suma;
    }

    public boolean esteReusita() {
        return esteReusita;
    }
}