package game.gui;

import game.LoggerSetup;
import game.utils.Constants;
import game.utils.Direction;
import game.utils.HighScoreManager;
import game.core.GameEngine;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

/**
 * The main panel for the snake game. Handles user input and manages all main UI elements.
 */
public class SnakePanel extends JPanel implements KeyListener, ActionListener {
    /**
     * Logger for logging information.
     * @hidden
     */
    private static final Logger LOGGER = LoggerSetup.getLogger(SnakePanel.class.getName());

    /**
     * The main instance of the {@code GameEngine}.
     */
    private final GameEngine gameEngine;

    /**
     * The panel that displays the play area.
     */
    private GameGridPanel gameGridPanel;

    /**
     * The panel that displays the score.
     */
    private HeaderPanel headerPanel;

    /**
     * The main instance of the {@code HighScoreManager}.
     */
    private final HighScoreManager hsm;

    /**
     * Sets up the game with first launch settings. A new timer is started, highscores are loaded, the start menu
     * opens, and the first frame of the game loads.
     */
    public SnakePanel() {
        Timer timer = new Timer(Constants.DELAY, this);
        gameEngine = new GameEngine(timer);
        hsm = new HighScoreManager("src/resources/data/highscores.csv");
        startMenu();
        initializeWindow();

        gameEngine.startGame();
        gameEngine.togglePause();
        LOGGER.config("Created new Snake Panel");
    }

    /**
     * Creates a new {@link StartMenuPanel}.
     */
    private void startMenu() {
        new StartMenuPanel(Constants.ICON, hsm, gameEngine);
    }


    /**
     * Sets the initial settings for the panel and creates a new {@link HeaderPanel} and {@link GameGridPanel}.
     */
    private void initializeWindow() {
        this.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        this.setLayout(new BorderLayout());
        this.setFocusable(true);
        addKeyListener(this);

        headerPanel = new HeaderPanel(resizeImage("src/resources/images/apple.png", 50, 50));
        this.add(headerPanel, BorderLayout.NORTH);

        gameGridPanel = new GameGridPanel(gameEngine);
        this.add(gameGridPanel.getPaddingPanel(), BorderLayout.CENTER);
    }

    /**
     * Repaints the panel.
     * @param g the {@code Graphics} object to protect that allows for only classes in the gui package to draw.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
    }

    /**
     * The main game loop. This method is run constantly while the timer is running. The refresh rate is based on
     * {@link Constants#DELAY}.
     * @param e the event to be processed (not used)
     */
    @Override
    public void actionPerformed(ActionEvent e) {
        gameEngine.updateGame();
        headerPanel.updateScore(gameEngine.getScore());
        gameGridPanel.repaint();
        repaint();
        if (gameEngine.hasEnded()) {
            new GameOverDialog(gameEngine, hsm);
        }
    }

    /**
     * @hidden
     * Run when a key is pressed and released (not used).
     * @param e the event to be processed
     */
    @Override
    public void keyTyped(KeyEvent e) {}

    /**
     * @hidden
     * Run when a key is released (not used).
     * @param e the event to be processed
     */
    @Override
    public void keyReleased(KeyEvent e) {}

    /**
     * When a key is pressed, determine if it is a key used in the game. If it is, begin the appropriate response.
     * @param e the event to be processed
     */
    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> gameEngine.getSnake().changeDirection(Direction.UP);
            case KeyEvent.VK_DOWN -> gameEngine.getSnake().changeDirection(Direction.DOWN);
            case KeyEvent.VK_LEFT -> gameEngine.getSnake().changeDirection(Direction.LEFT);
            case KeyEvent.VK_RIGHT -> gameEngine.getSnake().changeDirection(Direction.RIGHT);
            case KeyEvent.VK_P -> gameEngine.togglePause();
            case KeyEvent.VK_R -> {
                gameEngine.endGame();
                new GameOverDialog(gameEngine, hsm);
            }
        }
    }

    /**
     * A helper method used to resize images to a usable size.
     * @param path path to the image
     * @param width desired width of the image
     * @param height desired height of the image
     * @return a resized ImageIcon.
     */
    private ImageIcon resizeImage(String path, int width, int height) {
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File(path));
        } catch (IOException e) {
            e.printStackTrace();
        }
        if(img == null)
            return null;
        return new ImageIcon(img.getScaledInstance(width, height,
                Image.SCALE_SMOOTH));
    }
}
