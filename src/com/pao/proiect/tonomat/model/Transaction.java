package com.pao.proiect.tonomat.model;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Transaction {
    private final String idTranzactie;
    private final String codRaft;

    private final String numeProdus;
    private final double pretPlatit;

    private PaymentMethod metodaPlata;
    private LocalDateTime dataSiOra;
    private static int numarTranzactie = 1;

    public Transaction(String codRaft, String numeProdus, PaymentMethod metodaPlata, double pretPlatit) {
        this.idTranzactie = "TRZ-" + numarTranzactie;
        numarTranzactie++;
        this.codRaft = codRaft;

        this.numeProdus = numeProdus;
        this.pretPlatit = pretPlatit;
        this.metodaPlata = metodaPlata;
        this.dataSiOra = LocalDateTime.now();
    }

    public String getIdTranzactie()
    {
        return idTranzactie;
    }
    public String getCodRaft()
    {
        return codRaft;
    }
    public LocalDateTime getDataSiOra() { return dataSiOra; }

    public String getNumeProdus() { return numeProdus; }
    public double getPretPlatit() { return pretPlatit; }



    @Override
    public String toString() {
        DateTimeFormatter formatator = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss");
        // Am actualizat afișarea să arate exact ca o chitanță reală
        String tipPlata = (metodaPlata != null) ? metodaPlata.getClass().getSimpleName() : "Necunoscut";

        return String.format("[%s] Tranzactia %s | Produs: %s (Raft %s) | Încasat: %.2f RON | Metoda: %s",
                dataSiOra.format(formatator), idTranzactie, numeProdus, codRaft, pretPlatit, tipPlata);
    }
}