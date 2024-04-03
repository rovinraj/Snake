package game.gui;

import game.LoggerSetup;
import game.utils.Constants;
import game.utils.HighScoreManager;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.logging.Logger;

/**
 * Used to manage and display the leaderboard.
 */
public class Leaderboard {
    /**
     * Logger for logging information.
     * @hidden
     */
    private static final Logger LOGGER = LoggerSetup.getLogger(GameOverDialog.class.getName());
    /**
     * Reference to the {@code GameOverDialog} window. Used to open the game over dialog when Leaderboard is closed.
     */
    private GameOverDialog gameOverDialog;
    /**
     * Reference to the {@code StartMenuPanel} window. Used to open the start menu when Leaderboard is closed.
     */
    private StartMenuPanel startMenuPanel;

    /**
     * Reference to the {@code HighScoreManager}. Used to load all data into a table.
     */
    private final HighScoreManager hsm;

    /**
     * Creates a leaderboard from the {@code GameOverDialog} menu.
     * @param gameOverDialog a reference to the window, so it may be closed and reopened.
     * @param hsm a reference to the {@code HighScoreManager} to load data.
     */
    public Leaderboard(GameOverDialog gameOverDialog, HighScoreManager hsm) {
        this(hsm);
        this.gameOverDialog = gameOverDialog;
        gameOverDialog.dispose();
        LOGGER.fine("Display Leaderboard after game over.");
    }
    /**
     * Creates a leaderboard from the {@code StartMenuPanel} menu.
     * @param startMenuPanel a reference to the window, so it may be closed and reopened.
     * @param hsm a reference to the {@code HighScoreManager} to load data.
     */
    public Leaderboard(StartMenuPanel startMenuPanel, HighScoreManager hsm) {
        this(hsm);
        this.startMenuPanel = startMenuPanel;
        startMenuPanel.dispose();
        LOGGER.fine("Display Leaderboard after start.");
    }

    /**
     * default constructor that is always called. Sets up the Leaderboard.
     * @param hsm a reference to the {@code HighScoreManager} to load data.
     */
    private Leaderboard(HighScoreManager hsm) {
        this.hsm = hsm;
        gameOverDialog = null;
        startMenuPanel = null;
        DefaultTableModel table = createTable();
        displayLeaderboard(table);
    }

    /**
     * Creates a {@code JFrame} to display the leaderboard as a {@link JScrollPane}.
     * @param model a table representing all highscore information.
     */
    private void displayLeaderboard(DefaultTableModel model) {
        // Create Table
        JTable table = new JTable(model);

        // Adjust Table Appearance
        table.setFillsViewportHeight(true);
        table.setRowHeight(30);
        table.setFont(new Font("SansSerif", Font.PLAIN, 18));

        // Scroll Pane
        JScrollPane scrollPane = new JScrollPane(table);

        // Create JFrame to display the table
        JFrame frame = new JFrame("Leaderboard");
        frame.add(scrollPane);

        // if the leaderboard is closed, open up previous window
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                if(gameOverDialog != null)
                    gameOverDialog.refresh();
                if(startMenuPanel != null) {
                    startMenuPanel.refresh();
                }
            }
        });
        frame.setIconImage(Constants.ICON);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
        frame.setAlwaysOnTop(true);
    }

    /**
     * Generates a {@code DefaultTableModel} based on the data loaded by the {@code HighScoreManager}.
     * @return a table with all the highscore data, ready to be displayed.
     */
    private DefaultTableModel createTable() {
        // Column Names
        String[] columnNames = {"Rank", "Name", "Score", "Date"};

        // Convert high score list to a 2D array for the table model
        String[][] data = new String[hsm.hslist().size()][4];
        for (int i = 0; i < hsm.hslist().size(); i++) {
            data[i][0] = String.valueOf(i + 1);
            data[i][1] = hsm.hslist().get(i).name();
            data[i][2] = String.valueOf(hsm.hslist().get(i).score());
            data[i][3] = hsm.hslist().get(i).date().toString();
        }

        // Create Table Model that is not editable
        return new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
    }

}
