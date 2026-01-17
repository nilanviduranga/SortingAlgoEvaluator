package com.sortingapp;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;
import java.awt.*;
import java.io.File;
import java.util.Arrays;

public class SortingApp extends JFrame {
    // GUI Components
    private JButton uploadButton;
    private JButton sortButton;
    private JComboBox<String> columnSelector;
    private JTextArea resultArea;
    private JLabel statusLabel;
    private JLabel fileLabel;

    // High Contrast Colors
    private final Color UPLOAD_BTN_COLOR = new Color(52, 152, 219); // Bright Blue
    private final Color RUN_BTN_COLOR = new Color(39, 174, 96);     // Bright Green
    private final Color BG_COLOR = new Color(240, 240, 240);        // Light Grey

    public SortingApp() {
        // 1. Setup the Window
        setTitle("Sorting Algorithm Performance Evaluator");
        setSize(750, 650);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout(0, 0));
        setLocationRelativeTo(null); // Center on screen

        // --- TOP SECTION (INPUTS) ---
        JPanel topContainer = new JPanel();
        topContainer.setLayout(new GridLayout(2, 1, 10, 10));
        topContainer.setBorder(new EmptyBorder(15, 15, 15, 15));
        topContainer.setBackground(BG_COLOR);

        // Row 1: File Upload
        JPanel filePanel = new JPanel(new BorderLayout(10, 0));
        filePanel.setOpaque(false);

        uploadButton = createHighVisButton("1. UPLOAD CSV FILE", UPLOAD_BTN_COLOR);
        fileLabel = new JLabel("No file selected", SwingConstants.CENTER);
        fileLabel.setFont(new Font("Arial", Font.BOLD, 14));
        fileLabel.setForeground(Color.DARK_GRAY);
        fileLabel.setBorder(new LineBorder(Color.GRAY, 1));
        fileLabel.setOpaque(true);
        fileLabel.setBackground(Color.WHITE);

        filePanel.add(uploadButton, BorderLayout.WEST);
        filePanel.add(fileLabel, BorderLayout.CENTER);

        // Row 2: Column Select & Run
        JPanel actionPanel = new JPanel(new BorderLayout(10, 0));
        actionPanel.setOpaque(false);

        JPanel comboPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 0, 0));
        comboPanel.setOpaque(false);

        JLabel colLbl = new JLabel("Target Column: ");
        colLbl.setFont(new Font("Arial", Font.BOLD, 14));

        columnSelector = new JComboBox<>();
        columnSelector.addItem("Wait for Upload...");
        columnSelector.setPreferredSize(new Dimension(220, 45));
        columnSelector.setFont(new Font("Arial", Font.PLAIN, 13));

        comboPanel.add(colLbl);
        comboPanel.add(columnSelector);

        sortButton = createHighVisButton("2. RUN EVALUATION", RUN_BTN_COLOR);

        actionPanel.add(comboPanel, BorderLayout.CENTER);
        actionPanel.add(sortButton, BorderLayout.EAST);

        topContainer.add(filePanel);
        topContainer.add(actionPanel);

        // --- CENTER PANEL (RESULTS) ---
        resultArea = new JTextArea();
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.BOLD, 14));
        resultArea.setMargin(new Insets(15, 15, 15, 15));

        JScrollPane scrollPane = new JScrollPane(resultArea);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Performance Benchmark Results"));

        // --- BOTTOM PANEL (STATUS) ---
        JPanel statusPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        statusPanel.setBackground(Color.LIGHT_GRAY);
        statusLabel = new JLabel("System Ready");
        statusPanel.add(statusLabel);

        // Add to Frame
        add(topContainer, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);
        add(statusPanel, BorderLayout.SOUTH);

        // --- ACTIONS ---
        uploadButton.addActionListener(e -> handleFileUpload());
        sortButton.addActionListener(e -> handleSorting());
    }

    // Helper for Buttons
    private JButton createHighVisButton(String text, Color bg) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Arial", Font.BOLD, 14));
        btn.setBackground(bg);
        btn.setForeground(Color.WHITE);
        btn.setFocusPainted(false);
        btn.setPreferredSize(new Dimension(200, 45));
        btn.setOpaque(true);
        btn.setBorderPainted(false);
        return btn;
    }

    // File Handling
    private File currentFile;

    private void handleFileUpload() {
        JFileChooser fileChooser = new JFileChooser();
        int option = fileChooser.showOpenDialog(this);
        if (option == JFileChooser.APPROVE_OPTION) {
            currentFile = fileChooser.getSelectedFile();

            fileLabel.setText(" " + currentFile.getName());
            fileLabel.setBackground(new Color(220, 255, 220)); // Light Green
            statusLabel.setText("Loaded: " + currentFile.getAbsolutePath());

            resultArea.setText(""); // Clear previous results
            resultArea.append(">> FILE LOADED: " + currentFile.getName() + "\n");

            String[] headers = CSVReader.getHeaders(currentFile);
            columnSelector.removeAllItems();
            for (String header : headers) {
                columnSelector.addItem(header);
            }
            resultArea.append(">> COLUMNS DETECTED: " + headers.length + "\n");
        }
    }

    private void handleSorting() {
        if (currentFile == null || columnSelector.getSelectedIndex() < 0) {
            JOptionPane.showMessageDialog(this, "Please Step 1: Upload, Step 2: Select Column");
            return;
        }

        int colIndex = columnSelector.getSelectedIndex();

        // 1. Get Original Data
        double[] originalData = CSVReader.getColumnData(currentFile, colIndex);

        if (originalData.length == 0) {
            resultArea.append("Error: No valid numbers found in this column.\n");
            return;
        }

        resultArea.append("\n=== BENCHMARK STARTED (" + originalData.length + " rows) ===\n");
        resultArea.append("--------------------------------------------------\n");

        String bestAlgoName = "";
        long bestAlgoDuration = Long.MAX_VALUE;
        long startTime, endTime, duration;
        double[] testData; // Temp array for each sort

        // --- 1. Insertion Sort ---
        testData = originalData.clone(); // CRITICAL FIX: Clone data so it is unsorted
        startTime = System.nanoTime();
        Sorters.insertionSort(testData);
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        logResult("Insertion Sort", duration);
        if (duration < bestAlgoDuration) { bestAlgoDuration = duration; bestAlgoName = "Insertion Sort"; }

        // --- 2. Shell Sort ---
        testData = originalData.clone();
        startTime = System.nanoTime();
        Sorters.shellSort(testData);
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        logResult("Shell Sort", duration);
        if (duration < bestAlgoDuration) { bestAlgoDuration = duration; bestAlgoName = "Shell Sort"; }

        // --- 3. Merge Sort ---
        testData = originalData.clone();
        startTime = System.nanoTime();
        Sorters.mergeSort(testData);
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        logResult("Merge Sort", duration);
        if (duration < bestAlgoDuration) { bestAlgoDuration = duration; bestAlgoName = "Merge Sort"; }

        // --- 4. Quick Sort ---
        testData = originalData.clone();
        startTime = System.nanoTime();
        // Ensure your Sorters.quickSort matches this signature (array, low, high)
        Sorters.quickSort(testData, 0, testData.length - 1);
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        logResult("Quick Sort", duration);
        if (duration < bestAlgoDuration) { bestAlgoDuration = duration; bestAlgoName = "Quick Sort"; }

        // --- 5. Heap Sort ---
        testData = originalData.clone();
        startTime = System.nanoTime();
        Sorters.heapSort(testData);
        endTime = System.nanoTime();
        duration = (endTime - startTime);
        logResult("Heap Sort", duration);
        if (duration < bestAlgoDuration) { bestAlgoDuration = duration; bestAlgoName = "Heap Sort"; }

        // --- Final Result ---
        resultArea.append("--------------------------------------------------\n");
        resultArea.append("WINNER: " + bestAlgoName + "\n");
        resultArea.append("TIME  : " + (bestAlgoDuration / 1_000_000.0) + " ms\n");
        resultArea.append("==================================================\n\n");
    }

    private void logResult(String name, long timeNs) {
        // Formats neatly: "Name : Time ms"
        resultArea.append(String.format("%-18s : %10.4f ms\n", name, timeNs / 1_000_000.0));
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> new SortingApp().setVisible(true));
    }
}