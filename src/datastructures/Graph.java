package datastructures;

import models.Edge;
import models.Node;

import java.util.*;

public class Graph {
    private final Map<Integer, Node> nodes = new HashMap<>();
    private final Map<Integer, List<Integer>> adj = new HashMap<>();
    private final List<Edge> edges = new ArrayList<>();
    private int nextNodeId = 0;
    private int nextEdgeId = 0;

    private final Map<String, Integer> nodeKeyToId = new HashMap<>();

    public int ensureNode(String station, String line, String direction) {
        String key = (station + "|" + line + "|" + direction).toLowerCase();
        if (nodeKeyToId.containsKey(key)) return nodeKeyToId.get(key);

        int id = nextNodeId++;
        Node n = new Node(id, station, line, direction);
        nodes.put(id, n);
        adj.put(id, new ArrayList<>());
        nodeKeyToId.put(key, id);
        return id;
    }

    public Node getNode(int id) { return nodes.get(id); }

    public Edge addEdge(int fromId, int toId, String line, double baseTime) {
        Edge e = new Edge(nextEdgeId++, fromId, toId, line, baseTime);
        edges.add(e);
        adj.get(fromId).add(e.getId());
        return e;
    }

    public Collection<Node> getAllNodes() { return nodes.values(); }
    public List<Edge> getAllEdges() { return edges; }
    public List<Integer> getAdjEdges(int nodeId) { return adj.get(nodeId); }
    public Map<Integer, Node> getNodesMap() { return nodes; }

    public List<Edge> findEdges(String line, String fromStation, String toStation) {
        List<Edge> res = new ArrayList<>();
        for (Edge e : edges) {
            Node from = nodes.get(e.getFrom());
            Node to = nodes.get(e.getTo());
            if (e.getLine().equalsIgnoreCase(line)
                    && from.getStation().equalsIgnoreCase(fromStation)
                    && to.getStation().equalsIgnoreCase(toStation)) {
                res.add(e);
            }
        }
        return res;
    }

    public boolean stationExists(String station) {
        return nodes.values().stream().anyMatch(n -> n.getStation().equalsIgnoreCase(station));
    }

    public boolean lineExists(String line) {
        return edges.stream().anyMatch(e -> e.getLine().equalsIgnoreCase(line));
    }

    public void addInterchangeEdges(double interchangePenaltyMinutes) {
        Map<String, List<Integer>> byStation = new HashMap<>();
        for (Node n : nodes.values()) {
            String key = n.getStation().toLowerCase();
            byStation.computeIfAbsent(key, k -> new ArrayList<>()).add(n.getId());
        }

        for (List<Integer> ids : byStation.values()) {
            if (ids.size() < 2) continue;
            for (int i = 0; i < ids.size(); i++) {
                for (int j = 0; j < ids.size(); j++) {
                    if (i == j) continue;
                    addEdge(ids.get(i), ids.get(j), "INTERCHANGE", interchangePenaltyMinutes);
                }
            }
        }
    }
}
