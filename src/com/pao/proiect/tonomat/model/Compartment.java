package com.pao.proiect.tonomat.model;

public class Compartment implements Discountable {
    private String codRaft;
    private Product produs;
    private int cantitate;
    private int capacitateMaxima;

    // Constructor
    public Compartment (String codRaft, Product produs, int cantitate, int capacitateMaxima){
        this.codRaft = codRaft;
        if (produs != null) {
            this.produs = produs.duplica();
        } else {
            this.produs = null;
        }

        this.capacitateMaxima = capacitateMaxima;
        this.cantitate = Math.min(cantitate, capacitateMaxima);
    }

    public Compartment() {
        this.codRaft = "N/A";
        this.produs = null;
        this.cantitate = 0;
        this.capacitateMaxima = 10;
    }

    public boolean esteGol() {
        return cantitate == 0;
    }

    // Metoda care actualizeaza stocul in momentul in care un client cumpara ceva
    public boolean elibereazaProdus() {
        if (cantitate > 0) {
            cantitate--;
            return true;
        }
        return false;
    }

    public void suplimenteazaStoc(int cantitateAdaugata) {
        if (cantitate + cantitateAdaugata <= capacitateMaxima) {
            cantitate += cantitateAdaugata;
        } else {
            cantitate = capacitateMaxima;
        }
    }

    public String getCodRaft() {
        return codRaft;
    }

    public void setCodRaft(String codRaft) {
        this.codRaft = codRaft;
    }

    public Product getProdus() {
        return produs;
    }

    public void setProdus(Product produs) {
        if(produs != null) {
            this.produs = produs.duplica();
        } else {
            this.produs = null;
        }
    }

    public int getCantitate() {
        return cantitate;
    }

    public void setCantitate(int cantitate) {
        this.cantitate = cantitate;
    }

    public int getCapacitateMaxima() {
        return capacitateMaxima;
    }

    public void setCapacitateMaxima(int capacitateMaxima) {
        this.capacitateMaxima = capacitateMaxima;
    }

    @Override
    public String toString() {
        if (produs == null) {
            return "[" + codRaft + "] RAFT GOL (Nealocat)";
        }

        if (cantitate == 0) {
            return String.format("[%s] %s | STOC EPUIZAT | Pret: %.2f RON",
                    codRaft, produs.getNume(), produs.getPret());
        }

        return String.format("[%s] %s | Stoc: %d/%d buc. | Pret: %.2f RON",
                codRaft, produs.getNume(), cantitate, capacitateMaxima, produs.getPret());
    }

    @Override
    public void applyDiscount(double procent) {
        if (this.produs != null) {
            // Raftul transmite reducerea mai departe produsului pe care il contine
            this.produs.applyDiscount(procent);
            System.out.println("S-a aplicat o reducere de " + procent + "% pentru tot raftul " + this.codRaft);
        } else {
            System.out.println("Raftul " + this.codRaft + " este gol, nu se poate aplica reducerea.");
        }
    }
}