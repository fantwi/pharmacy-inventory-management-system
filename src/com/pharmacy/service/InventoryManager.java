package com.pharmacy.service;

import com.pharmacy.exception.InventoryException;
import com.pharmacy.model.InventoryItem;
import com.pharmacy.model.Medicine;
import com.pharmacy.model.PrescriptionMedicine;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.stream.Collectors;

public class InventoryManager {
    private final List<InventoryItem> items = new ArrayList<>();

    public void addItem(InventoryItem item) throws InventoryException {
        validateItem(item);
        if (findById(item.getId()).isPresent()) {
            throw new InventoryException("Medicine ID already exists.");
        }
        items.add(item);
    }

    public void updateItem(String originalId, InventoryItem updatedItem) throws InventoryException {
        validateItem(updatedItem);
        Optional<InventoryItem> existingWithNewId = findById(updatedItem.getId());
        if (existingWithNewId.isPresent() && !originalId.equalsIgnoreCase(updatedItem.getId())) {
            throw new InventoryException("Another medicine already uses this ID.");
        }
        for (int index = 0; index < items.size(); index++) {
            if (items.get(index).getId().equalsIgnoreCase(originalId)) {
                items.set(index, updatedItem);
                return;
            }
        }
        throw new InventoryException("Selected medicine was not found.");
    }

    public void removeItem(String id) throws InventoryException {
        boolean removed = items.removeIf(item -> item.getId().equalsIgnoreCase(id));
        if (!removed) {
            throw new InventoryException("Medicine was not found.");
        }
    }

    public void restock(String id, int amount) throws InventoryException {
        if (amount <= 0) {
            throw new InventoryException("Restock quantity must be greater than zero.");
        }
        InventoryItem item = findRequired(id);
        item.setQuantity(item.getQuantity() + amount);
    }

    public void sell(String id, int amount) throws InventoryException {
        if (amount <= 0) {
            throw new InventoryException("Sale quantity must be greater than zero.");
        }
        InventoryItem item = findRequired(id);
        if (item.isExpired()) {
            throw new InventoryException("Expired medicine cannot be sold.");
        }
        if (item.getQuantity() < amount) {
            throw new InventoryException("Insufficient stock for this sale.");
        }
        item.setQuantity(item.getQuantity() - amount);
    }

    public Optional<InventoryItem> findById(String id) {
        return items.stream()
                .filter(item -> item.getId().equalsIgnoreCase(id))
                .findFirst();
    }

    public List<InventoryItem> search(String keyword) {
        String query = keyword == null ? "" : keyword.trim().toLowerCase(Locale.ROOT);
        if (query.isEmpty()) {
            return getAllItems();
        }
        return items.stream()
                .filter(item -> item.getId().toLowerCase(Locale.ROOT).contains(query)
                        || item.getName().toLowerCase(Locale.ROOT).contains(query)
                        || item.getManufacturer().toLowerCase(Locale.ROOT).contains(query)
                        || item.getCategory().toLowerCase(Locale.ROOT).contains(query))
                .collect(Collectors.toList());
    }

    public List<InventoryItem> getLowStockItems() {
        return items.stream()
                .filter(item -> item.getQuantity() <= item.getReorderLevel())
                .collect(Collectors.toList());
    }

    public List<InventoryItem> getExpiredItems() {
        return items.stream()
                .filter(InventoryItem::isExpired)
                .collect(Collectors.toList());
    }

    public List<InventoryItem> getAllItems() {
        return Collections.unmodifiableList(items);
    }

    public int getTotalQuantity() {
        return items.stream().mapToInt(InventoryItem::getQuantity).sum();
    }

    public double getTotalInventoryValue() {
        return items.stream().mapToDouble(InventoryItem::getInventoryValue).sum();
    }

    public void seedSampleData() {
        if (!items.isEmpty()) {
            return;
        }
        items.add(new Medicine("MED-001", "Paracetamol", "HealthPlus Labs", 120, 30, 2.50,
                LocalDate.now().plusMonths(18), "500mg"));
        items.add(new Medicine("MED-002", "Cough Syrup", "MediCare Ghana", 18, 20, 12.00,
                LocalDate.now().plusMonths(9), "100ml"));
        items.add(new PrescriptionMedicine("RX-101", "Amoxicillin", "PrimePharm", 55, 15, 8.75,
                LocalDate.now().plusMonths(14), "250mg", "Antibiotic"));
        items.add(new PrescriptionMedicine("RX-205", "Diazepam", "SafeMeds", 8, 10, 21.40,
                LocalDate.now().plusMonths(6), "5mg", "Controlled"));
        items.add(new Medicine("MED-099", "Expired Test Batch", "OldStock Ltd", 4, 10, 1.75,
                LocalDate.now().minusDays(20), "10 tablets"));
    }

    public void saveToCsv(Path file) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(file)) {
            writer.write("type,id,name,manufacturer,quantity,reorderLevel,unitPrice,expiryDate,dosage,controlledClass");
            writer.newLine();
            for (InventoryItem item : items) {
                String type = item instanceof PrescriptionMedicine ? "PRESCRIPTION" : "GENERAL";
                String dosage = item instanceof Medicine ? ((Medicine) item).getDosage() : "";
                String controlledClass = item instanceof PrescriptionMedicine
                        ? ((PrescriptionMedicine) item).getControlledClass()
                        : "";
                writer.write(String.join(",",
                        escape(type),
                        escape(item.getId()),
                        escape(item.getName()),
                        escape(item.getManufacturer()),
                        String.valueOf(item.getQuantity()),
                        String.valueOf(item.getReorderLevel()),
                        String.valueOf(item.getUnitPrice()),
                        item.getExpiryDate().toString(),
                        escape(dosage),
                        escape(controlledClass)));
                writer.newLine();
            }
        }
    }

    public void loadFromCsv(Path file) throws IOException, InventoryException {
        List<InventoryItem> loadedItems = new ArrayList<>();
        try (BufferedReader reader = Files.newBufferedReader(file)) {
            String line = reader.readLine();
            while ((line = reader.readLine()) != null) {
                String[] parts = parseCsvLine(line);
                if (parts.length != 10) {
                    throw new InventoryException("Invalid CSV row: " + line);
                }
                InventoryItem item = createItem(parts[0], parts[1], parts[2], parts[3],
                        Integer.parseInt(parts[4]), Integer.parseInt(parts[5]),
                        Double.parseDouble(parts[6]), LocalDate.parse(parts[7]), parts[8], parts[9]);
                validateItem(item);
                loadedItems.add(item);
            }
        }
        items.clear();
        items.addAll(loadedItems);
    }

    public static InventoryItem createItem(String type, String id, String name, String manufacturer,
            int quantity, int reorderLevel, double unitPrice, LocalDate expiryDate, String dosage,
            String controlledClass) {
        if ("PRESCRIPTION".equalsIgnoreCase(type)) {
            return new PrescriptionMedicine(id, name, manufacturer, quantity, reorderLevel,
                    unitPrice, expiryDate, dosage, controlledClass);
        }
        return new Medicine(id, name, manufacturer, quantity, reorderLevel, unitPrice, expiryDate,
                dosage);
    }

    private InventoryItem findRequired(String id) throws InventoryException {
        return findById(id).orElseThrow(() -> new InventoryException("Medicine was not found."));
    }

    private void validateItem(InventoryItem item) throws InventoryException {
        if (item.getId() == null || item.getId().trim().isEmpty()) {
            throw new InventoryException("Medicine ID is required.");
        }
        if (item.getName() == null || item.getName().trim().isEmpty()) {
            throw new InventoryException("Medicine name is required.");
        }
        if (item.getManufacturer() == null || item.getManufacturer().trim().isEmpty()) {
            throw new InventoryException("Manufacturer is required.");
        }
        if (item.getQuantity() < 0) {
            throw new InventoryException("Quantity cannot be negative.");
        }
        if (item.getReorderLevel() < 0) {
            throw new InventoryException("Reorder level cannot be negative.");
        }
        if (item.getUnitPrice() <= 0) {
            throw new InventoryException("Unit price must be greater than zero.");
        }
        if (item.getExpiryDate() == null) {
            throw new InventoryException("Expiry date is required.");
        }
        if (item instanceof Medicine && ((Medicine) item).getDosage().trim().isEmpty()) {
            throw new InventoryException("Dosage is required.");
        }
        if (item instanceof PrescriptionMedicine
                && ((PrescriptionMedicine) item).getControlledClass().trim().isEmpty()) {
            throw new InventoryException("Controlled class is required for prescription medicine.");
        }
    }

    private String escape(String value) {
        String safeValue = value == null ? "" : value;
        if (safeValue.contains(",") || safeValue.contains("\"")) {
            return "\"" + safeValue.replace("\"", "\"\"") + "\"";
        }
        return safeValue;
    }

    private String[] parseCsvLine(String line) {
        List<String> values = new ArrayList<>();
        StringBuilder current = new StringBuilder();
        boolean inQuotes = false;
        for (int index = 0; index < line.length(); index++) {
            char ch = line.charAt(index);
            if (ch == '"') {
                if (inQuotes && index + 1 < line.length() && line.charAt(index + 1) == '"') {
                    current.append('"');
                    index++;
                } else {
                    inQuotes = !inQuotes;
                }
            } else if (ch == ',' && !inQuotes) {
                values.add(current.toString());
                current.setLength(0);
            } else {
                current.append(ch);
            }
        }
        values.add(current.toString());
        return values.toArray(new String[0]);
    }
}
