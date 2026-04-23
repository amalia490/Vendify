package com.pao.proiect.tonomat.service;

import com.pao.proiect.tonomat.exception.FonduriInsuficienteException;
import com.pao.proiect.tonomat.model.*;

import java.util.ArrayList;
import java.util.List;

public class PaymentService {
    private static PaymentService instanta;
    private double soldTonomat;

    private List<PromoCode> coduriPromo;

    private Administrator adminCurent;

    private PaymentService(Administrator admin) {
        this.soldTonomat = 0.0;
        this.coduriPromo = new ArrayList<>();
        this.adminCurent = admin;
    }

    public static synchronized PaymentService getInstanta(Administrator admin) {
        if (instanta == null) {
            instanta = new PaymentService(admin);
        }
        return instanta;
    }

    public void adaugaCodPromo(PromoCode cod) {
        if (cod != null) {
            coduriPromo.add(cod);
        }
    }

    // --- ACȚIUNEA 5: Autentificarea administratorului ---
    public boolean autentificareAdmin(String idIntrodus, String parolaIntrodusa) {
        if (adminCurent.getIdAngajat().equals(idIntrodus) && adminCurent.verificaParola(parolaIntrodusa)) {
            System.out.println("Autentificare reușită! Bun venit, " + adminCurent.getNume() + ".");
            return true;
        }
        System.out.println("Eroare: ID sau parolă incorectă!");
        return false;
    }

    // --- ACȚIUNEA 6: Retragerea numerarului de către Admin ---
    // Metoda reseteaza incasarile fizice dupa ce administratorul a ridicat banii
    public void retrageNumerar() {
        if (soldTonomat > 0) {
            System.out.println("Se retrage suma de: " + soldTonomat + " RON.");
            soldTonomat = 0.0; // Resetăm soldul intern al aparatului
            System.out.println("Soldul tonomatului a fost resetat la 0.0 RON.");
        } else {
            System.out.println("Tonomatul este gol, nu există numerar de retras.");
        }
    }

    // --- ACȚIUNEA 7: Aplicarea codului promoțional ---
    public double aplicaCodPromo(String textCod, double pretInitial) {
        for (PromoCode promo : coduriPromo) {

            if (promo.getCode().equalsIgnoreCase(textCod) && promo.isValid()) {
                double pretRedus = promo.calculateDiscountedPrice(pretInitial);
                promo.markAsUsed(); // Îl marcăm ca fiind folosit ("îl ardem")
                System.out.println("Cod aplicat cu succes! Prețul a scăzut de la "
                        + pretInitial + " la " + pretRedus + " RON.");
                return pretRedus;
            }
        }
        System.out.println("Codul promoțional '" + textCod + "' este invalid sau a fost deja folosit.");
        return pretInitial;
    }

    public boolean proceseazaPlata(double pretFinal, PaymentMethod metodaPlata) throws FonduriInsuficienteException {
        boolean plataReusita = metodaPlata.proceseazaPlata();

        if (plataReusita) {
            if (metodaPlata instanceof CashPayment) {
                CashPayment cash = (CashPayment) metodaPlata;
                double sumaIntrodusa = cash.getSuma();

                if (sumaIntrodusa >= pretFinal) {
                    double rest = sumaIntrodusa - pretFinal;
                    soldTonomat += pretFinal;
                    System.out.println("Plată cash acceptată. Rest de plată: " + rest + " RON.");
                    return true;
                } else {
                    throw new FonduriInsuficienteException("Fonduri insuficiente! Ai introdus " + sumaIntrodusa + " RON, dar produsul costă " + pretFinal + " RON.");
                }
            }
            else if (metodaPlata instanceof CardPayment) {
                System.out.println("Plată cu cardul acceptată. Vă mulțumim!");
                return true;
            }
        }
        return false;
    }

    public void eliminaCodPromo(String textCod) {
        coduriPromo.removeIf(c -> c.getCode().equalsIgnoreCase(textCod));
        System.out.println("Codul promoțional " + textCod + " a fost eliminat.");
    }

}