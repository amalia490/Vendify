package com.pao.proiect.tonomat.model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public abstract class Eatable extends Product {
    protected boolean faraZahar;
    protected String tip;
    protected List<String> ingrediente;
    protected LocalDate dataExpirare;
    protected int calorii;
    private String categorieSanatate;

    public Eatable(String nume, double pret, String marca,
                   LocalDate dataExpirare, boolean faraZahar, String tip,
                   List<String> ingrediente, int calorii){
        super(nume, pret, marca);

        this.dataExpirare = dataExpirare;
        this.faraZahar = faraZahar;
        this.tip = tip;
        this.ingrediente = ingrediente;
        this.calorii = calorii;

        actualizeazaCategorieSanatate();
    }

    public Eatable(){
        super();

        this.dataExpirare = LocalDate.now();
        this.faraZahar = false;
        this.tip = "nespecificat";
        this.ingrediente = new ArrayList<>();
        this.calorii = 0;

        actualizeazaCategorieSanatate();
    }

    public String getCategorieSanatate(){
        return categorieSanatate;
    }

    public int getCalorii() {
        return calorii;
    }

    public void setCalorii(int calorii) {
        this.calorii = calorii;
        actualizeazaCategorieSanatate();
    }

    public List<String> getIngrediente() {
        return new ArrayList<>(this.ingrediente);
    }

    public void setIngrediente(List<String> ingrediente) {
        this.ingrediente = ingrediente;
        actualizeazaCategorieSanatate();
    }

    public boolean esteFaraZahar() {
        return faraZahar;
    }

    public void setFaraZahar(boolean faraZahar) {
        this.faraZahar = faraZahar;
        actualizeazaCategorieSanatate();
    }

    public String getTip() {
        return tip;
    }

    public void setTip(String tip) {
        this.tip = tip;
    }

    public LocalDate getDataExpirare() {
        return dataExpirare;
    }

    public void setDataExpirare(LocalDate dataExpirare) {
        this.dataExpirare = dataExpirare;
    }

    private void actualizeazaCategorieSanatate(){
        int punctePenalizare = 0;

        if (calorii > 300)
            punctePenalizare += 2;
        else if (calorii > 150)
            punctePenalizare += 1;
        if (!faraZahar)
            punctePenalizare += 2;

        if (ingrediente != null) {
            for (String ing : ingrediente) {
                String ingMic = ing.toLowerCase();
                if (ingMic.contains("e-uri") || ingMic.contains("conservant") || ingMic.contains("colorant")) punctePenalizare += 2;
                if (ingMic.contains("ulei de palmier")) punctePenalizare += 1;
            }
        }

        if (punctePenalizare == 0)
            this.categorieSanatate = "Excelent (Sanatos)";
        else if (punctePenalizare <= 3)
            this.categorieSanatate = "Moderat (Consum rezonabil)";
        else this.categorieSanatate = "Atentie (Nesanatos)";

    }

    public void adaugaIngredient(String ingredientNou) {
        if (ingredientNou != null && !ingredientNou.trim().isEmpty()) {
            this.ingrediente.add(ingredientNou);

            actualizeazaCategorieSanatate();
        }
    }
}