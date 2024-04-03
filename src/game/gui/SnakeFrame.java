package game.gui;


import game.LoggerSetup;
import game.utils.Constants;

import java.awt.EventQueue;
import java.util.logging.Logger;
import javax.swing.JFrame;

/**
 * The main {@code JFrame} of the snake game.
 */
public class SnakeFrame extends JFrame {
    /**
     * Logger for logging information.
     * @hidden
     */
    private static final Logger LOGGER = LoggerSetup.getLogger(SnakeFrame.class.getName());

    /**
     * The main method that launches the snake game.
     * @param args Command line arguments (not used).
     */
    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new SnakeFrame().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    /**
     * Constructs a new snake frame by initializing its properties and adding a new {@link SnakePanel}.
     */
    public SnakeFrame() {
        this.setTitle("Snake");
        setIconImage(Constants.ICON);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        setContentPane(new SnakePanel());
        pack();
        this.setLocationRelativeTo(null);
        LOGGER.config("New SnakeFrame created");
    }

}
