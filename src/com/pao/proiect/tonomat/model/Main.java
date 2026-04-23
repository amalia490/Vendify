package com.pao.proiect.tonomat.model;

import com.pao.proiect.tonomat.exception.FonduriInsuficienteException;
import com.pao.proiect.tonomat.exception.StocEpuizatException;
import com.pao.proiect.tonomat.service.InventoryService;
import com.pao.proiect.tonomat.service.PaymentService;
import com.pao.proiect.tonomat.service.ReportService;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        Administrator admin = new Administrator("Ion Popescu", "parola123", 2);

        InventoryService inventory = InventoryService.getInstanta();
        PaymentService payment = PaymentService.getInstanta(admin);
        ReportService report = ReportService.getInstance();

        payment.adaugaCodPromo(new PromoCode("STUDENT20", 20.0));

        Drink cola = new Drink("Cola Zero", 5.0, "Coca-Cola", LocalDate.now().plusMonths(6), true, "Suc acidulat", Arrays.asList("Apa", "Indulcitor"), 2, 500, "Cirese");
        Snack bakeRolls = new Snack("Bake Rolls Sare", 6.5, "7Days", LocalDate.now().plusMonths(3), false, "models.Snack Sarat", Arrays.asList("Faina", "Sare", "Ulei"), 450, 80, true);
        Gadget cablu = new Gadget("Cablu USB-C", 25.0, "Hama", 12, false);

        inventory.adaugaRaft(new Compartment("A1", cola, 5, 10));
        inventory.adaugaRaft(new Compartment("A2", bakeRolls, 1, 10)); // Stoc aproape gol pentru a testa avertizarea
        inventory.adaugaRaft(new Compartment("B1", cablu, 3, 5));

        Scanner scanner = new Scanner(System.in);
        boolean ruleaza = true;
        boolean isAdminLogat = false;

        System.out.println("====== BINE ATI VENIT LA TONOMATUL INTELIGENT ======");

        while (ruleaza) {
            System.out.println("\n--- MENIU PRINCIPAL ---");
            System.out.println("1. Afiseaza produse disponibile");
            System.out.println("2. Afiseaza catalog sortat (dupa pret)");
            System.out.println("3. Filtreaza bauturi fara zahar");
            System.out.println("4. Cumpara produs");
            System.out.println("5. " + (isAdminLogat ? "Delogare models.Administrator" : "Logare models.Administrator"));

            if (isAdminLogat) {
                System.out.println("6. [ADMIN] Afiseaza avertizari stoc");
                System.out.println("7. [ADMIN] Suplimenteaza stoc (Restock)");
                System.out.println("8. [ADMIN] Retrage numerar incasat");
                System.out.println("9. [ADMIN] Genereaza si afiseaza Raportul Zilnic");
            }

            System.out.println("0. Iesire din aplicatie");
            System.out.print("Alege o optiune: ");

            String optiune = scanner.nextLine();

            switch (optiune) {
                case "1":
                    inventory.afiseazaProduseDisponibile();
                    break;
                case "2":
                    inventory.afiseazaCatalogSortatDupaPret();
                    break;
                case "3":
                    inventory.filtreazaBauturiFaraZahar();
                    break;
                case "4":
                    // --- FLUXUL DE CUMPĂRARE CU TRY-CATCH ---
                    System.out.print("Introdu codul raftului (ex: A1): ");
                    String codSlot = scanner.nextLine().toUpperCase().trim();
                    Compartment raft = inventory.obtineRaftDupaCod(codSlot);

                    if (raft == null) {
                        System.out.println("Eroare: Raftul nu exista!");
                        break;
                    }

                    double pretInitial = raft.getProdus().getPret();
                    System.out.println("Produs ales: " + raft.getProdus().getNume() + " | Pret: " + pretInitial + " RON");

                    // Promo Code
                    System.out.print("Ai un cod promotional? (Introdu codul sau lasa gol): ");
                    String codPromo = scanner.nextLine().trim();
                    double pretFinal = pretInitial;
                    if (!codPromo.isEmpty()) {
                        pretFinal = payment.aplicaCodPromo(codPromo, pretInitial);
                    }

                    // Alegere Metoda Plata
                    System.out.println("Metoda de plata: 1. Cash | 2. Card");
                    String tipPlata = scanner.nextLine().trim();
                    PaymentMethod metodaPlata = null;

                    if (tipPlata.equals("1")) {
                        System.out.print("Introdu suma in numerar: ");
                        try {
                            double sumaNumerar = Double.parseDouble(scanner.nextLine().trim());
                            metodaPlata = new CashPayment(pretFinal, sumaNumerar);
                        } catch (NumberFormatException e) {
                            System.out.println("Eroare: Te rog introdu o suma valida cu cifre!");
                            break;
                        }
                    } else if (tipPlata.equals("2")) {
                        System.out.print("Introdu numarul cardului: ");
                        String nrCard = scanner.nextLine().trim();
                        System.out.print("Numele de pe card: ");
                        String numePosesor = scanner.nextLine().trim();
                        metodaPlata = new CardPayment(pretFinal, nrCard, numePosesor);
                    } else {
                        System.out.println("Metoda de plata invalida. Tranzactie anulata.");
                        break;
                    }

                    // --- AICI ESTE HANDLE-UL PENTRU EXCEPȚIILE CUSTOM ---
                    try {
                        // 1. Încercăm să procesăm plata (poate arunca FonduriInsuficienteException)
                        boolean plataReusita = payment.proceseazaPlata(pretFinal, metodaPlata);

                        if (plataReusita) {
                            // 2. Încercăm să eliberăm produsul (poate arunca StocEpuizatException)
                            inventory.elibereazaProdus(codSlot);

                            // 3. Dacă totul a mers bine, salvăm tranzacția
                            Transaction tranzactie = new Transaction(codSlot, raft.getProdus().getNume(), metodaPlata, pretFinal);
                            report.inregistreazaTranzactie(tranzactie);
                            System.out.println("Produsul a fost eliberat. Multumim!");
                        }
                    } catch (FonduriInsuficienteException e) {
                        // Am prins eroarea de la plată cash insuficientă
                        System.out.println("\n[EROARE PLATA]: " + e.getMessage());
                        System.out.println("Tranzactie anulata. Banii au fost returnati.");
                    } catch (StocEpuizatException e) {
                        // Am prins eroarea de la stoc lipsă
                        System.out.println("\n[EROARE STOC]: " + e.getMessage());
                        System.out.println("Tranzactie anulata. Banii au fost returnati.");
                    } catch (Exception e) {
                        // Catch de siguranță pentru orice altceva (Prevenire Crash general)
                        System.out.println("\n[EROARE SISTEM]: A aparut o problema neasteptata: " + e.getMessage());
                    }
                    break;

                case "5":
                    if (isAdminLogat) {
                        isAdminLogat = false;
                        System.out.println("Delogare cu succes.");
                    } else {
                        System.out.print("ID Angajat (ex: ADM-1): ");
                        String idAdmin = scanner.nextLine();
                        System.out.print("Parola: ");
                        String parolaAdmin = scanner.nextLine();
                        isAdminLogat = payment.autentificareAdmin(idAdmin, parolaAdmin);
                    }
                    break;

                case "6":
                    if (isAdminLogat) inventory.afiseazaStocCritic(3);
                    else System.out.println("Acces respins.");
                    break;

                case "7":
                    if (isAdminLogat) {
                        System.out.print("Codul raftului pentru restock (ex: A1): ");
                        String cod = scanner.nextLine().trim();

                        System.out.print("Cantitate de adaugat: ");
                        try {
                            int cantitate = Integer.parseInt(scanner.nextLine().trim());
                            inventory.suplimenteazaStoc(cod, cantitate);
                        } catch (NumberFormatException e) {
                            System.out.println("Eroare: Te rog sa introduci un numar valid pentru cantitate!");
                        }
                    } else {
                        System.out.println("Acces respins. Trebuie sa fii logat ca Admin.");
                    }
                    break;
                case "8":
                    if (isAdminLogat) payment.retrageNumerar();
                    else System.out.println("Acces respins.");
                    break;

                case "9":
                    if (isAdminLogat) {
                        report.afiseazaIstoricTranzactii();

                        MachineReport raportFinal = report.genereazaRaportZilnic(2);
                        System.out.println(raportFinal.toString());
                    } else System.out.println("Acces respins.");
                    break;

                case "0":
                    System.out.println("Tonomatul se inchide. La revedere!");
                    ruleaza = false;
                    break;

                default:
                    System.out.println("Optiune invalida!");
                    break;
            }
        }
        scanner.close();
    }
}