package services;

import datastructures.Graph;
import models.Edge;
import models.Node;

import java.util.*;

public class RouteService {
    private final Graph graph;

    public RouteService(Graph graph) {
        this.graph = graph;
    }

    public void findShortestPath(String fromStation, String toStation) {
        if (!graph.stationExists(fromStation)) {
            System.out.println("Error: Station '" + fromStation + "' does not exist.");
            return;
        }
        if (!graph.stationExists(toStation)) {
            System.out.println("Error: Station '" + toStation + "' does not exist.");
            return;
        }
        if (fromStation.equalsIgnoreCase(toStation)) {
            System.out.println("Start and destination are the same.");
            return;
        }

        Map<Integer, Node> nodes = graph.getNodesMap();
        int n = nodes.size();

        List<Integer> starts = new ArrayList<>();
        List<Integer> ends = new ArrayList<>();
        for (Node node : nodes.values()) {
            if (node.getStation().equalsIgnoreCase(fromStation)) starts.add(node.getId());
            if (node.getStation().equalsIgnoreCase(toStation)) ends.add(node.getId());
        }

        double[] dist = new double[n];
        int[] prevEdge = new int[n];
        Arrays.fill(dist, Double.POSITIVE_INFINITY);
        Arrays.fill(prevEdge, -1);

        PriorityQueue<int[]> pq = new PriorityQueue<>(Comparator.comparingDouble(a -> a[1]));
        for (int s : starts) {
            dist[s] = 0.0;
            pq.add(new int[]{s, 0});
        }

        while (!pq.isEmpty()) {
            int[] cur = pq.poll();
            int u = cur[0];
            double dU = dist[u];
            if (dU > dist[u] + 1e-9) continue;

            for (int eId : graph.getAdjEdges(u)) {
                Edge e = graph.getAllEdges().get(eId);
                if (e.isClosed()) continue;
                int v = e.getTo();
                double nd = dU + e.getEffectiveTime();
                if (nd + 1e-9 < dist[v]) {
                    dist[v] = nd;
                    prevEdge[v] = e.getId();
                    pq.add(new int[]{v, (int) Math.round(nd)});
                }
            }
        }

        double best = Double.POSITIVE_INFINITY;
        int bestNode = -1;
        for (int t : ends) {
            if (dist[t] < best) {
                best = dist[t];
                bestNode = t;
            }
        }

        if (bestNode == -1) {
            System.out.println("No available route found (possible track closures).");
            return;
        }

        List<Edge> pathEdges = new ArrayList<>();
        int curNode = bestNode;
        while (prevEdge[curNode] != -1) {
            Edge e = graph.getAllEdges().get(prevEdge[curNode]);
            pathEdges.add(e);
            curNode = e.getFrom();
        }
        Collections.reverse(pathEdges);

        System.out.println("Fastest route from " + fromStation + " to " + toStation + ":");
        double total = 0.0;
        int step = 1;
        for (Edge e : pathEdges) {
            Node from = graph.getNode(e.getFrom());
            Node to = graph.getNode(e.getTo());
            System.out.printf("(%d) %s: %s -> %s (%.2f min)%n",
                    step++, e.getLine(), from.getName(), to.getName(), e.getEffectiveTime());
            total += e.getEffectiveTime();
        }
        System.out.printf("Total journey time: %.2f minutes%n", total);
    }
}
