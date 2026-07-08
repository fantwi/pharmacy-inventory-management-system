package com.pharmacy.ui;

import com.pharmacy.exception.InventoryException;
import com.pharmacy.model.InventoryItem;
import com.pharmacy.model.Medicine;
import com.pharmacy.model.PrescriptionMedicine;
import com.pharmacy.service.InventoryManager;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class MainFrame extends JFrame {
    private final InventoryManager inventoryManager = new InventoryManager();
    private final InventoryTableModel tableModel = new InventoryTableModel();
    private final JTable inventoryTable = new JTable(tableModel);

    private final JTextField idField = new JTextField(12);
    private final JTextField nameField = new JTextField(18);
    private final JTextField manufacturerField = new JTextField(18);
    private final JSpinner quantitySpinner = new JSpinner(new SpinnerNumberModel(0, 0, 100000, 1));
    private final JSpinner reorderSpinner = new JSpinner(new SpinnerNumberModel(5, 0, 100000, 1));
    private final JTextField priceField = new JTextField(10);
    private final JTextField expiryField = new JTextField(10);
    private final JTextField dosageField = new JTextField(10);
    private final JTextField classField = new JTextField(12);
    private final JComboBox<String> typeBox = new JComboBox<>(new String[] {"GENERAL", "PRESCRIPTION"});
    private final JTextField searchField = new JTextField(24);
    private final JLabel summaryLabel = new JLabel();

    private String selectedOriginalId;

    public MainFrame() {
        setTitle("Pharmacy Inventory Management System");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setMinimumSize(new Dimension(1150, 720));
        setLocationRelativeTo(null);

        inventoryManager.seedSampleData();
        buildInterface();
        bindEvents();
        refreshTable();
    }

    private void buildInterface() {
        JPanel root = new JPanel(new BorderLayout(12, 12));
        root.setBorder(new EmptyBorder(16, 16, 16, 16));
        root.setBackground(new Color(247, 249, 251));
        setContentPane(root);

        root.add(createHeader(), BorderLayout.NORTH);
        root.add(createTablePanel(), BorderLayout.CENTER);
        root.add(createFormPanel(), BorderLayout.EAST);
        root.add(createFooter(), BorderLayout.SOUTH);
    }

    private JPanel createHeader() {
        JPanel header = new JPanel(new BorderLayout(12, 8));
        header.setOpaque(false);

        JLabel title = new JLabel("Pharmacy Inventory Management System");
        title.setFont(new Font("Segoe UI", Font.BOLD, 24));
        title.setForeground(new Color(25, 59, 80));
        header.add(title, BorderLayout.WEST);

        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 8, 0));
        searchPanel.setOpaque(false);
        JButton searchButton = new JButton("Search");
        JButton showAllButton = new JButton("Show All");
        JButton lowStockButton = new JButton("Low Stock");
        JButton expiredButton = new JButton("Expired");

        searchButton.addActionListener(event -> searchItems());
        showAllButton.addActionListener(event -> refreshTable());
        lowStockButton.addActionListener(event -> tableModel.setItems(inventoryManager.getLowStockItems()));
        expiredButton.addActionListener(event -> tableModel.setItems(inventoryManager.getExpiredItems()));

        searchPanel.add(new JLabel("Find:"));
        searchPanel.add(searchField);
        searchPanel.add(searchButton);
        searchPanel.add(showAllButton);
        searchPanel.add(lowStockButton);
        searchPanel.add(expiredButton);
        header.add(searchPanel, BorderLayout.EAST);
        return header;
    }

    private JScrollPane createTablePanel() {
        inventoryTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        inventoryTable.setRowHeight(28);
        inventoryTable.setAutoCreateRowSorter(true);
        inventoryTable.setFillsViewportHeight(true);
        inventoryTable.getTableHeader().setFont(new Font("Segoe UI", Font.BOLD, 13));
        inventoryTable.setDefaultRenderer(Object.class, new StatusAwareRenderer());
        JScrollPane scrollPane = new JScrollPane(inventoryTable);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(214, 224, 231)));
        return scrollPane;
    }

    private JPanel createFormPanel() {
        JPanel formWrapper = new JPanel(new BorderLayout(0, 10));
        formWrapper.setPreferredSize(new Dimension(340, 0));
        formWrapper.setBackground(Color.WHITE);
        formWrapper.setBorder(BorderFactory.createCompoundBorder(
                BorderFactory.createLineBorder(new Color(214, 224, 231)),
                new EmptyBorder(14, 14, 14, 14)));

        JLabel formTitle = new JLabel("Medicine Record");
        formTitle.setFont(new Font("Segoe UI", Font.BOLD, 18));
        formWrapper.add(formTitle, BorderLayout.NORTH);

        JPanel form = new JPanel(new GridBagLayout());
        form.setOpaque(false);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(5, 0, 5, 0);
        gbc.fill = GridBagConstraints.HORIZONTAL;
        gbc.weightx = 1;

        int row = 0;
        row = addField(form, gbc, row, "Type", typeBox);
        row = addField(form, gbc, row, "Medicine ID", idField);
        row = addField(form, gbc, row, "Name", nameField);
        row = addField(form, gbc, row, "Manufacturer", manufacturerField);
        row = addField(form, gbc, row, "Quantity", quantitySpinner);
        row = addField(form, gbc, row, "Reorder Level", reorderSpinner);
        row = addField(form, gbc, row, "Unit Price", priceField);
        row = addField(form, gbc, row, "Expiry Date (yyyy-mm-dd)", expiryField);
        row = addField(form, gbc, row, "Dosage", dosageField);
        addField(form, gbc, row, "Prescription Class", classField);
        formWrapper.add(form, BorderLayout.CENTER);

        JPanel actions = new JPanel(new GridBagLayout());
        actions.setOpaque(false);
        GridBagConstraints buttonGbc = new GridBagConstraints();
        buttonGbc.insets = new Insets(4, 4, 4, 4);
        buttonGbc.fill = GridBagConstraints.HORIZONTAL;
        buttonGbc.weightx = 1;

        JButton addButton = new JButton("Add");
        JButton updateButton = new JButton("Update");
        JButton deleteButton = new JButton("Delete");
        JButton clearButton = new JButton("Clear");
        JButton sellButton = new JButton("Sell");
        JButton restockButton = new JButton("Restock");
        JButton saveButton = new JButton("Export CSV");
        JButton loadButton = new JButton("Import CSV");

        addButton.addActionListener(event -> addItem());
        updateButton.addActionListener(event -> updateItem());
        deleteButton.addActionListener(event -> deleteItem());
        clearButton.addActionListener(event -> clearForm());
        sellButton.addActionListener(event -> adjustStock(false));
        restockButton.addActionListener(event -> adjustStock(true));
        saveButton.addActionListener(event -> exportCsv());
        loadButton.addActionListener(event -> importCsv());

        addActionButton(actions, buttonGbc, addButton, 0, 0);
        addActionButton(actions, buttonGbc, updateButton, 1, 0);
        addActionButton(actions, buttonGbc, deleteButton, 0, 1);
        addActionButton(actions, buttonGbc, clearButton, 1, 1);
        addActionButton(actions, buttonGbc, sellButton, 0, 2);
        addActionButton(actions, buttonGbc, restockButton, 1, 2);
        addActionButton(actions, buttonGbc, saveButton, 0, 3);
        addActionButton(actions, buttonGbc, loadButton, 1, 3);
        formWrapper.add(actions, BorderLayout.SOUTH);

        return formWrapper;
    }

    private JPanel createFooter() {
        JPanel footer = new JPanel(new BorderLayout());
        footer.setOpaque(false);
        summaryLabel.setFont(new Font("Segoe UI", Font.BOLD, 13));
        summaryLabel.setHorizontalAlignment(SwingConstants.LEFT);
        footer.add(summaryLabel, BorderLayout.WEST);
        return footer;
    }

    private int addField(JPanel panel, GridBagConstraints gbc, int row, String label, Component field) {
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.gridwidth = 1;
        JLabel fieldLabel = new JLabel(label);
        fieldLabel.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        panel.add(fieldLabel, gbc);

        gbc.gridy = row + 1;
        panel.add(field, gbc);
        return row + 2;
    }

    private void addActionButton(JPanel panel, GridBagConstraints gbc, JButton button, int x, int y) {
        gbc.gridx = x;
        gbc.gridy = y;
        panel.add(button, gbc);
    }

    private void bindEvents() {
        inventoryTable.getSelectionModel().addListSelectionListener(event -> {
            if (!event.getValueIsAdjusting()) {
                populateFormFromSelection();
            }
        });
        typeBox.addActionListener(event -> classField.setEnabled(isPrescriptionSelected()));
        searchField.addActionListener(event -> searchItems());
        classField.setEnabled(false);
    }

    private void addItem() {
        try {
            inventoryManager.addItem(readFormItem());
            refreshTable();
            clearForm();
            showMessage("Medicine added successfully.");
        } catch (InventoryException | DateTimeParseException | NumberFormatException exception) {
            showError(exception.getMessage());
        }
    }

    private void updateItem() {
        if (selectedOriginalId == null) {
            showError("Select a medicine before updating.");
            return;
        }
        try {
            inventoryManager.updateItem(selectedOriginalId, readFormItem());
            refreshTable();
            clearForm();
            showMessage("Medicine updated successfully.");
        } catch (InventoryException | DateTimeParseException | NumberFormatException exception) {
            showError(exception.getMessage());
        }
    }

    private void deleteItem() {
        if (selectedOriginalId == null) {
            showError("Select a medicine before deleting.");
            return;
        }
        int choice = JOptionPane.showConfirmDialog(this,
                "Delete selected medicine?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
        if (choice != JOptionPane.YES_OPTION) {
            return;
        }
        try {
            inventoryManager.removeItem(selectedOriginalId);
            refreshTable();
            clearForm();
            showMessage("Medicine deleted successfully.");
        } catch (InventoryException exception) {
            showError(exception.getMessage());
        }
    }

    private void adjustStock(boolean restock) {
        if (selectedOriginalId == null) {
            showError("Select a medicine before changing stock.");
            return;
        }
        String label = restock ? "Restock quantity" : "Sale quantity";
        String input = JOptionPane.showInputDialog(this, label + ":");
        if (input == null) {
            return;
        }
        try {
            int amount = Integer.parseInt(input.trim());
            if (restock) {
                inventoryManager.restock(selectedOriginalId, amount);
            } else {
                inventoryManager.sell(selectedOriginalId, amount);
            }
            refreshTable();
            populateFormFromItem(inventoryManager.findById(selectedOriginalId).orElse(null));
        } catch (InventoryException | NumberFormatException exception) {
            showError(exception.getMessage());
        }
    }

    private void searchItems() {
        tableModel.setItems(inventoryManager.search(searchField.getText()));
        updateSummary();
    }

    private void refreshTable() {
        tableModel.setItems(inventoryManager.getAllItems());
        updateSummary();
    }

    private void updateSummary() {
        summaryLabel.setText(String.format(
                "Records: %d    Total units: %d    Inventory value: GHS %.2f    Low stock: %d    Expired: %d",
                inventoryManager.getAllItems().size(),
                inventoryManager.getTotalQuantity(),
                inventoryManager.getTotalInventoryValue(),
                inventoryManager.getLowStockItems().size(),
                inventoryManager.getExpiredItems().size()));
    }

    private InventoryItem readFormItem() {
        String type = (String) typeBox.getSelectedItem();
        String id = idField.getText().trim();
        String name = nameField.getText().trim();
        String manufacturer = manufacturerField.getText().trim();
        int quantity = (Integer) quantitySpinner.getValue();
        int reorder = (Integer) reorderSpinner.getValue();
        double price = Double.parseDouble(priceField.getText().trim());
        LocalDate expiryDate = LocalDate.parse(expiryField.getText().trim());
        String dosage = dosageField.getText().trim();
        String controlledClass = classField.getText().trim();
        return InventoryManager.createItem(type, id, name, manufacturer, quantity, reorder,
                price, expiryDate, dosage, controlledClass);
    }

    private void populateFormFromSelection() {
        int selectedRow = inventoryTable.getSelectedRow();
        if (selectedRow < 0) {
            return;
        }
        int modelRow = inventoryTable.convertRowIndexToModel(selectedRow);
        populateFormFromItem(tableModel.getItemAt(modelRow));
    }

    private void populateFormFromItem(InventoryItem item) {
        if (item == null) {
            return;
        }
        selectedOriginalId = item.getId();
        typeBox.setSelectedItem(item instanceof PrescriptionMedicine ? "PRESCRIPTION" : "GENERAL");
        idField.setText(item.getId());
        nameField.setText(item.getName());
        manufacturerField.setText(item.getManufacturer());
        quantitySpinner.setValue(item.getQuantity());
        reorderSpinner.setValue(item.getReorderLevel());
        priceField.setText(String.format("%.2f", item.getUnitPrice()));
        expiryField.setText(item.getExpiryDate().toString());
        dosageField.setText(item instanceof Medicine ? ((Medicine) item).getDosage() : "");
        classField.setText(item instanceof PrescriptionMedicine
                ? ((PrescriptionMedicine) item).getControlledClass()
                : "");
        classField.setEnabled(isPrescriptionSelected());
    }

    private void clearForm() {
        selectedOriginalId = null;
        inventoryTable.clearSelection();
        typeBox.setSelectedItem("GENERAL");
        idField.setText("");
        nameField.setText("");
        manufacturerField.setText("");
        quantitySpinner.setValue(0);
        reorderSpinner.setValue(5);
        priceField.setText("");
        expiryField.setText(LocalDate.now().plusYears(1).toString());
        dosageField.setText("");
        classField.setText("");
        classField.setEnabled(false);
    }

    private boolean isPrescriptionSelected() {
        return "PRESCRIPTION".equals(typeBox.getSelectedItem());
    }

    private void exportCsv() {
        JFileChooser chooser = new JFileChooser();
        chooser.setSelectedFile(new File("pharmacy-inventory.csv"));
        if (chooser.showSaveDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            inventoryManager.saveToCsv(chooser.getSelectedFile().toPath());
            showMessage("Inventory exported successfully.");
        } catch (IOException exception) {
            showError("Could not export CSV: " + exception.getMessage());
        }
    }

    private void importCsv() {
        JFileChooser chooser = new JFileChooser();
        if (chooser.showOpenDialog(this) != JFileChooser.APPROVE_OPTION) {
            return;
        }
        try {
            Path path = chooser.getSelectedFile().toPath();
            inventoryManager.loadFromCsv(path);
            refreshTable();
            clearForm();
            showMessage("Inventory imported successfully.");
        } catch (IOException | InventoryException | RuntimeException exception) {
            showError("Could not import CSV: " + exception.getMessage());
        }
    }

    private void showMessage(String message) {
        JOptionPane.showMessageDialog(this, message, "Pharmacy Inventory", JOptionPane.INFORMATION_MESSAGE);
    }

    private void showError(String message) {
        JOptionPane.showMessageDialog(this, message, "Validation Error", JOptionPane.ERROR_MESSAGE);
    }

    private class StatusAwareRenderer extends DefaultTableCellRenderer {
        @Override
        public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected,
                boolean hasFocus, int row, int column) {
            Component component = super.getTableCellRendererComponent(table, value, isSelected,
                    hasFocus, row, column);
            if (isSelected) {
                return component;
            }
            int modelRow = table.convertRowIndexToModel(row);
            InventoryItem item = tableModel.getItemAt(modelRow);
            if (item == null) {
                component.setBackground(Color.WHITE);
            } else if (item.isExpired()) {
                component.setBackground(new Color(255, 226, 226));
            } else if (item.getQuantity() <= item.getReorderLevel()) {
                component.setBackground(new Color(255, 246, 210));
            } else {
                component.setBackground(Color.WHITE);
            }
            return component;
        }
    }
}
