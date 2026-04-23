package com.pao.proiect.tonomat.model;

public class Gadget extends Product {
    private int luniGarantie;
    private boolean necesitaBaterii;

    public Gadget(String nume, double pret, String marca, int luniGarantie, boolean necesitaBaterii){
        super(nume, pret, marca);

        this.luniGarantie = luniGarantie;
        this.necesitaBaterii = necesitaBaterii;
    }

    public Gadget(){
        super();

        this.luniGarantie = 6;
        this.necesitaBaterii = false;
    }

    @Override
    public String getInstructiuniUtilizare() {
        return "Cititi manualul producatorului inainte de conectare.";
    }

    public int getLuniGarantie() {
        return luniGarantie;
    }

    public void setLuniGarantie(int luniGarantie) {
        this.luniGarantie = luniGarantie;
    }

    public boolean necesitaBaterii() {
        return necesitaBaterii;
    }

    public void setNecesitaBaterii(boolean necesitaBaterii) {
        this.necesitaBaterii = necesitaBaterii;
    }

    @Override
    public Product duplica() {
        return new Gadget(
                this.nume, this.pret, this.marca,
                this.luniGarantie, this.necesitaBaterii
        );
    }
}