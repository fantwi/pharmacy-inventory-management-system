package com.pharmacy.model;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

public abstract class InventoryItem {
    private String id;
    private String name;
    private String manufacturer;
    private int quantity;
    private int reorderLevel;
    private double unitPrice;
    private LocalDate expiryDate;

    protected InventoryItem(String id, String name, String manufacturer, int quantity,
            int reorderLevel, double unitPrice, LocalDate expiryDate) {
        this.id = id;
        this.name = name;
        this.manufacturer = manufacturer;
        this.quantity = quantity;
        this.reorderLevel = reorderLevel;
        this.unitPrice = unitPrice;
        this.expiryDate = expiryDate;
    }

    public abstract String getCategory();

    public String getStockStatus() {
        if (isExpired()) {
            return "Expired";
        }
        if (quantity == 0) {
            return "Out of Stock";
        }
        if (quantity <= reorderLevel) {
            return "Low Stock";
        }
        return "Available";
    }

    public boolean isExpired() {
        return expiryDate.isBefore(LocalDate.now());
    }

    public long daysUntilExpiry() {
        return ChronoUnit.DAYS.between(LocalDate.now(), expiryDate);
    }

    public double getInventoryValue() {
        return quantity * unitPrice;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getReorderLevel() {
        return reorderLevel;
    }

    public void setReorderLevel(int reorderLevel) {
        this.reorderLevel = reorderLevel;
    }

    public double getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(double unitPrice) {
        this.unitPrice = unitPrice;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }
}
