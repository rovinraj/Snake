package game;

import javax.swing.ImageIcon;

import java.awt.Point;
import java.awt.Image;

// Class to hold global constants
public final class Constants {
    private Constants() {
    }
    public static final Image ICON = new ImageIcon("src/data/logo.png").getImage();
    public static final int WIDTH = 650;
    public static final int HEIGHT = 650;
    public static final int PADDING_SIZE = 28;
    public static final int HEADER_HEIGHT = 70;
    public static final int NUM_ROWS = 15;
    public static final int NUM_COLS = 17;
    public static final int DELAY = 200;
    public static final int CELL_SIZE = (int) ((Constants.WIDTH - 2.0 * Constants.PADDING_SIZE) / Constants.NUM_COLS + 0.5);
    public static final int SNAKE_INITIAL_LENGTH = 3;
    public static final Point SNAKE_INITIAL_POSITION = new Point(3,7);
    public static final Point FOOD_INITIAL_POSITION = new Point(12, 7);
}
