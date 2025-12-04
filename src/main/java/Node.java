package main.java;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

public class Node {
    public String id;
    public int x, y;
    public List<Node> neighbors = new ArrayList<>();

    public BufferedImage levelImage;

    public Node(String id, int x, int y) {
        this.id = id;
        this.x = x;
        this.y = y;
    }

    public void addNeighbor(Node n) {
        if (!neighbors.contains(n)) neighbors.add(n);
    }
}