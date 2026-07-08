package com.pharmacy.model;

import java.time.LocalDate;

public class PrescriptionMedicine extends Medicine {
    private String controlledClass;

    public PrescriptionMedicine(String id, String name, String manufacturer, int quantity,
            int reorderLevel, double unitPrice, LocalDate expiryDate, String dosage,
            String controlledClass) {
        super(id, name, manufacturer, quantity, reorderLevel, unitPrice, expiryDate, dosage);
        this.controlledClass = controlledClass;
    }

    @Override
    public String getCategory() {
        return "Prescription Medicine";
    }

    public String getControlledClass() {
        return controlledClass;
    }

    public void setControlledClass(String controlledClass) {
        this.controlledClass = controlledClass;
    }
}
