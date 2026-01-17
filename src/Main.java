package com.sortingapp; // Ensure this matches the package of your SortingApp file

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        // This launches the window we designed in SortingApp
        SwingUtilities.invokeLater(() -> {
            new SortingApp().setVisible(true);
        });
    }
}