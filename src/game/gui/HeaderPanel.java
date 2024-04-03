package game.gui;

import game.LoggerSetup;
import game.utils.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.logging.Logger;

/**
 * A {@code JPanel} for the header of the main game window.
 */
public class HeaderPanel extends JPanel {
    /**
     * Logger for logging information.
     * @hidden
     */
    private static final Logger LOGGER = LoggerSetup.getLogger(HeaderPanel.class.getName());
    /**
     * A {@code JLabel} that holds the apple icon and the score.
     */
    private final JLabel scoreLabel;

    /**
     * Creates new header panel with the score and the apple icon
     * @param apple an {@code ImageIcon} of a small apple
     */
    public HeaderPanel(ImageIcon apple) {
        setBackground(new Color(74, 117, 44));
        setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEADER_HEIGHT));
        setLayout(new FlowLayout(FlowLayout.LEFT));

        scoreLabel = new JLabel("0");
        scoreLabel.setFont(new Font("SansSerif", Font.BOLD, 16));

        add(new JLabel(apple));
        add(scoreLabel);
        LOGGER.fine("Created new HeaderPanel.");
    }

    /**
     * Updates the score to the new score.
     * @param newScore the new value of the score.
     */
    public void updateScore(int newScore) {
        scoreLabel.setText(String.valueOf(newScore));
    }
}
