package game.gui;

import game.LoggerSetup;
import game.core.GameEngine;
import game.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * A custom JPanel that consists of a padding panel to add spacing around the grid and a grid that is repainted every
 * frame to represent the current state of the game.
 */
public class GameGridPanel extends JPanel {

    /**
     * Logger for logging information.
     * @hidden
     */
    private static final Logger LOGGER = LoggerSetup.getLogger(GameGridPanel.class.getName());

    /**
     * A reference to the {@code gameEngine} of the current game loop.
     */
    private final GameEngine gameEngine;

    /**
     * The panel that encompasses the grid allowing for padding around the edges of the grid.
     */
    private final JPanel paddingPanel;

    /**
     * A graphics helper that draws the apple onto the grid.
     */
    private final AppleGraphics apg;

    /**
     * A graphics helper that draws the snake onto the grid.
     */
    private final SnakeGraphics snkg;

    /**
     * Creates a new {@code GameGridPanel} that updates based off the {@code GameEngine} passed.
     * @param gameEngine a reference to the current {@code GameEngine}.
     */
    GameGridPanel(GameEngine gameEngine) {
        this.gameEngine = gameEngine;

        paddingPanel = new JPanel(new BorderLayout());
        paddingPanel.setBorder(BorderFactory.createEmptyBorder(Constants.PADDING_SIZE, Constants.PADDING_SIZE,
                Constants.PADDING_SIZE, Constants.PADDING_SIZE));
        paddingPanel.setBackground(new Color(87, 138, 52));

        paddingPanel.add(this, BorderLayout.CENTER);
        apg = new AppleGraphics();
        snkg = new SnakeGraphics();
        LOGGER.config("Created new GameGridPanel.");
    }

    /**
     * Gets a reference to the encompassing panel of the game grid.
     * @return a reference the {@code paddingPanel}
     */
    public JPanel getPaddingPanel() {
        return paddingPanel;
    }

    /**
     * Draws the grid with alternating colors and draws the apple and snake. If the game has ended, draw the dead snake.
     * @param g the {@code Graphics} object to protect that allows for only classes in the gui package to draw.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;
        // Enable anti-aliasing for smoother edges
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (int i = 0; i < Constants.NUM_ROWS; i += 1) {
            for (int j = 0; j < Constants.NUM_COLS; j += 1) {
                g2d.setColor((i + j) % 2 == 0 ? new Color(170, 215, 81) : new Color(162, 209, 73));
                g2d.fillRect(j * Constants.CELL_SIZE, i * Constants.CELL_SIZE,
                                  Constants.CELL_SIZE, Constants.CELL_SIZE);
            }
        }
        apg.drawApple(g2d, gameEngine.getFood());
        snkg.drawSnake(g2d, gameEngine.getSnake(), gameEngine.getFood());
        if (gameEngine.hasEnded())
            snkg.kill();
        LOGGER.finest("Repainted GameGridPanel.");
    }
}
