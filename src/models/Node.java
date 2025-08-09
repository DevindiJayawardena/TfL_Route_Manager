package models;

public class Node {
    private final int id;
    private final String station;
    private final String line;
    private final String direction;

    public Node(int id, String station, String line, String direction) {
        this.id = id;
        this.station = station;
        this.line = line;
        this.direction = direction;
    }

    public int getId() { return id; }
    public String getStation() { return station; }
    public String getLine() { return line; }
    public String getDirection() { return direction; }

    public String getName() {
        return station + " (" + line + ", " + direction + ")";
    }
}
