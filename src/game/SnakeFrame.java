package game;


import java.awt.EventQueue;
import javax.swing.JFrame;


public class SnakeFrame extends JFrame {

    public static void main(String[] args) {
        EventQueue.invokeLater(() -> {
            try {
                new SnakeFrame().setVisible(true);
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
    }

    public SnakeFrame() {
        this.setTitle("Snake");
        setIconImage(Constants.ICON);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setLayout(null);
        setContentPane(new SnakePanel());
        pack();
        this.setLocationRelativeTo(null);
    }

}
