package game;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class SnakePanel extends JPanel implements KeyListener, ActionListener
{

    private final GameEngine gameEngine;
    private Timer timer;
    private JPanel gameGridPanel;
    private JLabel score;

    public SnakePanel() {
        initializeWindow();
        gameEngine = new GameEngine();
        startGame();
    }

    private void startGame() {
        gameEngine.startGame();
        timer = new Timer(Constants.DELAY, this);
        timer.start();
    }

    private void initializeWindow() {
        // Sets panel settings

        this.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEIGHT));
        this.setLayout(new BorderLayout());
        this.setFocusable(true);
        addKeyListener(this);

        // Creates header panel
        JPanel headerPanel = new JPanel();
        headerPanel.setBackground(new Color(74, 117, 44));
        headerPanel.setPreferredSize(new Dimension(Constants.WIDTH, Constants.HEADER_HEIGHT));
        this.add(headerPanel, BorderLayout.NORTH);

        headerPanel.setLayout(new FlowLayout(FlowLayout.LEFT));


        // resize images
        BufferedImage img = null;
        try {
            img = ImageIO.read(new File("src/data/apple.png"));
        } catch (IOException e) {
            e.printStackTrace();
        }

        ImageIcon headerApple = new ImageIcon(img.getScaledInstance(50, 50,
                Image.SCALE_SMOOTH));
        ImageIcon gameApple = new ImageIcon(img.getScaledInstance(Constants.CELL_SIZE, Constants.CELL_SIZE,
                Image.SCALE_SMOOTH));

        JLabel fruits = new JLabel(headerApple);
        score = new JLabel("0");
        score.setFont(new Font("SansSerif", Font.BOLD, 16));
        headerPanel.add(fruits);
        headerPanel.add(score);



        // Creates a grid based on size parameters
        // Panel within a panel so there's padding around the grid
        JPanel paddingPanel = new JPanel(new BorderLayout());
        paddingPanel.setBorder(BorderFactory.createEmptyBorder(Constants.PADDING_SIZE, Constants.PADDING_SIZE,
                                                               Constants.PADDING_SIZE, Constants.PADDING_SIZE));
        paddingPanel.setBackground(new Color(87, 138, 52));

        gameGridPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                for (int i = 0; i < Constants.NUM_ROWS; i += 1) {
                    for (int j = 0; j < Constants.NUM_COLS; j += 1) {
                        g.setColor((i + j) % 2 == 0 ? new Color(170, 215, 81) : new Color(162, 209, 73));
                        g.fillRect(j*Constants.CELL_SIZE, i*Constants.CELL_SIZE, Constants.CELL_SIZE, Constants.CELL_SIZE);
                    }
                }
                g.setColor(Color.BLUE);
                Snake snake = gameEngine.getSnake();
                for(Point p : snake.getSnake()) {
                    g.fillRect(p.x*Constants.CELL_SIZE,
                            p.y*Constants.CELL_SIZE,
                            Constants.CELL_SIZE, Constants.CELL_SIZE);
                }

                g.setColor(Color.RED);
                Point food = gameEngine.getFood().getPosition();
                gameApple.paintIcon(this, g, food.x*Constants.CELL_SIZE,
                        food.y*Constants.CELL_SIZE);
            }
        };

        paddingPanel.add(gameGridPanel, BorderLayout.CENTER);
        this.add(paddingPanel, BorderLayout.CENTER);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

    }
    @Override
    public void actionPerformed(ActionEvent e) {
        gameEngine.updateGame();
        score.setText(gameEngine.getScore() + "");
        if (!gameEngine.isRunning()) {
            timer.stop();
            showGameOverDialog(); // Show the game over dialog
        } else {
            gameGridPanel.repaint(); // Redraw the panel to reflect updates
            repaint();
        }
    }

    private void showGameOverDialog() {
        // Create a JDialog that shows a game over message
        JDialog gameOverDialog = new JDialog();
        gameOverDialog.setIconImage(Constants.ICON);

        gameOverDialog.setModal(true);
        gameOverDialog.setTitle("Game Over");
        gameOverDialog.setSize(300, 375);
        gameOverDialog.setLayout(new BorderLayout());
        gameOverDialog.setLocationRelativeTo(this); // Center on screen

        JLabel messageLabel = new JLabel("Game Over!", SwingConstants.CENTER);
        messageLabel.setFont(new Font("SansSerif", Font.BOLD, 40));
        JTextField nameField = new JTextField(10); // Field for entering name
        nameField.setHorizontalAlignment(JTextField.CENTER); // Center text in the JTextField

        RoundedButton saveScoreButton = new RoundedButton("Save Score", new ImageIcon("src/data/save.png"));
        RoundedButton restartButton = new RoundedButton("Play again", new ImageIcon("src/data/play.png"));
        RoundedButton lbButton = new RoundedButton("Leaderboard", new ImageIcon("src/data/lb.png"));
        RoundedButton exitButton = new RoundedButton("Exit", new ImageIcon("src/data/quit.png"));

        saveScoreButton.addActionListener(e -> {
            String playerName = nameField.getText();
            if (!playerName.isBlank()) {
                HighScoreManager hsm = new HighScoreManager("src/data/highscores.csv");
                hsm.saveHighScore(gameEngine.getScore(), playerName);
                showLeaderBoard();
                gameOverDialog.dispose();
            } else {
                JOptionPane.showMessageDialog(gameOverDialog, "Please enter a name.", "Name Required", JOptionPane.WARNING_MESSAGE);
            }
        });

        restartButton.addActionListener(e -> {
            gameEngine.startGame(); // Restart game logic
            timer.start(); // Restart game timer
            gameOverDialog.dispose(); // Close dialog
        });

        lbButton.addActionListener(e -> {
            showLeaderBoard();
            gameOverDialog.dispose();
        });

        exitButton.addActionListener(e -> {
            System.exit(0); // Exit application
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS));

        addButton(buttonPanel, nameField);
        addButton(buttonPanel, saveScoreButton);
        addButton(buttonPanel, restartButton);
        addButton(buttonPanel, lbButton);
        addButton(buttonPanel, exitButton);

        gameOverDialog.add(messageLabel, BorderLayout.CENTER);
        gameOverDialog.add(buttonPanel, BorderLayout.SOUTH);

        gameOverDialog.setVisible(true);
    }

    private void addButton(JPanel buttonPanel, JComponent jc) {
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.CENTER));
        panel.add(jc);
        buttonPanel.add(panel);
    }

    private void showLeaderBoard() {
        HighScoreManager hsm = new HighScoreManager("src/data/highscores.csv");

        // Column Names
        String[] columnNames = {"Rank", "Name", "Score", "Date"};

        // Convert high score list to a 2D array for the table model
        String[][] data = new String[hsm.hslist().size()][4];
        for (int i = 0; i < hsm.hslist().size(); i++) {
            data[i][0] = String.valueOf(i + 1); // Rank
            data[i][1] = hsm.hslist().get(i).name();
            data[i][2] = String.valueOf(hsm.hslist().get(i).score());
            data[i][3] = hsm.hslist().get(i).date().toString();
        }

        // Create Table Model
        DefaultTableModel model = new DefaultTableModel(data, columnNames) {
            @Override
            public boolean isCellEditable(int row, int column) {
                // Prevent editing cells
                return false;
            }
        };

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
        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                frame.dispose();
                showGameOverDialog();
            }
        });
        frame.setIconImage(Constants.ICON);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        frame.add(scrollPane);
        frame.pack(); // Adjust frame size to contents
        frame.setLocationRelativeTo(null); // Center on screen
        frame.setVisible(true);
        frame.toFront();
        frame.requestFocus();

        frame.setAlwaysOnTop(true);


    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyPressed(KeyEvent e) {
        switch (e.getKeyCode()) {
            case KeyEvent.VK_UP -> gameEngine.getSnake().changeDirection(Direction.UP);
            case KeyEvent.VK_DOWN -> gameEngine.getSnake().changeDirection(Direction.DOWN);
            case KeyEvent.VK_LEFT -> gameEngine.getSnake().changeDirection(Direction.LEFT);
            case KeyEvent.VK_RIGHT -> gameEngine.getSnake().changeDirection(Direction.RIGHT);
            case KeyEvent.VK_R -> gameEngine.endGame();
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}
}
