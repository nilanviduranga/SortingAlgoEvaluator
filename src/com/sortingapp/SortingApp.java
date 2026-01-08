package com.sortingapp;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

public class SortingApp extends JFrame {
    //GUI Components
    private JButton uploadButton;
    private JButton sortButton;
    private JComboBox<String> columnSelector; // To select CSV column
    private JComboBox<String> algoSelector;   // To select Algorithm
    private JTextArea resultArea;             // To show performance results
    private JLabel statusLabel;

    public SortingApp() {
        // 1. Setup the Window
        setTitle("Sorting Algorithm Performance Evaluator");
        setSize(600, 500);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(10, 10)); // Layout manager

        // 2. Top Panel: Controls
        JPanel topPanel = new JPanel();
        topPanel.setLayout(new GridLayout(3, 2, 20, 20)); // Grid layout for neatness

        uploadButton = new JButton("Upload CSV File");
        statusLabel = new JLabel("No file loaded");

        columnSelector = new JComboBox<>(); // Empty for now, filled after upload
        columnSelector.addItem("Select Column...");



        sortButton = new JButton("Run Sort & Evaluate");

        // Add components to Top Panel
        topPanel.add(statusLabel);
        topPanel.add(uploadButton);
        topPanel.add(new JLabel("Target Column:"));
        topPanel.add(columnSelector);
        topPanel.add(sortButton);

        // 3. Center Panel: Results
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(resultArea);

        // 4. Add Panels to Main Frame
        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        // 5. Add Button Actions (Event Listeners)
        uploadButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleFileUpload();
            }
        });

        sortButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                handleSorting();
            }
        });
    }

    private void handleFileUpload() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            File file = fileChooser.getSelectedFile();
            statusLabel.setText("Loaded: " + file.getName());
            resultArea.append("File selected: " + file.getAbsolutePath() + "\n");

            // TODO: Call CSVReader class here to parse data
        }
    }

    private void handleSorting() {
        resultArea.append("Running Test UI" + "...\n");

        long startTime = System.nanoTime();

        // TODO: Call Sorters class methods here based on selection

        long endTime = System.nanoTime();
        long duration = (endTime - startTime);

        resultArea.append("Finished in: " + duration + " nanoseconds\n");
        resultArea.append("---------------------------\n");
    }



    public static void main(String[] args) {
        // Run the GUI
        SwingUtilities.invokeLater(() -> {
            new SortingApp().setVisible(true);
        });
    }
}
