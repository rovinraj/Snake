package game.gui;

import game.LoggerSetup;
import game.core.Food;
import game.utils.Constants;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.logging.Logger;

/**
 * Used to draw the apple onto the game grid. The apple is drawn using {@link Graphics2D} and will pulsate as the timer
 * runs.
 */
public class AppleGraphics {
    /**
     * Logger for logging information.
     * @hidden
     */
    private static final Logger LOGGER = LoggerSetup.getLogger(AppleGraphics.class.getName());
    /**
     * Initial size of the apple.
     */
    private final int INITIAL_SIZE = Constants.CELL_SIZE;
    /**
     * Current {@code size} of the apple.
     */
    private int size;
    /**
     * Represents whether the apple is {@code growing} or not. The value is {@code true} if it's growing, otherwise
     * it's {@code false}.
     */
    private boolean growing;

    /**
     * Creates a new {@code AppleGraphics} object that is initially growing.
     */
    AppleGraphics() {
        growing = true;
        size = INITIAL_SIZE;
    }

    /**
     * Draws a new apple on the screen that alternates between growing and shrinking as determined by the timer.
     * @param g2d a reference to the graphics of the {@link GameGridPanel}.
     * @param food a reference to the current {@code Food} object.
     */
    public void drawApple(Graphics2D g2d, Food food) {
        // Enable anti-aliasing for smoother edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        int originalX = food.getPosition().x * Constants.CELL_SIZE;
        int originalY = food.getPosition().y * Constants.CELL_SIZE;

        // Calculate new top-left coordinates to keep the apple centered
        int x = originalX + (INITIAL_SIZE - size) / 2;
        int y = originalY + (INITIAL_SIZE - size) / 2;

        // Shadow
        g2d.setColor(new Color(161, 206, 78));
        g2d.fillOval(x, y + size / 3, size, size);

        // Body
        g2d.setColor(new Color(251, 79, 34));
        g2d.fillOval(x, y, size, size);

        // Stem
        int stemX = x + size / 2 - size / 20;
        int stemY = y - size / 5;
        int stemWidth = size / 10;
        int stemHeight = size / 5;
        g2d.setColor(new Color(165, 120, 84));
        g2d.fillRect(stemX, stemY, stemWidth, stemHeight);

        // Leaf
        int leafX = stemX + stemWidth - 1;
        int leafY = stemY - 4;
        int leafWidth = size / 2;
        int leafHeight = size / 5;
        g2d.setColor(new Color(83, 214, 45));
        g2d.fillArc(leafX, leafY, leafWidth, leafHeight, 0, 180);
        g2d.fillArc(leafX, leafY - 1, leafWidth, leafHeight, 180, 180);

        // Glare
        AffineTransform ogTrans = g2d.getTransform();
        g2d.setColor(new Color(255, 128, 101));
        int glareX = x + size / 7;
        int glareY = y + size / 7;
        int glareWidth = size / 4;
        int glareHeight = size / 3;
        g2d.rotate(0.5, glareX + glareWidth / 2.0, glareY + glareHeight / 2.0);
        g2d.fillOval(glareX, glareY, glareWidth, glareHeight);
        g2d.setTransform(ogTrans);

        // Update the apple's size to create a pulsating effect
        if (growing) {
            size += 2;
        } else {
            size -= 2;
        }

        // Reverse the growing/shrinking direction if limits are reached
        if (size >= INITIAL_SIZE + 5) {
            growing = false;
        } else if (size <= INITIAL_SIZE) {
            growing = true;
        }

        LOGGER.finest("Painted a " + (growing ? "growing" : "shrinking") + " apple at [x="
                + food.getPosition().x + ",y=" + food.getPosition().y + "].");
    }
}
