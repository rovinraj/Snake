package game;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class Snake {
    private final List<Point> body;
    private Direction direction;
    private Direction nextDirection;

    // Constructor to initialize the snake
    Snake(Point startPosition, int initialLength) {
        body = new ArrayList<>();
        body.add(startPosition); // Add the starting position to the snake's body
        direction = Direction.RIGHT; // Default direction
        nextDirection = null;
        for(int i = 1; i < initialLength; i++) {
            body.add(new Point(startPosition.x - i, startPosition.y));
        }
    }

    public void move() {
        Point newHead = new Point(body.get(0)); // Create a copy of the head to become the new head

        // Change the new head's position based on the direction
        switch (direction) {
            case UP -> newHead.y--;
            case DOWN -> newHead.y++;
            case LEFT -> newHead.x--;
            case RIGHT -> newHead.x++;
        }

        body.add(0, newHead); // Add new head at the start of the list
        body.remove(body.size() - 1); // Remove the tail
    }

    public void grow() {
        Point newTail = new Point(body.get(body.size() - 1));
        switch (direction) {
            case UP -> newTail.y++;
            case DOWN -> newTail.y--;
            case LEFT -> newTail.x++;
            case RIGHT -> newTail.x--;
        }
        body.add(newTail); // Add new segment to the tail
    }

    public void changeDirection(Direction newDirection) {
        switch (newDirection) {
            case RIGHT -> {
                if (direction != Direction.LEFT)
                    nextDirection = newDirection;
            }
            case LEFT -> {
                if (direction != Direction.RIGHT)
                    nextDirection = newDirection;
            }
            case DOWN -> {
                if (direction != Direction.UP)
                    nextDirection = newDirection;
            }
            case UP -> {
                if (direction != Direction.DOWN)
                    nextDirection = newDirection;
            }
        }
    }
    public void setDirection() {
        if (nextDirection != null) {
            direction = nextDirection;
            nextDirection = null;
        }
    }
    public List<Point> getSnake() {
        return body;
    }
}
