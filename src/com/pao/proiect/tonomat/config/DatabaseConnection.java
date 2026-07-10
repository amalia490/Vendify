package com.pao.proiect.tonomat.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instanta;
    private Connection connection;

    // Conectarea catre baza de date tonomat_db, am folosit TablePlus
    private static final String URL = "jdbc:mysql://127.0.0.1:3306/tonomat_db";
    private static final String USER = "root";
    private static final String PASSWORD = "root";

    private DatabaseConnection() {
        try {
            // Aceasta linie activeaza driver-ul (fisierul .jar)
            Class.forName("com.mysql.cj.jdbc.Driver");

            // conectare la baza
            this.connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("CONEXIUNE DB REUSITA!");
        } catch (ClassNotFoundException e) {
            System.out.println("Eroare: Nu s-a gasit driver-ul MySQL. Asigura-te ca ai pus fisierul .jar in Project Structure!");
            e.printStackTrace();
        } catch (SQLException e) {
            System.out.println("Eroare la conectarea cu baza de date: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public static synchronized DatabaseConnection getInstanta() {
        if (instanta == null) {
            instanta = new DatabaseConnection();
        }
        return instanta;
    }

    public Connection getConnection() {
        return connection;
    }
}