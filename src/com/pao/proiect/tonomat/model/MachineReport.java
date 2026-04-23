package com.pao.proiect.tonomat.model;

import java.time.LocalDate;

public final class MachineReport {
    private final LocalDate dataRaport;
    private final double venitTotal;
    private final int totalProduseVandute;
    private final String celMaiVandutProdus;
    private final int avertizariStoc;

    public MachineReport(double venitTotal, int totalProduseVandute, String celMaiVandutProdus, int avertizariStoc) {
        this.dataRaport = LocalDate.now();
        this.venitTotal = venitTotal;
        this.totalProduseVandute = totalProduseVandute;
        this.celMaiVandutProdus = celMaiVandutProdus;
        this.avertizariStoc = avertizariStoc;
    }

    public LocalDate getDataRaport() {
        return dataRaport;
    }

    public double getVenitTotal() {
        return venitTotal;
    }

    public int getTotalProduseVandute() {
        return totalProduseVandute;
    }

    public String getCelMaiVandutProdus() {
        return celMaiVandutProdus;
    }

    public int getAvertizariStoc() {
        return avertizariStoc;
    }

    @Override
    public String toString() {
        return String.format(
                "=== RAPORT ZILNIC: %s ===\nIncasari: %.2f RON\nProduse vandute: %d buc.\nCel mai vandut: %s\nAvertizari stoc: %d rafturi",
                dataRaport, venitTotal, totalProduseVandute, celMaiVandutProdus, avertizariStoc
        );
    }
}