package game.core;

import game.LoggerSetup;
import game.utils.Constants;

import java.awt.Point;
import java.util.Random;
import java.util.logging.Logger;

/**
 * The {@code Food} class is responsible for managing the spawning and storing of the current food on the screen.
 */
public class Food {
    /**
     * Logger for logging information.
     * @hidden
     */
    private static final Logger LOGGER = LoggerSetup.getLogger(Food.class.getName());

    /**
     * Represents the current {@code position} of the {@code Food}.
     */
    private Point position;

    /**
     * Used to generate a random position for the food.
     */
    private final Random rand;
    /**
     * Holds a reference to the current snake object. Used to ensure food does not spawn inside the snake's body.
     */
    private final Snake snake;

    /**
     * Creates {@code Food} at a {@code Point} specified location.
     * @param initialPos The initial position of the food. Must not be {@code null} and must be within the grid's bounds.
     * @param snake A reference to the current snake object. Must not be {@code null}.
     * @throws IllegalArgumentException if {@code initialPos} is {@code null} or is outside the grid's range or if the
     *                                  {@code snake} is null.
     *                                  This exception is caught and logged at {@code Level.SEVERE}.
     */
    Food(Point initialPos, Snake snake) {
        validateConstructor(initialPos, snake);
        this.snake = snake;
        rand = new Random();
        position = initialPos;
        LOGGER.config("Food created at: [x=" + initialPos.x + ", y=" + initialPos.y + "]");
    }

    /**
     * Creates food at a random position. The food is spawned at a random point in the grid that is not currently
     * occupied by the snake.
     */
    public void spawn() {
        Point pos;
        do {
            int x = rand.nextInt(Constants.NUM_COLS);
            int y = rand.nextInt(Constants.NUM_ROWS);
            pos = new Point(x, y);
        } while(snake.getSnake().contains(pos));
        position = pos;
        LOGGER.finer("Food spawned at: [x=" + pos.x + ", y=" + pos.y + "]");
    }

    /**
     * Gets the {@code Point} representing the {@code position} of the food.
     * @return The {@code Point} representing the current position.
     */
    public Point getPosition() {
        return position;
    }

    /**
     * Validates the values passed in to the food constructor
     *
     * @hidden
     * @param initialPos The initial position of the food.
     * @param snake A reference to the current snake object.
     * @throws IllegalArgumentException if {@code initialPos} is {@code null} or is outside the grid's range or if the
     *                                  {@code snake} is null.
     *                                  This exception is caught and logged at {@code Level.SEVERE}.
     */
    private void validateConstructor(Point initialPos, Snake snake) {
        if(initialPos == null) {
            LOGGER.severe("initialPos passed into Food constructor was null.");
            throw new IllegalArgumentException("initialPos must not be null");
        }
        if(initialPos.x < 0 || initialPos.x >= Constants.NUM_COLS ||
                initialPos.y < 0 || initialPos.y >= Constants.NUM_ROWS) {
            LOGGER.severe("initialPos passed into Food constructor was not within the correct range.");
            throw new IllegalArgumentException("initialPos must be within the range " +
                    "x:[0, " + Constants.NUM_COLS + "), y:[0, " + Constants.NUM_ROWS + ")");
        }
        if(snake == null) {
            LOGGER.severe("snake passed into the Food constructor was null.");
            throw new IllegalArgumentException("snake must not be null");
        }
    }
}
