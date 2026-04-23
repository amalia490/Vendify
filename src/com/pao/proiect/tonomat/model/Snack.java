package com.pao.proiect.tonomat.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class Snack extends Eatable {
    private int greutateGrame;
    private boolean esteVegan;

    public Snack(String nume, double pret, String marca,
                 LocalDate dataExpirare, boolean faraZahar, String tip,
                 List<String> ingrediente, int calorii, int greutateGrame, boolean esteVegan){
        super(nume, pret, marca, dataExpirare, faraZahar, tip, ingrediente, calorii);

        this.greutateGrame = greutateGrame;
        this.esteVegan = esteVegan;
    }

    public Snack(){
        super();

        this.greutateGrame = 0;
        this.esteVegan = false;
    }

    @Override
    public String getInstructiuniUtilizare() {
        return "A se pastra la loc uscat si racoros.";
    }

    public int getGreutateGrame() {
        return greutateGrame;
    }

    public void setGreutateGrame(int greutateGrame) {
        this.greutateGrame = greutateGrame;
    }

    public boolean esteVegan() {
        return esteVegan;
    }

    public void setVegan(boolean esteVegan) {
        this.esteVegan = esteVegan;
    }

    @Override
    public Product duplica() {
        List<String> copieIngrediente = new ArrayList<>();
        if (this.ingrediente != null) {
            copieIngrediente.addAll(this.ingrediente);
        }

        return new Snack(
                this.nume, this.pret, this.marca,
                this.dataExpirare, this.faraZahar, this.tip,
                copieIngrediente, this.calorii,
                this.greutateGrame, this.esteVegan
        );
    }
}