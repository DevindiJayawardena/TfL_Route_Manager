import data.DataLoader;
import datastructures.Graph;
import services.MaintenanceService;
import services.RouteService;

import java.util.Scanner;

public class TfLApp {
    public static void main(String[] args) {
        Graph graph = new Graph();
        DataLoader.loadCSV("Data/Data.csv", graph);
        graph.addInterchangeEdges(2.0);

        RouteService routeService = new RouteService(graph);
        MaintenanceService maintenanceService = new MaintenanceService(graph);

        Scanner sc = new Scanner(System.in);

        while (true) {
            System.out.println("\n--- TfL Manager ---");
            System.out.println("Select Mode:");
            System.out.println("1) Manager Operations");
            System.out.println("2) Customer Operations");
            System.out.println("3) Exit");
            System.out.print("Choice: ");
            String modeChoice = sc.nextLine().trim();

            if (modeChoice.equals("3")) {
                System.out.println("Exiting program.");
                sc.close();
                return;
            }

            switch (modeChoice) {
                case "1": // Manager Operations
                    while (true) {
                        System.out.println("\n--- Manager Operations ---");
                        System.out.println("1) Add delay");
                        System.out.println("2) Remove delay");
                        System.out.println("3) Close track");
                        System.out.println("4) Open track");
                        System.out.println("5) List closed tracks");
                        System.out.println("6) List delayed tracks");
                        System.out.println("8) Print all nodes");
                        System.out.println("9) Print all edges");
                        System.out.println("0) Back to main menu");
                        System.out.print("Choice: ");
                        String mgrChoice = sc.nextLine().trim();

                        if (mgrChoice.equals("0")) break;

                        try {
                            switch (mgrChoice) {
                                case "1":
                                    System.out.print("Line: ");
                                    String line = sc.nextLine().trim();
                                    System.out.print("From station: ");
                                    String f = sc.nextLine().trim();
                                    System.out.print("To station: ");
                                    String t = sc.nextLine().trim();
                                    System.out.print("Delay minutes: ");
                                    double d = Double.parseDouble(sc.nextLine().trim());
                                    if (d <= 0) {
                                        System.out.println("Error: Delay must be positive.");
                                    } else {
                                        maintenanceService.addDelay(line, f, t, d);
                                    }
                                    break;

                                case "2":
                                    System.out.print("Line: ");
                                    line = sc.nextLine().trim();
                                    System.out.print("From station: ");
                                    f = sc.nextLine().trim();
                                    System.out.print("To station: ");
                                    t = sc.nextLine().trim();
                                    maintenanceService.removeDelay(line, f, t);
                                    break;

                                case "3":
                                    System.out.print("Line: ");
                                    line = sc.nextLine().trim();
                                    System.out.print("From station: ");
                                    f = sc.nextLine().trim();
                                    System.out.print("To station: ");
                                    t = sc.nextLine().trim();
                                    maintenanceService.closeTrack(line, f, t);
                                    break;

                                case "4":
                                    System.out.print("Line: ");
                                    line = sc.nextLine().trim();
                                    System.out.print("From station: ");
                                    f = sc.nextLine().trim();
                                    System.out.print("To station: ");
                                    t = sc.nextLine().trim();
                                    maintenanceService.openTrack(line, f, t);
                                    break;

                                case "5":
                                    maintenanceService.listClosedTracks();
                                    break;

                                case "6":
                                    maintenanceService.listDelayedTracks();
                                    break;

                                case "8":
                                    System.out.println("All Nodes:");
                                    graph.getAllNodes().forEach(n ->
                                            System.out.println(n.getId() + ": " + n.getName()));
                                    break;

                                case "9":
                                    System.out.println("All Edges:");
                                    graph.getAllEdges().forEach(e ->
                                            System.out.println(e.getId() + ": " +
                                                    e.description(graph.getNode(e.getFrom()), graph.getNode(e.getTo()))));
                                    break;

                                default:
                                    System.out.println("Invalid choice.");
                            }
                        } catch (NumberFormatException e) {
                            System.out.println("Error: Invalid number format.");
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    break;

                case "2": // Customer Operations
                    while (true) {
                        System.out.println("\n--- Customer Operations ---");
                        System.out.println("1) Find fastest route");
                        System.out.println("2) Print all nodes");
                        System.out.println("3) Print all edges");
                        System.out.println("0) Back to main menu");
                        System.out.print("Choice: ");
                        String custChoice = sc.nextLine().trim();

                        if (custChoice.equals("0")) break;

                        try {
                            switch (custChoice) {
                                case "1":
                                    System.out.print("From station: ");
                                    String from = sc.nextLine().trim();
                                    System.out.print("To station: ");
                                    String to = sc.nextLine().trim();
                                    routeService.findShortestPath(from, to);
                                    break;

                                case "2":
                                    System.out.println("All Nodes:");
                                    graph.getAllNodes().forEach(n ->
                                            System.out.println(n.getId() + ": " + n.getName()));
                                    break;

                                case "3":
                                    System.out.println("All Edges:");
                                    graph.getAllEdges().forEach(e ->
                                            System.out.println(e.getId() + ": " +
                                                    e.description(graph.getNode(e.getFrom()), graph.getNode(e.getTo()))));
                                    break;

                                default:
                                    System.out.println("Invalid choice.");
                            }
                        } catch (Exception e) {
                            System.out.println("Error: " + e.getMessage());
                        }
                    }
                    break;

                default:
                    System.out.println("Invalid mode selection.");
            }
        }
    }
}