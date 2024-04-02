package game.utils;

import javax.swing.ImageIcon;

import java.awt.Point;
import java.awt.Image;

/**
 * This is a class used to hold constants that are used throughout the game. These constants include initial game
 * states and basic formatting for the GUI. This class is not instantiable as it is only used to store constants in an
 * easy to access location.
 */
public final class Constants {
    /** Private constructor to prevent instantiation.
     * @hidden */
    private Constants() {

    }
    /** Icon for each window. */
    public static final Image ICON = new ImageIcon("src/resources/images/logo.png").getImage();

    /** Width of the game window. */
    public static final int WIDTH = 650;

    /** Height of the game window. */
    public static final int HEIGHT = 650;

    /** Padding size between the game window and the game grid. */
    public static final int PADDING_SIZE = 28;

    /** Height of header in the game window. */
    public static final int HEADER_HEIGHT = 70;

    /** Number of rows in the game grid. */
    public static final int NUM_ROWS = 15;

    /** Number of cols in the game grid. */
    public static final int NUM_COLS = 17;

    /** Delay for the game loop {@link javax.swing.Timer}. */
    public static final int DELAY = 200;

    /**
     * Size of the cells in the grid.
     * Calculated based on {@code WIDTH}, {@code PADDING_SIZE}, and {@code NUM_COLS}.
     */
    public static final int CELL_SIZE = (int) ((WIDTH - 2.0 * PADDING_SIZE) / NUM_COLS + 0.5);

    /** Length of the snake at the start of each game. */
    public static final int SNAKE_INITIAL_LENGTH = 3;

    /** Position of the snake's head at the start of the game. */
    public static final Point SNAKE_INITIAL_POSITION = new Point(3,7);

    /** Position of the first fruit at the start of the game. */
    public static final Point FOOD_INITIAL_POSITION = new Point(12, 7);
}
