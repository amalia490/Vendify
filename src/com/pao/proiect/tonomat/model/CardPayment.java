package com.pao.proiect.tonomat.model;

import java.util.Random;

public class CardPayment extends PaymentMethod {
    private String numarCard;
    private String numeTitular;

    // Constructor pentru initializarea datelor de pe card
    public CardPayment(double suma, String numarCard, String numeTitular) {
        super(suma);
        this.numarCard = numarCard;
        this.numeTitular = numeTitular;
    }

    public CardPayment() {
        super();
        this.numarCard = "nespecificat";
        this.numeTitular = "nespecificat";
    }

    // Returneaza o clona a obiectului curent
    @Override
    public PaymentMethod duplica() {
        CardPayment copie = new CardPayment(this.suma, this.numarCard, this.numeTitular);
        copie.esteReusita = this.esteReusita;
        return copie;
    }

    // Simuleaza procesarea platii
    @Override
    public boolean proceseazaPlata() {
        System.out.println("\n💳 [SISTEM BANCAR] Conectare la banca emitenta pentru cardul " + numarCard + "...");

        try {
            // Pasul 1: Simulam timpul de conectare
            System.out.print("Se proceseaza");
            for (int i = 0; i < 4; i++) {
                Thread.sleep(800); // Pune pauza 0.8 secunde per punct
                System.out.print(".");
            }
            System.out.println("\nConexiune securizata stabilita.");

            // Pasul 2: Verificarea fondurilor
            System.out.println("Se verifica disponibilitatea fondurilor...");
            Thread.sleep(2000); // Pauza 2 secunde

            // Pasul 3: Factorul de risc
            Random rand = new Random();
            if (rand.nextInt(10) == 0) {
                System.out.println("[EROARE] Timeout: Serverul bancii nu raspunde. Incearca din nou.");
                this.esteReusita = false;
                return false;
            }

            System.out.println(" Plata autorizata! Suma de " + this.suma + " RON a fost achitata.");
            this.esteReusita = true;

        } catch (InterruptedException e) {
            System.out.println("Tranzactie intrerupta fortat.");
            this.esteReusita = false;
        }

        return this.esteReusita;
    }
}