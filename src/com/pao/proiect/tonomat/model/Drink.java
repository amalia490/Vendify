package com.pao.proiect.tonomat.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Drink extends Eatable {
    private int volum;
    private String aroma;

    // Constructor
    public Drink(String nume, double pret, String marca,
                 LocalDate dataExpirare, boolean faraZahar, String tip,
                 List<String> ingrediente, int calorii,
                 int volum, String aroma){
        super(nume, pret, marca, dataExpirare, faraZahar, tip, ingrediente, calorii);

        this.volum = volum;
        this.aroma = aroma;
    }

    public Drink() {
        super();
        this.volum = 0;
        this.aroma = "fara aroma";
    }

    public int getVolum() {
        return volum;
    }

    public void setVolum(int volum) {
        this.volum = volum;
    }

    public String getAroma() {
        return aroma;
    }

    public void setAroma(String aroma) {
        this.aroma = aroma;
    }

    @Override
    public String getInstructiuniUtilizare() {
        return "A se agita inainte de consum, serviti rece.";
    }

    @Override
    public Product duplica() {
        // Cream o copie a listei de ingrediente pentru a nu afecta produsul original
        List<String> copieIngrediente = new ArrayList<>();
        if (this.ingrediente != null) {
            copieIngrediente.addAll(this.ingrediente);
        }

        return new Drink(
                this.nume, this.pret, this.marca,
                this.dataExpirare, this.faraZahar, this.tip,
                copieIngrediente, this.calorii,
                this.volum, this.aroma
        );
    }
}