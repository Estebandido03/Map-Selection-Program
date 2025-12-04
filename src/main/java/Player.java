package main.java;

public class Player {
    public Node current;

    public int x, y;
    public boolean isMoving = false;

    public enum Direction { UP, DOWN, LEFT, RIGHT }
    public Direction direction = Direction.DOWN;

    public int frameIndex = 0;
    public long lastFrameTime = 0;

    public Player(Node start) {
        this.current = start;
        this.x = start.x;
        this.y = start.y;
    }
}
