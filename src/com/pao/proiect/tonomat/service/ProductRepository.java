package com.pao.proiect.tonomat.service;

import com.pao.proiect.tonomat.config.DatabaseConnection;
import com.pao.proiect.tonomat.model.Drink;
import com.pao.proiect.tonomat.model.Gadget;
import com.pao.proiect.tonomat.model.Product;
import com.pao.proiect.tonomat.model.Snack;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
import java.sql.SQLException;

public class ProductRepository {
    private static ProductRepository instanta;
    private Connection connection;

    private ProductRepository() {
        this.connection = DatabaseConnection.getInstanta().getConnection();
    }

    public static synchronized ProductRepository getInstanta() {
        if (instanta == null) {
            instanta = new ProductRepository();
        }
        return instanta;
    }

    // 1. CREATE - Returneaza ID-ul generat de baza de date
    public int adaugaProdus(Product produs) {
        String sql = "INSERT INTO products (categorie_produs, nume, pret, marca, data_expirare, fara_zahar, tip_aliment, ingrediente, calorii, volum, aroma, greutate_grame, este_vegan, luni_garantie, necesita_baterii) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        int idGenerat = -1;

        try (PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            // 1. Setare campurile comune
            statement.setString(2, produs.getNume());
            statement.setDouble(3, produs.getPret());
            statement.setString(4, produs.getMarca());

            statement.setObject(5, null);
            statement.setObject(6, null);
            statement.setObject(7, null);
            statement.setObject(8, null);
            statement.setObject(9, null);
            statement.setObject(10, null);
            statement.setObject(11, null);
            statement.setObject(12, null);
            statement.setObject(13, null);
            statement.setObject(14, null);
            statement.setObject(15, null);

            // 2. Verificare ce TIP de produs este si completare coloanele specifice
            if (produs instanceof Drink) {
                Drink drink = (Drink) produs; // Facem cast
                statement.setString(1, "Drink");
                statement.setObject(5, drink.getDataExpirare());
                statement.setBoolean(6, drink.esteFaraZahar());
                statement.setString(7, drink.getTip());
                statement.setString(8, String.join(",", drink.getIngrediente()));
                statement.setInt(9, drink.getCalorii());
                statement.setInt(10, drink.getVolum());
                statement.setString(11, drink.getAroma());

            } else if (produs instanceof Snack) {
                Snack snack = (Snack) produs;
                statement.setString(1, "Snack");
                statement.setObject(5, snack.getDataExpirare());
                statement.setBoolean(6, snack.esteFaraZahar());
                statement.setString(7, snack.getTip());
                statement.setString(8, String.join(",", snack.getIngrediente()));
                statement.setInt(9, snack.getCalorii());
                statement.setInt(12, snack.getGreutateGrame());
                statement.setBoolean(13, snack.esteVegan());

            } else if (produs instanceof Gadget) {
                Gadget gadget = (Gadget) produs;
                statement.setString(1, "Gadget");
                statement.setInt(14, gadget.getLuniGarantie());
                statement.setBoolean(15, gadget.necesitaBaterii());
            }

            statement.executeUpdate();

            // Luam ID-ul generat
            ResultSet rs = statement.getGeneratedKeys();
            if (rs.next()) {
                idGenerat = rs.getInt(1);
            }
            System.out.println("Produsul " + produs.getNume() + " a fost salvat in DB!");

        } catch (SQLException e) {
            System.out.println("Eroare la adaugarea produsului in DB: " + e.getMessage());
            e.printStackTrace();
        }
        return idGenerat;
    }

    // 2. DELETE
    public void stergeProdus(int idProdus) {
        String sql = "DELETE FROM products WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idProdus);
            statement.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Eroare la stergere: " + e.getMessage());
        }
    }

    // 3. READ
    public void citesteProdus(int idProdus) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, idProdus);
            ResultSet rs = statement.executeQuery();
            if (rs.next()) {
                System.out.println("Produs gasit: " + rs.getString("nume") + " | Pret: " + rs.getDouble("pret") + " RON");
            } else {
                System.out.println("Produsul nu a fost gasit.");
            }
        } catch (SQLException e) {
            System.out.println("Eroare la citire produs: " + e.getMessage());
        }
    }

    // 4. UPDATE
    public void actualizeazaPretProdus(int idProdus, double pretNou) {
        String sql = "UPDATE products SET pret = ? WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, pretNou);
            statement.setInt(2, idProdus);
            statement.executeUpdate();
            System.out.println("Pret actualizat pt produsul cu ID: " + idProdus);
        } catch (SQLException e) {
            System.out.println("Eroare la update pret produs: " + e.getMessage());
        }
    }

    // Extrage un produs din DB sub forma de obiect
    public Product obtineProdusDupaId(int id) {
        String sql = "SELECT * FROM products WHERE id = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();

            if (rs.next()) {
                String categorie = rs.getString("categorie_produs");
                String nume = rs.getString("nume");
                double pret = rs.getDouble("pret");
                String marca = rs.getString("marca");

                if ("Drink".equals(categorie)) {
                    java.sql.Date sqlDate = rs.getDate("data_expirare");
                    java.time.LocalDate dataExp = (sqlDate != null) ? sqlDate.toLocalDate() : java.time.LocalDate.now();
                    String ingrStr = rs.getString("ingrediente");
                    java.util.List<String> ingrediente = ingrStr != null ? java.util.Arrays.asList(ingrStr.split(",")) : new java.util.ArrayList<>();

                    return new Drink(nume, pret, marca, dataExp,
                            rs.getBoolean("fara_zahar"), rs.getString("tip_aliment"),
                            ingrediente, rs.getInt("calorii"), rs.getInt("volum"),
                            rs.getString("aroma"));

                } else if ("Snack".equals(categorie)) {
                    java.sql.Date sqlDate = rs.getDate("data_expirare");
                    java.time.LocalDate dataExp = (sqlDate != null) ? sqlDate.toLocalDate() : java.time.LocalDate.now();
                    String ingrStr = rs.getString("ingrediente");
                    java.util.List<String> ingrediente = ingrStr != null ? java.util.Arrays.asList(ingrStr.split(",")) : new java.util.ArrayList<>();

                    return new Snack(nume, pret, marca, dataExp,
                            rs.getBoolean("fara_zahar"), rs.getString("tip_aliment"),
                            ingrediente, rs.getInt("calorii"), rs.getInt("greutate_grame"),
                            rs.getBoolean("este_vegan"));

                } else if ("Gadget".equals(categorie)) {
                    return new Gadget(nume, pret, marca,
                            rs.getInt("luni_garantie"), rs.getBoolean("necesita_baterii"));
                }
            }
        } catch (SQLException e) {
            System.out.println("Eroare la reconstructia produsului: " + e.getMessage());
        }
        return null;
    }
}