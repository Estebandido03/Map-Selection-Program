package main.java;

import java.util.*;

public class Graph {
    private Map<String, Node> nodes = new HashMap<>();

    public void addNode(String id, int x, int y) {
        nodes.put(id, new Node(id, x, y));
    }

    public void addEdge(String from, String to) {
        Node a = nodes.get(from);
        Node b = nodes.get(to);
        a.addNeighbor(b);
        b.addNeighbor(a);
    }

    public Node getNode(String id) {
        return nodes.get(id);
    }

}