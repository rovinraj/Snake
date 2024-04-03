package game.gui;


import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.event.ActionListener;
import java.awt.geom.RoundRectangle2D;

/**
 * A custom {@code JButton} used in the {@link StartMenuPanel} and {@link GameOverDialog}.
 */
public class RoundedButton extends JButton {

    /**
     * The corner radius of the rounded buttons.
     */
    private static final int radius = 20;

    /**
     * The default size of the buttons.
     */
    private static final Dimension BUTTON_SIZE = new Dimension(175, 40);

    /**
     * The icon to be displayed on the left side of the button.
     */
    private final ImageIcon icon;

    /**
     * Creates a button with the given text, icon, and behavior.
     * @param text The text that appears in the center of the button.
     * @param icon The icon that appears on the left side of the button.
     * @param e The behavior that the button has when pressed.
     */
    public RoundedButton(String text, ImageIcon icon, ActionListener e) {
        super(text);
        this.icon = icon;
        setForeground(Color.WHITE); // Text color
        setBackground(new Color(86, 136, 255)); // Button color
        setFocusPainted(false); // Removes the focus ring
        setBorderPainted(false); // We will paint our own border
        setContentAreaFilled(false); // Tells Swing to not fill the background, we paint it ourselves
        setFont(new Font("SansSerif", Font.BOLD, 16)); // Example font, customize as needed
        setMargin(new Insets(0, 10, 0, 10)); // Icon and text padding
        addActionListener(e);
    }

    /**
     * Draws the button with a different color depending on if the button is armed or not.
     * @param g the {@code Graphics} object to protect that allows for only classes in the gui package to draw.
     */
    @Override
    protected void paintComponent(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        if (getModel().isArmed()) {
            g2d.setColor(getBackground().darker()); // Button pressed color
        } else {
            g2d.setColor(getBackground());
        }
        g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
        super.paintComponent(g2d);

        // Draw the icon on the left
        int iconX = 5; // Horizontal margin from the left edge
        int iconY = (getHeight() - icon.getIconHeight()) / 2 + 2; // Vertically centered
        icon.paintIcon(this, g2d, iconX, iconY);

    }

    /**
     * Allows the default size of the button to be set.
     * @return the default button size.
     */
    @Override
    public Dimension getPreferredSize() {
        return BUTTON_SIZE;
    }
}
