package models;

public class Edge {
    private final int id;
    private final int from;
    private final int to;
    private final String line;
    private final double baseTime;
    private double delay = 0.0;
    private boolean closed = false;

    public Edge(int id, int from, int to, String line, double baseTime) {
        this.id = id;
        this.from = from;
        this.to = to;
        this.line = line;
        this.baseTime = baseTime;
    }

    public int getId() { return id; }
    public int getFrom() { return from; }
    public int getTo() { return to; }
    public String getLine() { return line; }
    public double getBaseTime() { return baseTime; }
    public double getDelay() { return delay; }
    public boolean isClosed() { return closed; }

    public void setDelay(double delay) { this.delay = delay; }
    public void setClosed(boolean closed) { this.closed = closed; }

    public double getEffectiveTime() { return baseTime + delay; }

    public String description(Node fromNode, Node toNode) {
        return String.format("%s -> %s | line=%s | base=%.2f | delay=%.2f%s",
                fromNode.getName(), toNode.getName(), line, baseTime, delay, closed ? " | CLOSED" : "");
    }
}
