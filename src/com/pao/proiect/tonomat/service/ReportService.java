package com.pao.proiect.tonomat.service;

import com.pao.proiect.tonomat.model.MachineReport;
import com.pao.proiect.tonomat.model.Transaction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReportService {
    private static ReportService instance;
    private List<Transaction> istoricTranzactii;

    private ReportService() {
        this.istoricTranzactii = new ArrayList<>();
    }

    public void inregistreazaTranzactie(Transaction t) {
        if (t != null) {
            istoricTranzactii.add(t);
        }
    }

    public static synchronized ReportService getInstance() {
        if (instance == null) {
            instance = new ReportService();
        }
        return instance;
    }
    //Interogare 11
    public void afiseazaIstoricTranzactii() {
        System.out.println("\n=== ISTORIC TRANZACȚII ZIUA CURENTĂ ===");

        if (istoricTranzactii.isEmpty()) {
            System.out.println("Nu a fost înregistrată nicio vânzare astăzi.");
        } else {
            for (Transaction t : istoricTranzactii) {
                // Presupunem că ai o metodă toString() clară în models.Transaction
                System.out.println(t.toString());
            }
            System.out.println("Total tranzacții procesate: " + istoricTranzactii.size());
        }
        System.out.println("=======================================\n");
    }

    public MachineReport genereazaRaportZilnic(int numarAvertizariStoc) {
        double totalIncasari = 0.0;
        int totalProduseVandute = istoricTranzactii.size();

        Map<String, Integer> frecventaProduse = new HashMap<>();

        for (Transaction t : istoricTranzactii) {
            totalIncasari += t.getPretPlatit();

            String numeProdus = t.getNumeProdus();
            frecventaProduse.put(numeProdus, frecventaProduse.getOrDefault(numeProdus, 0) + 1);
        }

        String celMaiVandutProdus = "Niciun produs vandut";
        int maxVanzari = 0;

        for (Map.Entry<String, Integer> entry : frecventaProduse.entrySet()) {
            if (entry.getValue() > maxVanzari) {
                maxVanzari = entry.getValue();
                celMaiVandutProdus = entry.getKey();
            }
        }

        return new MachineReport(totalIncasari, totalProduseVandute, celMaiVandutProdus, numarAvertizariStoc);
    }
}
