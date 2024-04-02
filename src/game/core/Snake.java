package game.core;

import game.LoggerSetup;
import game.utils.Direction;

import java.awt.Point;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * The {@code Snake} class is responsible for managing the current position of the {@code body} and {@code direction}
 * of the snake within the current game loop. It can only change directions once per frame so the {@code nextDirection}
 * is queued until the next frame begins. Inputs can be buffered (multiple entered per frame), allowing for the game
 * to feel more responsive.
 */
public class Snake {
    /**
     * Logger for logging information.
     * @hidden
     */
    private static final Logger LOGGER = LoggerSetup.getLogger(Snake.class.getName());

    /**
     * A List representing the points on the grid the snake's body occupies.
     */
    private final List<Point> body;

    /**
     * Used to determine which direction the snake is currently moving.
     */
    private Direction direction;

    /**
     * Used to determine which direction the snake will move on the next frame.
     */
    private Direction nextDirection;
    /**
     * Used to buffer an input for the next frame. Allows for two inputs to be used on the current frame. Is only set
     * if the direction is attempted to be changed while {@code nextDirection} is not {@code null}.
     */
    private Direction bufferDirection;
    /**
     * A {@code Point} used to represent the tail from the previous frame. This is added to the end of the snake when it
     * grows and when the snake moves backwards a frame when the game ends.
     */
    private Point oldTail;

    /**
     * Initializes a {@code snake} in a game ready state by creating a list of points to represent the {@code body}
     * and setting its starting direction.
     * @param startPosition The initial position of the head of the {@code snake}. Must not be {@code null}.
     * @param initialLength The initial length of the {@code snake}. Must be positive and fit within the grid given
     *                      the {@code startPosition}.
     * @throws IllegalArgumentException if {@code startPosition} is {@code null} or if the {@code initialLength} is
     *                                  non-positive or too long to fit given the specified {@code startPosition}.
     *                                  This exception is caught and logged at {@code Level.SEVERE}.
     */
    Snake(Point startPosition, int initialLength) {
        // validate the parameters
        validateConstructor(startPosition, initialLength);
        // init the body arraylist
        body = new ArrayList<>();
        // add the start position as the head
        body.add(startPosition);
        // Set the direction and nextDirection
        direction = Direction.RIGHT;
        nextDirection = null;
        bufferDirection = null;
        // add the rest of the body to the body array
        for(int i = 1; i < initialLength; i++) {
            body.add(new Point(startPosition.x - i, startPosition.y));
        }
        LOGGER.config("Snake initialized at [x=" + startPosition.x + ", y=" + startPosition.y + "] " +
                            "with length " + initialLength + '.');
    }

    /**
     * Moves the snack up one space based on the new direction of the snake. This method is called once a frame so the
     * direction must be updated as well.
     */
    public void move() {
        // Update direction as it's now the start of a new frame
        updateDirection();

        // Create a copy of the head to become the new head
        Point newHead = new Point(body.get(0));

        // Change the new head's position based on the direction variable
        switch (direction) {
            case UP -> newHead.y--;
            case DOWN -> newHead.y++;
            case LEFT -> newHead.x--;
            case RIGHT -> newHead.x++;
        }

        // Add new head to the front of the list
        body.add(0, newHead);
        // Remove the tail and store in oldTail
        oldTail = body.remove(body.size() - 1);
        LOGGER.finest("Snake moved to [" + newHead.x + ", " + newHead.y + "].");
    }

    /**
     * Increase the length of the snake by adding the tail from the previous frame to the {@code body}.
     */
    public void grow() {
        // Add the old tail to the end of the list
        body.add(oldTail);
        LOGGER.finer("Snake grew to a length of " + body.size() + ".");
    }

    /**
     * Changes the {@code nextDirection} of the snake if it is not attempting to go in the opposite direction, such as
     * right to left. If the {@code nextDirection} already has a value then buffer the input instead by setting
     * {@code bufferDirection} to the {@code newDirection}.
     * @param newDirection direction to attempt to change to.
     */
    public void changeDirection(Direction newDirection) {
        boolean queue = false;
        switch (newDirection) {
            case RIGHT -> {
                if (direction != Direction.LEFT) {
                    queue = true;
                    if(nextDirection == null)
                        nextDirection = newDirection;
                    else
                        bufferDirection = newDirection;
                }
            }
            case LEFT -> {
                if (direction != Direction.RIGHT) {
                    queue = true;
                    if(nextDirection == null)
                        nextDirection = newDirection;
                    else
                        bufferDirection = newDirection;
                }
            }
            case DOWN -> {
                if (direction != Direction.UP) {
                    queue = true;
                    if(nextDirection == null)
                        nextDirection = newDirection;
                    else
                        bufferDirection = newDirection;
                }
            }
            case UP -> {
                if (direction != Direction.DOWN) {
                    queue = true;
                    if(nextDirection == null)
                        nextDirection = newDirection;
                    else
                        bufferDirection = newDirection;
                }
            }
        }
        if(queue)
            LOGGER.finer("Direction change queued to " + direction + ".");
        else
            LOGGER.finer("Opposite direction attempted (" + direction + " -> " + newDirection + ").");
    }

    /**
     * Sets the current {@code direction} to the {@code nextDirection }as long as the {@code nextDirection} is not null.
     * Uses the {@code bufferDirection} to set the value of {@code nextDirection} for the next frame.
     * Resets {@code bufferDirection} to {@code null}.
     */
    public void updateDirection() {
        if (nextDirection != null) {
            direction = nextDirection;
            nextDirection = bufferDirection;
            bufferDirection = null;
            LOGGER.fine("Snake changed direction to " + direction + ".");
        }
    }

    /**
     * Moves the snake backwards to its state from the previous frame. This is used when the snake dies
     * And we don't want to show the snake phased inside the wall on the final frame.
     */
    public void moveBackwards() {
        // Add oldTail to the end of list and remove the head.
        body.add(oldTail);
        body.remove(0);
    }

    /**
     * Gets the {@code List<Point>} representing the {@code body} of the snake.
     * @return a list representing the snake.
     */
    public List<Point> getSnake() {
        return body;
    }

    /**
     * Validates the values passed in to the {@code Snake} constructor
     *
     * @hidden
     * @param startPosition The initial position of the head of the {@code snake}.
     * @param initialLength The initial length of the {@code snake}.
     * @throws IllegalArgumentException if {@code startPosition} is {@code null} or if the {@code initialLength} is
     *                                  non-positive or too long to fit given the specified {@code startPosition}.
     *                                  The initial length is too long if it is greater than the startPosition's x + 1.
     *                                  This exception is caught and logged at {@code Level.SEVERE}.
     */
    private void validateConstructor(Point startPosition, int initialLength) {
        if(startPosition == null) {
            LOGGER.severe("startPosition passed into Snake constructor was null");
            throw new IllegalArgumentException("startPosition must not be null");
        }
        if(initialLength <= 0) {
            LOGGER.severe("initialLength passed into Snake constructor was non-positive");
            throw new IllegalArgumentException("initialLength must be positive");
        }
        if(initialLength > startPosition.x + 1) {
            LOGGER.severe("initialLength was too long for startPosition");
            throw new IllegalArgumentException("initialLength must be less than or " +
                                                "equal to the startPosition.x + 1");
        }
    }
}
