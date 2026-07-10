package com.pao.proiect.tonomat.model;

import com.pao.proiect.tonomat.config.DatabaseConnection;
import com.pao.proiect.tonomat.exception.FonduriInsuficienteException;
import com.pao.proiect.tonomat.exception.StocEpuizatException;
import com.pao.proiect.tonomat.service.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {

        // --- SEEDING: Popularea initiala a bazei de date (doar la prima rulare) ---
//        java.util.List<Compartment> verificareRafturi = CompartmentRepository.getInstanta().obtineToateRafturile();
//
//        if (verificareRafturi.isEmpty()) {
//            System.out.println("Baza de date este goala. Se adauga datele initiale...");
//
//            // 1. Creare Produse in Java
//            Drink cola = new Drink("Cola Zero", 5.0, "Coca-Cola", java.time.LocalDate.now().plusMonths(6), true, "Suc acidulat", java.util.Arrays.asList("Apa", "Indulcitor"), 2, 500, "Cirese");
//            Snack bakeRolls = new Snack("Bake Rolls Sare", 6.5, "7Days", java.time.LocalDate.now().plusMonths(3), false, "Sarat", java.util.Arrays.asList("Faina", "Sare", "Ulei"), 450, 80, true);
//            Gadget cablu = new Gadget("Cablu USB-C", 25.0, "Hama", 12, false);
//            Snack vivaPernute = new Snack("Viva pernute", 3.5, "Nestle", java.time.LocalDate.now().plusMonths(3), false, "Dulce", java.util.Arrays.asList("Faina", "Sare", "Margarina", "Cacao", "Lapte de cocos"), 350, 100, true);
//
//            // 2. Salvare Produse in Baza de Date si preluare ID-uri generate
//            int idCola = ProductRepository.getInstanta().adaugaProdus(cola);
//            int idBakeRolls = ProductRepository.getInstanta().adaugaProdus(bakeRolls);
//            int idCablu = ProductRepository.getInstanta().adaugaProdus(cablu);
//            int idVivaPernute = ProductRepository.getInstanta().adaugaProdus(vivaPernute);
//
//            // 3. Salvare Rafturi in Baza de Date (asociate cu produsele create)
//            CompartmentRepository.getInstanta().adaugaRaft(new Compartment("A1", cola, 5, 10), idCola);
//            CompartmentRepository.getInstanta().adaugaRaft(new Compartment("A2", bakeRolls, 1, 10), idBakeRolls);
//            CompartmentRepository.getInstanta().adaugaRaft(new Compartment("B1", cablu, 3, 5), idCablu);
//            CompartmentRepository.getInstanta().adaugaRaft(new Compartment("B2", vivaPernute, 5, 10), idVivaPernute);
//
//            // 4. Salvare Coduri Promo in Baza de Date
//            PromoCodeRepository.getInstanta().adaugaPromoCode(new PromoCode("WINTER50", 50.0));
//            PromoCodeRepository.getInstanta().adaugaPromoCode(new PromoCode("STUDENT20", 20.0));
//
//            System.out.println("Date initiale adaugate cu succes in DB!\n");
//        }

          Administrator admin = new Administrator("Ion Popescu", "parola123", 2);
//
//        DatabaseConnection db = DatabaseConnection.getInstanta();
//        PromoCodeRepository promoRepo = PromoCodeRepository.getInstanta();
//        promoRepo.adaugaPromoCode(new PromoCode("WINTER50", 50.0));
//
//        InventoryService inventory = InventoryService.getInstanta();
//        inventory.adaugaObservator(admin);
//        PaymentService payment = PaymentService.getInstanta(admin);
//        ReportService report = ReportService.getInstance();
//
//        payment.adaugaCodPromo(new PromoCode("STUDENT20", 20.0));
//
//        Drink cola = new Drink("Cola Zero", 5.0, "Coca-Cola", LocalDate.now().plusMonths(6), true, "Suc acidulat", Arrays.asList("Apa", "Indulcitor"), 2, 500, "Cirese");
//        Snack bakeRolls = new Snack("Bake Rolls Sare", 6.5, "7Days", LocalDate.now().plusMonths(3), false, "Sarat", Arrays.asList("Faina", "Sare", "Ulei"), 450, 80, true);
//        Gadget cablu = new Gadget("Cablu USB-C", 25.0, "Hama", 12, false);
//        Snack vivaPernute = new Snack("Viva pernute", 3.5, "Nestie", LocalDate.now().plusMonths(3), false, "Dulce", Arrays.asList("Faina", "Sare", "Margarina", "Cacao", "Lapte de cocos"), 350, 100, true);
//
//        Compartment raftB2 = new Compartment("B2", vivaPernute, 5, 10);
//        inventory.adaugaRaft(raftB2);
//        inventory.adaugaRaft(new Compartment("A2", bakeRolls, 1, 10)); // Stoc aproape gol
//        inventory.adaugaRaft(new Compartment("B1", cablu, 3, 5));
//
//        int idVivaPernute = ProductRepository.getInstanta().adaugaProdus(vivaPernute);
//        CompartmentRepository.getInstanta().adaugaRaft(raftB2, idVivaPernute);
        // Initializare conexiune si servicii
        DatabaseConnection db = DatabaseConnection.getInstanta();
        InventoryService inventory = InventoryService.getInstanta();
        inventory.adaugaObservator(admin);
        PaymentService payment = PaymentService.getInstanta(admin);
        ReportService report = ReportService.getInstance();

        System.out.println("Se sincronizeaza datele cu Baza de Date...");

        // 1. Incarcare Coduri Promo din DB in memoria PaymentService
        java.util.List<PromoCode> coduriDinDB = PromoCodeRepository.getInstanta().obtineToateCodurile();
        for (PromoCode cod : coduriDinDB) {
            payment.adaugaCodPromo(cod);
        }
        System.out.println("-> S-au incarcat " + coduriDinDB.size() + " coduri promo.");

        // 2. Incarcare Produse si Rafturi din DB in memoria InventoryService
        java.util.List<Compartment> rafturiDinDB = CompartmentRepository.getInstanta().obtineToateRafturile();
        for (Compartment raft : rafturiDinDB) {
            inventory.adaugaRaft(raft);
        }
        System.out.println("-> S-au incarcat " + rafturiDinDB.size() + " rafturi cu produse.");

        System.out.println("Sincronizare finalizata cu succes!\n");


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
            System.out.println("5. " + (isAdminLogat ? "Delogare Administrator" : "Logare Administrator"));

            if (isAdminLogat) {
                System.out.println("6. [ADMIN] Afiseaza avertizari stoc");
                System.out.println("7. [ADMIN] Suplimenteaza stoc (Restock)");
                System.out.println("8. [ADMIN] Retrage numerar incasat");
                System.out.println("9. [ADMIN] Genereaza si afiseaza Raportul Zilnic");
                System.out.println("10. [ADMIN] Sterge istoric tranzactii (Delete DB)");
                System.out.println("11. [ADMIN] Actualizeaza pret produs (Update DB)");
                System.out.println("12. [ADMIN] Citeste informatii DB (Produs/Raft/Promo)");
                System.out.println("13. [ADMIN] Sterge elemente DB (Produs/Raft)");
                System.out.println("14. [ADMIN] Modifica/Sterge PromoCode DB");
                System.out.println("15. [ADMIN] Corecteaza/Sterge o tranzactie DB");
            }

            System.out.println("0. Iesire din aplicatie");
            System.out.print("Alege o optiune: ");

            String optiune = scanner.nextLine();

            switch (optiune) {
                case "1":
                    inventory.afiseazaProduseDisponibile();
                    AuditService.getInstanta().scrieActiune("Interogare_Afisare_Produse");
                    break;
                case "2":
                    inventory.afiseazaCatalogSortatDupaPret();
                    AuditService.getInstanta().scrieActiune("Interogare_Catalog_Sortat");
                    break;
                case "3":
                    inventory.filtreazaBauturiFaraZahar();
                    AuditService.getInstanta().scrieActiune("Interogare_Bauturi_Fara_Zahar");
                    break;
                case "4":
                    // --- FLUXUL DE CUMPARARE CU TRY-CATCH ---
                    System.out.println("Tasteaza codul raftului sau numele produsului dorit:");
                    String inputClient = scanner.nextLine();
                    Compartment raft = inventory.obtineRaftDupaCod(inputClient);

                    // 2. Daca nu a scris codul corect, intervine asistentul
                    if (raft == null) {

                        // Cautam produsul dupa cuvinte cheie
                        Compartment sugestie = inventory.cautaRaftAproximativ(inputClient);

                        if (sugestie != null) {
                            // Am gasit ceva asemanator
                            System.out.print("Cred ca te referi la produsul [" + sugestie.getProdus().getNume() + "]. Asta cautai? (Da/Nu): ");
                            String raspuns = scanner.nextLine().trim().toLowerCase();

                            if (raspuns.equals("da") || raspuns.equals("d")) {
                                raft= sugestie; // Confirmare primita
                                System.out.println("Perfect! Continuam procesul pentru " + raft.getProdus().getNume() + "...");
                            } else {
                                // A refuzat sugestia
                                System.out.println("Ne pare rau. Nu am inteles exact ce cauti. Te rugam sa consulti meniul si sa incerci din nou.");
                                break;
                            }
                        } else {
                            // Nu s a gasit nicio potrivire
                            System.out.println("Eroare: Produsul sau codul introdus nu a putut fi recunoscut. Verifica meniul!");
                            break;
                        }
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

                        // --- SISTEM VERIFICARE PIN (3 INCERCARI) ---
                        int incercariRamase = 3;
                        boolean pinValid = false;
                        String pinCorect = "1234";

                        while (incercariRamase > 0) {
                            System.out.print("Introdu codul PIN: ");
                            String pinIntrodus = scanner.nextLine().trim();

                            if (pinIntrodus.equals(pinCorect)) {
                                pinValid = true;
                                break;
                            } else {
                                incercariRamase--;
                                if (incercariRamase > 0) {
                                    System.out.println("PIN incorect. Mai ai " + incercariRamase + " incercari.");
                                }
                            }
                        }

                        if (!pinValid) {
                            System.out.println("Card blocat din cauza a 3 incercari gresite! Tranzactie anulata.");
                            break;
                        }

                        // Daca a trecut de PIN, trecem la procesare
                        metodaPlata = new CardPayment(pretFinal, nrCard, numePosesor);

                    } else {
                        System.out.println("Metoda de plata invalida. Tranzactie anulata.");
                        break;
                    }

                    // --- PENTRU EXCEPȚIILE CUSTOM ---
                    try {
                        //procesam plata - poate arunca FonduriInsuficienteException
                        boolean plataReusita = payment.proceseazaPlata(pretFinal, metodaPlata);

                        if (plataReusita) {
                            //eliberam produsul - poate arunca StocEpuizatException
                            inventory.elibereazaProdus(raft.getCodRaft());

                            //salvam tranzactia
                            Transaction tranzactie = new Transaction(raft.getCodRaft(), raft.getProdus().getNume(), metodaPlata, pretFinal);

                            TransactionRepository.getInstanta().adaugaTranzactie(tranzactie);

                            // --- Scrie in fisierul de audit (Cerinta 2) ---
                            AuditService.getInstanta().scrieActiune("Cumparare_Produs_" + raft.getProdus().getNume());

                            report.inregistreazaTranzactie(tranzactie);
                            System.out.println("Produsul a fost eliberat. Multumim!");
                            AuditService.getInstanta().scrieActiune("Cumparare_" + raft.getProdus().getNume());
                        }
                    } catch (FonduriInsuficienteException e) {
                        // Am prins eroarea de la plata cash insuficienta
                        System.out.println("\n[EROARE PLATA]: " + e.getMessage());
                        System.out.println("Tranzactie anulata. Banii au fost returnati.");
                    } catch (StocEpuizatException e) {
                        // Am prins eroarea de la stoc lipsa
                        System.out.println("\n[EROARE STOC]: " + e.getMessage());
                        System.out.println("Tranzactie anulata. Banii au fost returnati.");
                    } catch (Exception e) {
                        // Catch de siguranta pentru orice altceva (Prevenire Crash general)
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
                    if (isAdminLogat) {
                        inventory.afiseazaStocCritic(3);
                        AuditService.getInstanta().scrieActiune("Interogare_Avertizari_Stoc");
                    } else System.out.println("Acces respins.");
                    break;

                case "7":
                    if (isAdminLogat) {
                        System.out.print("Codul raftului pentru restock (ex: A1): ");
                        String cod = scanner.nextLine().trim();

                        System.out.print("Cantitate de adaugat: ");
                        try {
                            int cantitateAdaugata = Integer.parseInt(scanner.nextLine().trim());
                            inventory.suplimenteazaStoc(cod, cantitateAdaugata);

                            // --- Actualizare in Baza de Date ---
                            Compartment raftActualizat = inventory.obtineRaftDupaCod(cod);
                            if(raftActualizat != null) {
                                CompartmentRepository.getInstanta().actualizeazaStoc(cod, raftActualizat.getCantitate());

                                // Audit
                                AuditService.getInstanta().scrieActiune("Admin_Restock_Raft_" + cod);
                            }
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
                        TransactionRepository.getInstanta().obtineToateTranzactiile();

                        MachineReport raportFinal = report.genereazaRaportZilnic(2);
                        System.out.println(raportFinal.toString());

                        // Audit log
                        AuditService.getInstanta().scrieActiune("Generare_Raport_Admin");
                    } else System.out.println("Acces respins.");
                    break;

                case "10":
                    if (isAdminLogat) {
                        TransactionRepository.getInstanta().stergeIstoric();
                        AuditService.getInstanta().scrieActiune("Admin_Stergere_Istoric_Tranzactii");
                    } else {
                        System.out.println("Acces respins.");
                    }
                    break;

                case "11":
                    if (isAdminLogat) {
                        System.out.print("Introdu ID-ul produsului pentru modificare pret: ");
                        try {
                            int idProd = Integer.parseInt(scanner.nextLine().trim());
                            System.out.print("Introdu noul pret: ");
                            double pretNou = Double.parseDouble(scanner.nextLine().trim());

                            ProductRepository.getInstanta().actualizeazaPretProdus(idProd, pretNou);
                            AuditService.getInstanta().scrieActiune("Admin_Update_Pret_Produs_ID_" + idProd);
                        } catch (NumberFormatException e) {
                            System.out.println("Eroare: Te rog sa introduci valori numerice valide!");
                        }
                    } else {
                        System.out.println("Acces respins.");
                    }
                    break;

                case "12": // READ din toate repository-urile
                    if (isAdminLogat) {
                        System.out.print("ID produs de citit din DB: ");
                        ProductRepository.getInstanta().citesteProdus(Integer.parseInt(scanner.nextLine().trim()));

                        System.out.print("Cod raft de citit din DB: ");
                        CompartmentRepository.getInstanta().citesteRaft(scanner.nextLine().trim());

                        System.out.print("Cod promo de citit din DB: ");
                        PromoCode promo = PromoCodeRepository.getInstanta().citestePromoCode(scanner.nextLine().trim());
                        if(promo != null) {
                            System.out.println("Promo valid. Reducere: " + promo.getProcentReducere() + "%");
                        }

                        AuditService.getInstanta().scrieActiune("Admin_Citire_Date_DB");
                    } else System.out.println("Acces respins.");
                    break;

                case "13": // DELETE pentru Produse si Rafturi
                    if (isAdminLogat) {
                        System.out.print("Cod raft de sters: ");
                        CompartmentRepository.getInstanta().stergeRaft(scanner.nextLine().trim());

                        System.out.print("ID produs de sters: ");
                        ProductRepository.getInstanta().stergeProdus(Integer.parseInt(scanner.nextLine().trim()));

                        AuditService.getInstanta().scrieActiune("Admin_Stergere_Raft_Produs");
                    } else System.out.println("Acces respins.");
                    break;

                case "14": // UPDATE si DELETE pentru PromoCode
                    if (isAdminLogat) {
                        System.out.print("Cod promo vizat: ");
                        String cod = scanner.nextLine().trim();
                        System.out.print("Introdu 1 pt modificare reducere sau 2 pt stergere: ");
                        String optPromo = scanner.nextLine().trim();

                        if(optPromo.equals("1")) {
                            System.out.print("Reducere noua procent: ");
                            PromoCodeRepository.getInstanta().actualizeazaReducere(cod, Double.parseDouble(scanner.nextLine().trim()));
                            AuditService.getInstanta().scrieActiune("Admin_Update_Promo_" + cod);
                        } else if(optPromo.equals("2")) {
                            PromoCodeRepository.getInstanta().stergePromoCode(cod);
                            AuditService.getInstanta().scrieActiune("Admin_Stergere_Promo_" + cod);
                        }
                    } else System.out.println("Acces respins.");
                    break;

                case "15": // UPDATE si DELETE pentru Tranzactii individuale
                    if (isAdminLogat) {
                        System.out.print("ID tranzactie vizata: ");
                        int idTranz = Integer.parseInt(scanner.nextLine().trim());
                        System.out.print("Introdu 1 pt modificare pret sau 2 pt stergere: ");
                        String optTranz = scanner.nextLine().trim();

                        if(optTranz.equals("1")) {
                            System.out.print("Pret nou corectat: ");
                            TransactionRepository.getInstanta().actualizeazaPretTranzactie(idTranz, Double.parseDouble(scanner.nextLine().trim()));
                            AuditService.getInstanta().scrieActiune("Admin_Update_Tranzactie_" + idTranz);
                        } else if(optTranz.equals("2")) {
                            TransactionRepository.getInstanta().stergeTranzactie(idTranz);
                            AuditService.getInstanta().scrieActiune("Admin_Stergere_Tranzactie_" + idTranz);
                        }
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