package com.pao.proiect.tonomat.service;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AuditService {
    private static AuditService instanta;
    private final String FILE_PATH = "audit.csv";
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    private AuditService() {}

    public static synchronized AuditService getInstanta() {
        if (instanta == null) {
            instanta = new AuditService();
        }
        return instanta;
    }

    // Metoda care scrie in fisier
    public void scrieActiune(String numeActiune) {

        try (FileWriter writer = new FileWriter(FILE_PATH, true)) {
            String timestamp = LocalDateTime.now().format(formatter);
            writer.write(numeActiune + "," + timestamp + "\n");
        } catch (IOException e) {
            System.out.println("Eroare la scrierea in fisierul de audit: " + e.getMessage());
        }
    }
}