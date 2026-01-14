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

    // Stores the currently loaded file
    private File currentFile;

    private void handleFileUpload() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();
            statusLabel.setText("Loaded: " + currentFile.getName());
            resultArea.append("File loaded: " + currentFile.getName() + "\n");

            // Use Member 1's code to get headers
            String[] headers = CSVReader.getHeaders(currentFile);

            // Clear and refill the dropdown
            columnSelector.removeAllItems();
            for (String header : headers) {
                columnSelector.addItem(header);
            }
            resultArea.append("Columns detected: " + headers.length + "\n");
        }
    }

    private void handleSorting() {
        // Validation: Check if file exists and column is selected
        if (currentFile == null || columnSelector.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Please upload a file and select a column first!");
            return;
        }

//        String selectedAlgo = (String) algoSelector.getSelectedItem();
        int colIndex = columnSelector.getSelectedIndex();

        // 1. Get Data (Using Member 1's Code)
        resultArea.append("Reading data...\n");
        double[] data = CSVReader.getColumnData(currentFile, colIndex);

        if (data.length == 0) {
            resultArea.append("Error: No valid numbers found in this column.\n");
            return;
        }

        String bestAlgoName;
        long bestAlgoDuration;

        long startTime = System.nanoTime();
        Sorters.insertionSort(data);
        long endTime = System.nanoTime();
        long duration = (endTime - startTime);
        bestAlgoName = "Insertion Sort";
        bestAlgoDuration = duration;
        resultArea.append("Sorting " + data.length + " rows using Insertion Sort" + "...\n");
        resultArea.append("Done! Execution Time: " + duration + " ns (" + (duration / 1_000_000.0) + " ms)\n");
        resultArea.append("---------------------------\n");

        startTime = System.nanoTime();
        Sorters.shellSort(data);
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        if (duration < bestAlgoDuration) {
            bestAlgoName = "Shell Sort";
            bestAlgoDuration = duration;
        }
        resultArea.append("Sorting " + data.length + " rows using Shell Sort" + "...\n");
        resultArea.append("Done! Execution Time: " + duration + " ns (" + (duration / 1_000_000.0) + " ms)\n");
        resultArea.append("---------------------------\n");

        startTime = System.nanoTime();
        Sorters.mergeSort(data);
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        if (duration < bestAlgoDuration) {
            bestAlgoName = "Merge Sort";
            bestAlgoDuration = duration;
        }
        resultArea.append("Sorting " + data.length + " rows using Merge Sort" + "...\n");
        resultArea.append("Done! Execution Time: " + duration + " ns (" + (duration / 1_000_000.0) + " ms)\n");
        resultArea.append("---------------------------\n");

        resultArea.append("---------------------------\n");
        resultArea.append("---------------------------\n");
        resultArea.append("The Best Sorting Algo:"+bestAlgoName+" Execution Time: " + bestAlgoDuration + " ns (" + (bestAlgoDuration / 1_000_000.0) + " ms)\n");
        resultArea.append("---------------------------\n");
        resultArea.append("---------------------------\n");



    }


    public static void main(String[] args) {
        // Run the GUI
        SwingUtilities.invokeLater(() -> {
            new SortingApp().setVisible(true);
        });
    }
}
