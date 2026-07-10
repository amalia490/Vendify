package com.pao.proiect.tonomat.service;

import com.pao.proiect.tonomat.config.DatabaseConnection;
import com.pao.proiect.tonomat.model.PromoCode;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PromoCodeRepository {
    private static PromoCodeRepository instanta;
    private Connection connection;

    private PromoCodeRepository() {
        this.connection = DatabaseConnection.getInstanta().getConnection();
    }

    public static synchronized PromoCodeRepository getInstanta() {
        if (instanta == null) {
            instanta = new PromoCodeRepository();
        }
        return instanta;
    }

    // 1. CREATE (Adaugare in baza de date)
    public void adaugaPromoCode(PromoCode promoCode) {
        String sql = "INSERT INTO promo_codes (code, discount_value) VALUES (?, ?)";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, promoCode.getCod());
            statement.setDouble(2, promoCode.getProcentReducere());
            statement.executeUpdate();
            System.out.println("Codul promo a fost salvat in baza de date!");
        } catch (SQLException e) {
            System.out.println("Eroare la adaugare: " + e.getMessage());
        }
    }

    // 2. READ (Citire din baza de date)
    public PromoCode citestePromoCode(String code) {
        String sql = "SELECT * FROM promo_codes WHERE code = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, code);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {

                return new PromoCode(
                        resultSet.getString("code"),
                        resultSet.getDouble("discount_value")
                );
            }
        } catch (SQLException e) {
            System.out.println("Eroare la citire: " + e.getMessage());
        }
        return null; // Daca nu exista in DB
    }

    // 3. UPDATE (Modificare reducere)
    public void actualizeazaReducere(String code, double nouaReducere) {
        String sql = "UPDATE promo_codes SET discount_value = ? WHERE code = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, nouaReducere);
            statement.setString(2, code);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // 4. DELETE (Stergere)
    public void stergePromoCode(String code) {
        String sql = "DELETE FROM promo_codes WHERE code = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, code);
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Metoda pentru sincronizare
    public List<PromoCode> obtineToateCodurile() {
        List<PromoCode> coduri = new ArrayList<>();
        String sql = "SELECT * FROM promo_codes";
        try (PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {
            while (resultSet.next()) {
                coduri.add(new PromoCode(
                        resultSet.getString("code"),
                        resultSet.getDouble("discount_value")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Eroare la citirea codurilor din DB: " + e.getMessage());
        }
        return coduri;
    }
}