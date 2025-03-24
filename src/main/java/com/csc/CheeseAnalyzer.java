package com.csc;
import java.io.*;
import java.util.*;

public class CheeseAnalyzer { public static void main(String[] args) {
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
          if (isHeader) {
              isHeader = false;
              continue;
          }

          String[] data = line.split(",");
          if (data.length < 12 || containsEmptyValues(data)) {
              continue;
          }

          try {
              String milkTreatment = data[9].trim();
              String organicStr = data[6].trim();
              String moistureStr = data[3].trim();
              String milkType = data[8].trim().toLowerCase();

              if (isNumeric(organicStr)) {
                  int organic = Integer.parseInt(organicStr);

                  if (isDouble(moistureStr)) {
                      double moisturePercent = Double.parseDouble(moistureStr);

                      if (organic == 1 && moisturePercent > 41.0) {
                          organicHighMoistureCount++;
                      }
                  }
              }

              if (milkTreatment.equalsIgnoreCase("pasteurized")) {
                  pasteurizedCount++;
              } else if (milkTreatment.equalsIgnoreCase("raw")) {
                  rawCount++;
              }

              milkTypeCount.put(milkType, milkTypeCount.getOrDefault(milkType, 0) + 1);

          } catch (NumberFormatException e) {
              System.err.println("Skipping invalid row: " + line);
              continue;
          }
      }

      String mostCommonMilkType = getMostCommonMilkType(milkTypeCount);

      bw.write("Number of cheeses using pasteurized milk: " + pasteurizedCount + "\n");
      bw.write("Number of cheeses using raw milk: " + rawCount + "\n");
      bw.write("Number of organic cheeses with moisture > 41.0%: " + organicHighMoistureCount + "\n");
      bw.write("Most common milk type for cheeses in Canada: " + mostCommonMilkType + "\n");

      System.out.println("✅ Calculations complete. Results saved to output.txt.");

  } catch (IOException | NumberFormatException e) {
      System.err.println("Error processing the file: " + e.getMessage());
  }
}

private static boolean containsEmptyValues(String[] data) {
  for (String value : data) {
      if (value == null || value.trim().isEmpty()) {
          return true;
      }
  }
  return false;
}

private static String getMostCommonMilkType(Map<String, Integer> milkTypeCount) {
  String mostCommon = "";
  int maxCount = 0;

  for (Map.Entry<String, Integer> entry : milkTypeCount.entrySet()) {
      if (entry.getValue() > maxCount) {
          mostCommon = entry.getKey();
          maxCount = entry.getValue();
      }
  }

  if (!mostCommon.isEmpty()) {
      mostCommon = mostCommon.substring(0, 1).toUpperCase() + mostCommon.substring(1);
  }

  return mostCommon;
}

private static boolean isNumeric(String str) {
  try {
      Integer.parseInt(str);
      return true;
  } catch (NumberFormatException e) {
      return false;
  }
}

private static boolean isDouble(String str) {
  try {
      Double.parseDouble(str);
      return true;
  } catch (NumberFormatException e) {
      return false;
  }
}
}
