package com.pao.proiect.tonomat.service;

import com.pao.proiect.tonomat.exception.StocEpuizatException;
import com.pao.proiect.tonomat.model.Compartment;
import com.pao.proiect.tonomat.model.Drink;
import com.pao.proiect.tonomat.model.Product;

import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

public class InventoryService {
    private static InventoryService instanta;

    private List<Compartment> rafturi;

    private InventoryService() {
        this.rafturi = new ArrayList<>();
    }

    public static synchronized InventoryService getInstanta() {
        if (instanta == null) {
            instanta = new InventoryService();
        }
        return instanta;
    }

    public void adaugaRaft(Compartment raft) {
        if (raft != null) {
            this.rafturi.add(raft);
        }
    }

    public void suplimenteazaStoc(String codRaft, int cantitateAdaugata) {
        Compartment r = obtineRaftDupaCod(codRaft);
        if (r != null) {
            r.suplimenteazaStoc(cantitateAdaugata);
            System.out.println("Stoc actualizat pentru " + codRaft + ". Noua cantitate: " + r.getCantitate());
        } else {
            System.out.println("Eroare: Raftul " + codRaft + " nu a fost găsit.");
        }
    }

    public void afiseazaMeniuComplet() {
        System.out.println("\n=== MENIU TONOMAT ===");
        for (Compartment r : rafturi) {
            System.out.println(r.toString());
        }
        System.out.println("=====================\n");
    }

    public void afiseazaProduseDisponibile() {
        System.out.println("\n=== PRODUSE DISPONIBILE ===");
        for (Compartment r : rafturi) {
            if (r.getCantitate() > 0 && r.getProdus() != null) {
                System.out.println(r.toString());
            }
        }
    }

    public void afiseazaCatalogSortatDupaPret() {
        TreeSet<Product> produseSortate = new TreeSet<>();

        for (Compartment r : rafturi) {
            if (r.getProdus() != null) {
                produseSortate.add(r.getProdus());
            }
        }

        System.out.println("\n=== CATALOG PRODUSE (Sortat după Preț) ===");
        if (produseSortate.isEmpty()) {
            System.out.println("Nu există produse de afișat.");
        } else {
            for (Product p : produseSortate) {
                System.out.println(p.getNume() + " [ID: " + p.getId() + "] ............... " + p.getPret() + " RON");
            }
        }
        System.out.println("========================================\n");
    }

    public Compartment obtineRaftDupaCod(String codRaft) {
        for (Compartment r : rafturi) {
            if (r.getCodRaft().equalsIgnoreCase(codRaft)) {
                return r;
            }
        }
        return null;
    }

    public void elibereazaProdus(String codRaft) throws StocEpuizatException {
        Compartment r = obtineRaftDupaCod(codRaft);

        if (r == null || r.getCantitate() == 0) {
            throw new StocEpuizatException("Ne pare rău! Produsul de la raftul " + codRaft + " este epuizat sau nu există.");
        }
        r.elibereazaProdus();
    }

    public void filtreazaBauturiFaraZahar() {
        System.out.println("\n=== BĂUTURI FĂRĂ ZAHĂR DISPONIBILE ===");
        for (Compartment r : rafturi) {
            Product p = r.getProdus();
            if (p instanceof Drink d) {
                if (d.esteFaraZahar()) {
                    System.out.println("[" + r.getCodRaft() + "] " + d.getNume() + " - " + d.getPret() + " RON");
                }
            }
        }
    }

    public void afiseazaStocCritic(int limitaMinima) {
        System.out.println("\n=== AVERTIZĂRI STOC CRITIC (< " + limitaMinima + " buc) ===");
        boolean gasitAvertizari = false;

        for (Compartment r : rafturi) {
            if (r.getCantitate() < limitaMinima) {
                System.out.println("ALERTA: Raftul [" + r.getCodRaft() + "] mai are doar " + r.getCantitate() + " buc!");
                gasitAvertizari = true;
            }
        }

        if (!gasitAvertizari) {
            System.out.println("Toate rafturile sunt bine aprovizionate.");
        }
    }

    public void stergeRaft(String codRaft) {
        boolean eliminat = rafturi.removeIf(r -> r.getCodRaft().equalsIgnoreCase(codRaft));
        if (eliminat) {
            System.out.println("Raftul " + codRaft + " a fost șters din sistem.");
        } else {
            System.out.println("Eroare: Raftul " + codRaft + " nu a fost găsit.");
        }
    }
}