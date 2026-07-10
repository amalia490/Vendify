package com.pao.proiect.tonomat.service;

import com.pao.proiect.tonomat.config.DatabaseConnection;
import com.pao.proiect.tonomat.model.Transaction;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class TransactionRepository {
    private static TransactionRepository instanta;
    private Connection connection;

    private TransactionRepository() {
        // Luam conexiunea deja existenta
        this.connection = DatabaseConnection.getInstanta().getConnection();
    }

    public static synchronized TransactionRepository getInstanta() {
        if (instanta == null) {
            instanta = new TransactionRepository();
        }
        return instanta;
    }

    // 1. CREATE (Salvarea unei tranzactii noi in DB)
    public void adaugaTranzactie(Transaction tranzactie) {
        String sql = "INSERT INTO transactions (cod_raft, nume_produs, metoda_plata, pret_final) VALUES (?, ?, ?, ?)";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, tranzactie.getCodRaft());
            statement.setString(2, tranzactie.getNumeProdus());
            // Salvam numele clasei pentru a sti daca a fost Cash sau Card (ex: "CashPayment")
            statement.setString(3, tranzactie.getMetodaPlata().getClass().getSimpleName());
            statement.setDouble(4, tranzactie.getPretPlatit());

            statement.executeUpdate();
            // Nu mai punem System.out.println aici ca sa nu incarcam consola clientului
        } catch (SQLException e) {
            System.out.println("Eroare la salvarea tranzactiei in DB: " + e.getMessage());
        }
    }

    // 2. READ (Extragerea tuturor tranzactiilor pentru raportul de Admin)
    public List<Transaction> obtineToateTranzactiile() {
        List<Transaction> istoric = new ArrayList<>();
        String sql = "SELECT * FROM transactions";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet resultSet = statement.executeQuery();

            while (resultSet.next()) {
                // Afisam direct in consola informatiile pe care le scoatem din DB
                System.out.println("ID: " + resultSet.getInt("id") +
                        " | Raft: " + resultSet.getString("cod_raft") +
                        " | Produs: " + resultSet.getString("nume_produs") +
                        " | Plata: " + resultSet.getString("metoda_plata") +
                        " | Pret: " + resultSet.getDouble("pret_final") + " RON" +
                        " | Data: " + resultSet.getString("data_tranzactie"));
            }
        } catch (SQLException e) {
            System.out.println("Eroare la citirea tranzactiilor din DB: " + e.getMessage());
        }
        return istoric;
    }

    // 3. DELETE (Pentru situatia in care adminul vrea sa stearga istoricul vechi)
    public void stergeIstoric() {
        String sql = "DELETE FROM transactions";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.executeUpdate();
            System.out.println("Istoricul tranzactiilor a fost sters din baza de date.");
        } catch (SQLException e) {
            System.out.println("Eroare la stergere: " + e.getMessage());
        }
    }

    // 4. UPDATE
    public void actualizeazaPretTranzactie(int idTranzactie, double pretNou) {
        String sql = "UPDATE transactions SET pret_final = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, pretNou);
            statement.setInt(2, idTranzactie);
            statement.executeUpdate();
            System.out.println("Tranzactia " + idTranzactie + " a fost actualizata.");
        } catch (SQLException e) {
            System.out.println("Eroare la update tranzactie: " + e.getMessage());
        }
    }

    // 5. DELETE dupa id
    public void stergeTranzactie(int idTranzactie) {
        String sql = "DELETE FROM transactions WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idTranzactie);
            statement.executeUpdate();
            System.out.println("Tranzactia " + idTranzactie + " a fost stearsa.");
        } catch (SQLException e) {
            System.out.println("Eroare la stergere tranzactie: " + e.getMessage());
        }
    }
}