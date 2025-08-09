package data;

import datastructures.Graph;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class DataLoader {
    public static void loadCSV(String filepath, Graph graph) {
        try (BufferedReader br = new BufferedReader(new FileReader(filepath))) {
            String header = br.readLine();
            if (header == null) {
                System.err.println("CSV file is empty: " + filepath);
                return;
            }
            String line;
            int lineNo = 1;
            while ((line = br.readLine()) != null) {
                lineNo++;
                if (line.trim().isEmpty()) continue;
                String[] parts = line.split(",", -1);
                if (parts.length < 5) {
                    System.err.println("Skipping malformed line " + lineNo + ": " + line);
                    continue;
                }
                String lineName = parts[0].trim();
                String direction = parts[1].trim();
                String fromStation = parts[2].trim();
                String toStation = parts[3].trim();
                String timeStr = parts[4].trim();
                double time;
                try {
                    time = Double.parseDouble(timeStr);
                } catch (NumberFormatException ex) {
                    System.err.println("Invalid time at line " + lineNo + ": " + timeStr);
                    continue;
                }
                int fromId = graph.ensureNode(fromStation, lineName, direction);
                int toId = graph.ensureNode(toStation, lineName, direction);
                graph.addEdge(fromId, toId, lineName, time);
            }
        } catch (IOException e) {
            System.err.println("Error loading CSV: " + e.getMessage());
        }
    }
}