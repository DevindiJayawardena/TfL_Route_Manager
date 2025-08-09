package services;

import datastructures.Graph;
import models.Edge;

import java.util.List;

public class MaintenanceService {
    private final Graph graph;

    public MaintenanceService(Graph graph) {
        this.graph = graph;
    }

    public void addDelay(String line, String fromStation, String toStation, double minutes) {
        if (!validateInputs(line, fromStation, toStation)) return;
        List<Edge> matches = graph.findEdges(line, fromStation, toStation);
        if (matches.isEmpty()) {
            System.out.println("Error: No matching track section found.");
            return;
        }
        for (Edge e : matches) e.setDelay(e.getDelay() + minutes);
        System.out.printf("Success: Added %.2f min delay to %d track(s).%n", minutes, matches.size());
    }

    public void removeDelay(String line, String fromStation, String toStation) {
        if (!validateInputs(line, fromStation, toStation)) return;
        List<Edge> matches = graph.findEdges(line, fromStation, toStation);
        if (matches.isEmpty()) {
            System.out.println("Error: No matching track section found.");
            return;
        }
        for (Edge e : matches) e.setDelay(0.0);
        System.out.printf("Success: Removed delays on %d track(s).%n", matches.size());
    }

    public void closeTrack(String line, String fromStation, String toStation) {
        if (!validateInputs(line, fromStation, toStation)) return;
        List<Edge> matches = graph.findEdges(line, fromStation, toStation);
        if (matches.isEmpty()) {
            System.out.println("Error: No matching track section found.");
            return;
        }
        for (Edge e : matches) e.setClosed(true);
        System.out.printf("Success: Closed %d track(s).%n", matches.size());
    }

    public void openTrack(String line, String fromStation, String toStation) {
        if (!validateInputs(line, fromStation, toStation)) return;
        List<Edge> matches = graph.findEdges(line, fromStation, toStation);
        if (matches.isEmpty()) {
            System.out.println("Error: No matching track section found.");
            return;
        }
        for (Edge e : matches) e.setClosed(false);
        System.out.printf("Success: Opened %d track(s).%n", matches.size());
    }

    public void listClosedTracks() {
        boolean any = false;
        for (Edge e : graph.getAllEdges()) {
            if (e.isClosed()) {
                System.out.println("CLOSED: " + e.description(graph.getNode(e.getFrom()), graph.getNode(e.getTo())));
                any = true;
            }
        }
        if (!any) System.out.println("No closed tracks.");
    }

    public void listDelayedTracks() {
        boolean any = false;
        for (Edge e : graph.getAllEdges()) {
            if (e.getDelay() > 0) {
                System.out.println("DELAYED: " + e.description(graph.getNode(e.getFrom()), graph.getNode(e.getTo())));
                any = true;
            }
        }
        if (!any) System.out.println("No delayed tracks.");
    }

    private boolean validateInputs(String line, String fromStation, String toStation) {
        if (!graph.lineExists(line)) {
            System.out.println("Error: Line '" + line + "' does not exist.");
            return false;
        }
        if (!graph.stationExists(fromStation)) {
            System.out.println("Error: Station '" + fromStation + "' does not exist.");
            return false;
        }
        if (!graph.stationExists(toStation)) {
            System.out.println("Error: Station '" + toStation + "' does not exist.");
            return false;
        }
        if (fromStation.equalsIgnoreCase(toStation)) {
            System.out.println("Error: From and To stations cannot be the same.");
            return false;
        }
        return true;
    }
}
