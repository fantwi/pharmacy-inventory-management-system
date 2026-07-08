package com.pharmacy;

import com.pharmacy.ui.MainFrame;

import javax.swing.SwingUtilities;
import javax.swing.UIManager;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
            } catch (Exception ignored) {
                // The default Swing theme is acceptable if the system theme is unavailable.
            }
            new MainFrame().setVisible(true);
        });
    }
}
