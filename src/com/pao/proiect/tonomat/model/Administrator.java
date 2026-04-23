package com.pao.proiect.tonomat.model;

public class Administrator {
    private String nume;
    private String idAngajat;
    private String parola;
    private int nivelAcces;
    private static int contorId = 1;

    // Constructor cu parametri
    public Administrator(String nume, String parola, int nivelAcces) {
        this.nume = nume;
        this.idAngajat = "ADM-" + contorId;
        contorId++;
        this.parola = parola;
        this.nivelAcces = nivelAcces;
    }

    // Constructor implicit pentru un admin de baza
    public Administrator() {
        this.nume = "Admin Nou";
        this.idAngajat = "ADM-" + contorId;
        contorId++;
        this.parola = "admin123";
        this.nivelAcces = 1;
    }

    public String getNume() {
        return nume;
    }

    public void setNume(String nume) {
        this.nume = nume;
    }

    public String getIdAngajat() {
        return idAngajat;
    }

    public void setIdAngajat(String idAngajat) {
        this.idAngajat = idAngajat;
    }

    // Metoda pentru validarea parolei
    public boolean verificaParola(String input) {
        return this.parola.equals(input);
    }

    public void setParola(String parola) {
        this.parola = parola;
    }

    public int getNivelAcces() {
        return nivelAcces;
    }

    public void setNivelAcces(int nivelAcces) {
        this.nivelAcces = nivelAcces;
    }

    @Override
    public String toString() {
        return "Admin: " + nume + " (ID: " + idAngajat + ", Nivel Acces: " + nivelAcces + ")";
    }
}