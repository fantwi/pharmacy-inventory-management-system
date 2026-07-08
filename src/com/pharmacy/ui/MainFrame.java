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

}
