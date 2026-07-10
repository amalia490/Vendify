package com.pao.proiect.tonomat.service;

import com.pao.proiect.tonomat.config.DatabaseConnection;
import com.pao.proiect.tonomat.model.Compartment;
import com.pao.proiect.tonomat.model.Product;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class CompartmentRepository {
    private static CompartmentRepository instanta;
    private Connection connection;

    private CompartmentRepository() {
        this.connection = DatabaseConnection.getInstanta().getConnection();
    }

    public static synchronized CompartmentRepository getInstanta() {
        if (instanta == null) {
            instanta = new CompartmentRepository();
        }
        return instanta;
    }

    // 1. CREATE
    public void adaugaRaft(Compartment raft, int idProdus) {
        String sql = "INSERT INTO compartments (cod_raft, id_produs, cantitate, capacitate_maxima) VALUES (?, ?, ?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, raft.getCodRaft());
            statement.setInt(2, idProdus);
            statement.setInt(3, raft.getCantitate());
            statement.setInt(4, raft.getCapacitateMaxima());

            statement.executeUpdate();
            System.out.println("Raftul " + raft.getCodRaft() + " a fost salvat in DB!");
        } catch (SQLException e) {
            System.out.println("Eroare la salvarea raftului: " + e.getMessage());
        }
    }

    // 2. UPDATE (Actualizarea stocului)
    public void actualizeazaStoc(String codRaft, int nouaCantitate) {
        String sql = "UPDATE compartments SET cantitate = ? WHERE cod_raft = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, nouaCantitate);
            statement.setString(2, codRaft);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la update stoc: " + e.getMessage());
        }
    }

    // 3. READ
    public void citesteRaft(String codRaft) {
        String sql = "SELECT * FROM compartments WHERE cod_raft = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codRaft);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                System.out.println("Raft: " + rs.getString("cod_raft") + " | Cantitate: " + rs.getInt("cantitate") + " | Capacitate: " + rs.getInt("capacitate_maxima"));
            } else {
                System.out.println("Raftul nu a fost gasit in DB.");
            }
        } catch (SQLException e) {
            System.out.println("Eroare la citire raft: " + e.getMessage());
        }
    }

    // 4. DELETE
    public void stergeRaft(String codRaft) {
        String sql = "DELETE FROM compartments WHERE cod_raft = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, codRaft);
            statement.executeUpdate();
            System.out.println("Raftul " + codRaft + " a fost sters din DB.");
        } catch (SQLException e) {
            System.out.println("Eroare la stergere raft: " + e.getMessage());
        }
    }

    // Extrage toate rafturile si produsele lor pt sincronizare
    public java.util.List<Compartment> obtineToateRafturile() {
        java.util.List<Compartment> rafturi = new java.util.ArrayList<>();
        String sql = "SELECT * FROM compartments";

        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet rs = statement.executeQuery()) {

            while (rs.next()) {
                String codRaft = rs.getString("cod_raft");
                int idProdus = rs.getInt("id_produs");
                int cantitate = rs.getInt("cantitate");
                int capacitate = rs.getInt("capacitate_maxima");

                // Apelam celalalt repository pt a aduce produsul
                Product produs = ProductRepository.getInstanta().obtineProdusDupaId(idProdus);

                if (produs != null) {
                    rafturi.add(new Compartment(codRaft, produs, cantitate, capacitate));
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare la incarcarea rafturilor: " + e.getMessage());
        }
        return rafturi;
    }
}