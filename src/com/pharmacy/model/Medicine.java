package com.pharmacy.model;

import java.time.LocalDate;

public class Medicine extends InventoryItem {
    private String dosage;

    public Medicine(String id, String name, String manufacturer, int quantity, int reorderLevel,
            double unitPrice, LocalDate expiryDate, String dosage) {
        super(id, name, manufacturer, quantity, reorderLevel, unitPrice, expiryDate);
        this.dosage = dosage;
    }

    @Override
    public String getCategory() {
        return "General Medicine";
    }

    public String getDosage() {
        return dosage;
    }

    public void setDosage(String dosage) {
        this.dosage = dosage;
    }
}