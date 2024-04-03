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
 * Custom {@code JDialog} that is visible every time the game ends.
 */
public class GameOverDialog extends JDialog {
    /**
     * Logger for logging information.
     * @hidden
     */
    private static final Logger LOGGER = LoggerSetup.getLogger(GameOverDialog.class.getName());
    /**
     * An instance of the current {@code GameEngine}.
     */
    private final GameEngine gameEngine;
    /**
     * An instance of the current {@code HighScoreManager}.
     */
    private final HighScoreManager hsm;

    /**
     * Creates a new {@code GameOverDialog}.
     * @param gameEngine a reference to the current {@code GameEngine}.
     * @param hsm a reference to the current {@code HighScoreManager}.
     */
    public GameOverDialog(GameEngine gameEngine, HighScoreManager hsm) {
        LOGGER.fine("New GameOverDialog created.");
        this.gameEngine = gameEngine;
        this.hsm = hsm;
        initalizeDialog();
        addButtons();
        setVisible(true);
    }

    /**
     * Initializes all settings for the {@code JDialog}.
     */
    private void initalizeDialog() {
        setIconImage(Constants.ICON);

        setModal(true);
        setTitle("Game Over");
        setSize(300, 375);
        setLayout(new BorderLayout());
        setLocationRelativeTo(null);
        setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);

        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                dispose();
                System.exit(0);
            }
        });
    }

    /**
     * Adds and configures all {@code JButtons} and {@code JTextFields} for the dialog.
     */
    private void addButtons() {
        JLabel messageLabel = new JLabel("Game Over!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
        JTextField nameField = new JTextField(10); // Field for entering name
        nameField.setHorizontalAlignment(JTextField.CENTER); // Center text in the JTextField

        RoundedButton saveScoreButton = new RoundedButton("Save Score",
                new ImageIcon("src/resources/images/save.png"),
                (e -> saveScore(nameField.getText())));
        RoundedButton restartButton = new RoundedButton("Play Again",
                new ImageIcon("src/resources/images/play.png"),
                (e -> restartGame()));
        RoundedButton lbButton = new RoundedButton("Leaderboard",
                new ImageIcon("src/resources/images/lb.png"),
                (e -> showLeaderBoard()));
        RoundedButton exitButton = new RoundedButton("Exit",
                new ImageIcon("src/resources/images/quit.png"),
                (e -> endGame()));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        addPanels(buttonPanel, nameField, saveScoreButton, restartButton, lbButton, exitButton);

        add(messageLabel, BorderLayout.CENTER);
        add(buttonPanel, BorderLayout.SOUTH);
    }

    /**
     * Helper method to add multiple {@code JComponents} to a {@code JPanel} each within their own {@code JPanel}.
     * @param buttonPanel the main {@code JPanel} that the rest of the {@code JComponents} will be added to.
     * @param jc a varargs parameter({@code JComponent}...) that allows for multiple {@code JComponents} to be added
     *           each as their own parameter.
     */
    private void addPanels(JPanel buttonPanel, JComponent... jc) {
        for (JComponent j : jc) {
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER));
            panel.add(j);
            buttonPanel.add(panel);
        }
    }

    /**
     * Logic for the save score button on the {@code GameOverDialog}. Saves the score with the player name entered
     * in the {@code JTextField}. If the name is blank, show a {@code JOptionPane} telling the user to enter a name.
     * @param playerName the name of the player.
     */
    private void saveScore(String playerName) {
        if (!playerName.isBlank()) { // checks if name only contains whitespace
            hsm.saveHighScore(gameEngine.getScore(), playerName);
            showLeaderBoard();
        } else {
            JOptionPane.showMessageDialog(this, "Please enter a name.", "Name Required", JOptionPane.WARNING_MESSAGE);
        }
        LOGGER.finer("Save Score button pressed");
    }

    /**
     * Logic for the restart button. Starts a new game and closes this window.
     */
    private void restartGame() {
        gameEngine.startGame(); // Restart game logic
        this.dispose(); // Close dialog
        LOGGER.finer("Play Again button pressed.");
    }

    /**
     * Logic for exit button. Ends the program.
     */
    private void endGame() {
        LOGGER.finer("Exit button pressed.");
        System.exit(0);
    }

    /**
     * Logic for the leaderboard button. Creates a new {@link Leaderboard}.
     */
    private void showLeaderBoard() {
        new Leaderboard(this, hsm);
        LOGGER.finer("Leaderboard button pressed.");
    }

    /**
     * Creates a new {@code GameOverDialog}. Used when exiting the leaderboard.
     */
    public void refresh() {
        new GameOverDialog(gameEngine, hsm);
    }
}
