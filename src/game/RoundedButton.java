package game;


import javax.swing.JButton;
import javax.swing.ImageIcon;
import java.awt.Dimension;
import java.awt.Color;
import java.awt.Font;
import java.awt.Insets;
import java.awt.Graphics2D;
import java.awt.Graphics;
import java.awt.geom.RoundRectangle2D;

public class RoundedButton extends JButton {

    private static final int radius = 20;
    private static final Dimension BUTTON_SIZE = new Dimension(175, 40);

    ImageIcon icon;
    public RoundedButton(String text, ImageIcon icon) {
        super(text);
        this.icon = icon;
        setForeground(Color.WHITE); // Text color
        setBackground(new Color(17, 85, 204)); // Button color
        setFocusPainted(false); // Removes the focus ring
        setBorderPainted(false); // We will paint our own border
        setContentAreaFilled(false); // Tells Swing to not fill the background, we paint it ourselves
        setFont(new Font("SansSerif", Font.BOLD, 16)); // Example font, customize as needed
        setMargin(new Insets(0, 10, 0, 10)); // Icon and text padding
    }

    @Override
    protected void paintComponent(Graphics g) {
        if (getModel().isArmed()) {
            g.setColor(getBackground().darker()); // Button pressed color
        } else {
            g.setColor(getBackground());
        }
        Graphics2D g2d = (Graphics2D) g;
        g2d.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), radius, radius));
        super.paintComponent(g);

        // Draw the icon on the left
        int iconX = 5; // Horizontal margin from the left edge
        int iconY = (getHeight() - icon.getIconHeight()) / 2 + 2; // Vertically centered
        icon.paintIcon(this, g, iconX, iconY);

    }

    @Override
    public Dimension getPreferredSize() {
        return BUTTON_SIZE;
    }



}
