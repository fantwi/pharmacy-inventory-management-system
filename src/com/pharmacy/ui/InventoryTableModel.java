package com.pharmacy.ui;

import com.pharmacy.model.InventoryItem;
import com.pharmacy.model.Medicine;
import com.pharmacy.model.PrescriptionMedicine;

import javax.swing.table.AbstractTableModel;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class InventoryTableModel extends AbstractTableModel {
    private static final DateTimeFormatter DATE_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd");
    private final String[] columns = {
            "ID", "Name", "Category", "Manufacturer", "Qty", "Reorder", "Price",
            "Expiry", "Dosage", "Class", "Status", "Value"
    };
    private List<InventoryItem> items = new ArrayList<>();

    public void setItems(List<InventoryItem> items) {
        this.items = new ArrayList<>(items);
        fireTableDataChanged();
    }

    public InventoryItem getItemAt(int rowIndex) {
        if (rowIndex < 0 || rowIndex >= items.size()) {
            return null;
        }
        return items.get(rowIndex);
    }

    @Override
    public int getRowCount() {
        return items.size();
    }

    @Override
    public int getColumnCount() {
        return columns.length;
    }

    @Override
    public String getColumnName(int column) {
        return columns[column];
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        InventoryItem item = items.get(rowIndex);
        switch (columnIndex) {
            case 0:
                return item.getId();
            case 1:
                return item.getName();
            case 2:
                return item.getCategory();
            case 3:
                return item.getManufacturer();
            case 4:
                return item.getQuantity();
            case 5:
                return item.getReorderLevel();
            case 6:
                return String.format("%.2f", item.getUnitPrice());
            case 7:
                return item.getExpiryDate().format(DATE_FORMAT);
            case 8:
                return item instanceof Medicine ? ((Medicine) item).getDosage() : "";
            case 9:
                return item instanceof PrescriptionMedicine
                        ? ((PrescriptionMedicine) item).getControlledClass()
                        : "-";
            case 10:
                return item.getStockStatus();
            case 11:
                return String.format("%.2f", item.getInventoryValue());
            default:
                return "";
        }
    }
}
