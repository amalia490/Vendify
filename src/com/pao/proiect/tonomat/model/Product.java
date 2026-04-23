package com.pao.proiect.tonomat.model;

public abstract class Product implements Comparable<Product>, Discountable {
    protected String id;
    protected String nume;
    protected double pret;
    protected String marca;
    private static int contorProduse = 1;

    // Constructor cu parametri
    public Product(String nume, double pret, String marca){
        this.id = "PRD-" + contorProduse;
        contorProduse++;
        this.nume = nume;
        this.marca = marca;
        this.pret = pret;
    }

    public Product(){
        this.id = "PRD-" + contorProduse;
        contorProduse++;
        this.nume = "produs nou";
        this.pret = 0.0;
        this.marca = "fara marca";
    }

    public Product(Product p){
        this.id = p.id;
        this.nume = p.nume;
        this.pret = p.pret;
        this.marca = p.marca;
    }

    public abstract Product duplica();

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public double getPret() {
        return pret;
    }

    public void setPret(double pret) {
        this.pret = pret;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public abstract String getInstructiuniUtilizare();

    @Override
    public String toString(){
        return String.format("Produsul %s cu ID-ul %s si marca %s are pretul %.2f",
                nume, id, marca, pret);
    }

    @Override
    public boolean equals(Object o){
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass()) return false;
        Product p = (Product) o;
        return id.equals(p.getId());
    }

    @Override
    public void applyDiscount(double procentaj){
        if (procentaj >= 1 && procentaj <= 100){
            this.pret -= (procentaj/100) * this.pret;
        }
    }

    @Override
    public int compareTo(Product altProdus) {
        int comparatiePret = Double.compare(this.pret, altProdus.pret);

        if (comparatiePret == 0) {
            int comparatieNume = 0;
            if (this.nume != null && altProdus.nume != null) {
                comparatieNume = this.nume.compareToIgnoreCase(altProdus.nume);
            }

            if (comparatieNume == 0) {

                if (this.id != null && altProdus.id != null) {
                    return this.id.compareToIgnoreCase(altProdus.id);
                }
            }

            return comparatieNume;
        }

        return comparatiePret;
    }
}