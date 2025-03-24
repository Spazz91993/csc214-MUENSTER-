package com.csc;
import java.io.*;
import java.util.*;

public class CheeseAnalyzer {
    public static void main(String[] args) {
        String inputFile = "cheese_data.csv";
        String outputFile = "output.txt";

        int pasteurizedCount = 0;
        int rawCount = 0;
        int organicHighMoistureCount = 0;
        Map<String, Integer> milkTypeCount = new HashMap<>();

        try (BufferedReader br = new BufferedReader(new FileReader(inputFile));
             BufferedWriter bw = new BufferedWriter(new FileWriter(outputFile))) {

            String line;
            boolean isHeader = true;

            while ((line = br.readLine()) != null) {
                // Skip the header row
                if (isHeader) {
                    isHeader = false;
                    continue;
                }

                String[] data = line.split(",");

                // Skip invalid rows (if columns are missing)
                if (data.length < 5 || containsEmptyValues(data)) {
                    continue;
                }

                // Extract relevant information from columns
                String milkTreatment = data[1].trim();
                int organic = Integer.parseInt(data[2].trim());
                double moisturePercent = Double.parseDouble(data[3].trim());
                String milkType = data[4].trim().toLowerCase();

                // Count pasteurized vs. raw
                if (milkTreatment.equalsIgnoreCase("pasteurized")) {
                    pasteurizedCount++;
                } else if (milkTreatment.equalsIgnoreCase("raw")) {
                    rawCount++;
                }

                // Count organic cheeses with moisture > 41.0%
                if (organic == 1 && moisturePercent > 41.0) {
                    organicHighMoistureCount++;
                }

                // Count types of animal milk
                milkTypeCount.put(milkType, milkTypeCount.getOrDefault(milkType, 0) + 1);
            }

            // Get the most common milk type
            String mostCommonMilkType = getMostCommonMilkType(milkTypeCount);

            // Write results to output.txt
            bw.write("Number of cheeses using pasteurized milk: " + pasteurizedCount + "\n");
            bw.write("Number of cheeses using raw milk: " + rawCount + "\n");
            bw.write("Number of organic cheeses with moisture > 41.0%: " + organicHighMoistureCount + "\n");
            bw.write("Most common milk type for cheeses in Canada: " + mostCommonMilkType + "\n");

            System.out.println("Calculations complete. Results saved to output.txt.");

        } catch (IOException | NumberFormatException e) {
            System.err.println("Error processing the file: " + e.getMessage());
        }
    }

    // Check for missing values in a row
    private static boolean containsEmptyValues(String[] data) {
        for (String value : data) {
            if (value == null || value.trim().isEmpty()) {
                return true;
            }
        }
        return false;
    }

    // Determine the most common milk type
    private static String getMostCommonMilkType(Map<String, Integer> milkTypeCount) {
        String mostCommon = "";
        int maxCount = 0;

        for (Map.Entry<String, Integer> entry : milkTypeCount.entrySet()) {
            if (entry.getValue() > maxCount) {
                mostCommon = entry.getKey();
                maxCount = entry.getValue();
            }
        }

        // Capitalize first letter
        if (!mostCommon.isEmpty()) {
            mostCommon = mostCommon.substring(0, 1).toUpperCase() + mostCommon.substring(1);
        }

        return mostCommon;
    }
}
