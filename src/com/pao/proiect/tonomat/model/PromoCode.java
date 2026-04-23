package com.pao.proiect.tonomat.model;

public class PromoCode {
    private String cod;
    private double procentReducere;
    private boolean esteFolosit;

    public PromoCode(String cod, double procentReducere) {
        this.cod = cod;
        this.procentReducere = procentReducere;
        this.esteFolosit = false;
    }

    public boolean esteValid() {
        return !esteFolosit;
    }

    public void marcheazaCaFolosit() {
        this.esteFolosit = true;
    }

    public double calculeazaPretRedus(double pretInitial) {
        if (esteValid()) {
            double valoareReducere = pretInitial * (procentReducere / 100.0);
            return pretInitial - valoareReducere;
        }
        return pretInitial;
    }

    public String getCod() {
        return cod;
    }

    public double getProcentReducere() {
        return procentReducere;
    }

    public boolean esteFolosit() {
        return esteFolosit;
    }

    @Override
    public String toString() {
        String stare = esteFolosit ? "CONSUMAT" : "VALID";
        return String.format("Cupon: %s (-%.0f%%) | Stare: %s", cod, procentReducere, stare);
    }
}