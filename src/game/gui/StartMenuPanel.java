package game.gui;

import game.LoggerSetup;
import game.core.GameEngine;
import game.utils.Constants;
import game.utils.HighScoreManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

/**
 * The menu that appears at the start of the game.
 */
public class StartMenuPanel extends JPanel {
    /**
     * Logger for logging information.
     * @hidden
     */
    private static final Logger LOGGER = LoggerSetup.getLogger(StartMenuPanel.class.getName());
    /**
     * The {@code JFrame} that encompasses this panel.
     */
    private JFrame parent;

    /**
     * The snake icon that appears in the center of the start menu.
     */
    private final Image icon;

    /**
     * A reference of the {@code HighScoreManager} used to create a leaderboard if needed.
     */
    private final HighScoreManager hsm;

    /**
     * A reference to the {@code GameEngine} used when the game should start.
     */
    private final GameEngine gameEngine;

    /**
     * Creates a new {@code StartMenuPanel} when the program is first run.
     * @param unscaledIcon the main snake icon that is resized within the constructor.
     * @param hsm a reference to the {@code HighScoreManager}.
     * @param gameEngine a reference to the {@code gameEngine}.
     */
    StartMenuPanel(Image unscaledIcon, HighScoreManager hsm, GameEngine gameEngine) {
        this.hsm = hsm;
        this.gameEngine = gameEngine;
        setLayout(new BorderLayout());
        icon = unscaledIcon.getScaledInstance(200, 200, Image.SCALE_SMOOTH);
        initializeFrame();
        initializePanel();
        LOGGER.info("Created new StartMenuPanel");
    }

    /**
     * Creates the parent frame for this panel to sit in.
     */
    private void initializeFrame() {
        parent = new JFrame("Snake");
        parent.setIconImage(Constants.ICON);
        parent.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        parent.setSize(300, 375);
        parent.setLayout(new BorderLayout());
        parent.setLocationRelativeTo(null);
        parent.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        parent.setAlwaysOnTop(true);

        parent.setContentPane(this);

        parent.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                parent.dispose();
                System.exit(0);
            }
        });
        parent.setVisible(true);
    }

    /**
     * Sets initial settings for the current panel and adds {@link RoundedButton}s for options.
     */
    private void initializePanel() {
        RoundedButton restartButton = new RoundedButton("Play",
                new ImageIcon("src/resources/images/play.png"),
                (e -> playGame()));
        RoundedButton lbButton = new RoundedButton("Leaderboard",
                new ImageIcon("src/resources/images/lb.png"),
                (e -> showLeaderBoard()));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(restartButton);
        buttonPanel.add(panel);
        panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        panel.add(lbButton);
        buttonPanel.add(panel);
        this.add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Logic for leaderboard button. Creates a new leaderboard.
     */
    private void showLeaderBoard() {
        new Leaderboard(this, hsm);
        LOGGER.fine("Leaderboard button pressed.");
    }

    /**
     * Logic for the start game button. Toggles pause and disposes of the current panel.
     */
    private void playGame() {
        gameEngine.togglePause();
        dispose();
        LOGGER.fine("Start game button pressed.");
    }

    /**
     * Repaints the panel.
     * @param g the {@code Graphics} object to protect that allows for only classes in the gui package to draw.
     */
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int x = (getWidth() - icon.getWidth(null)) / 2;
        int y = (getHeight() - icon.getHeight(null)) / 10;
        g.drawImage(icon, x, y, this);
    }

    /**
     * Disposes of the {@code parent} frame.
     */
    public void dispose() {
        parent.dispose();
    }

    /**
     * Creates a new {@code StartMenuPanel}.
     */
    public void refresh() {
        new StartMenuPanel(icon, hsm, gameEngine);
    }
}
