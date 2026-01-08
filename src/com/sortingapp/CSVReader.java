package com.sortingapp;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CSVReader {

    // 1. Get the first row (headers) to fill the dropdown menu
    public static String[] getHeaders(File file) {
        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Read only the first line
            if (line != null) {
                // Split by comma (standard CSV format)
                return line.split(",");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new String[]{}; // Return empty if failed
    }

    // 2. Get all numbers from a specific column index
    public static double[] getColumnData(File file, int columnIndex) {
        List<Double> dataList = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line = br.readLine(); // Skip headers (first line)

            while ((line = br.readLine()) != null) {
                String[] values = line.split(",");
                // Safety check: ensure the row has enough columns
                if (columnIndex < values.length) {
                    try {
                        // Convert text to number
                        double val = Double.parseDouble(values[columnIndex].trim());
                        dataList.add(val);
                    } catch (NumberFormatException e) {
                        // Skip invalid data (e.g., text in a number column)
                        System.out.println("Skipping invalid number: " + values[columnIndex]);
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        // Convert List to simple Array (easier for sorting algorithms)
        double[] dataArray = new double[dataList.size()];
        for (int i = 0; i < dataList.size(); i++) {
            dataArray[i] = dataList.get(i);
        }
        return dataArray;
    }
}