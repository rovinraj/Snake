package game.gui;

import game.LoggerSetup;
import game.core.Food;
import game.core.Snake;
import game.utils.Constants;

import java.awt.*;
import java.util.List;
import java.util.logging.Logger;

/**
 * A class for drawing the snake onto the {@link GameGridPanel}.
 */
public class SnakeGraphics {
    /**
     * Logger for logging information.
     * @hidden
     */
    private static final Logger LOGGER = LoggerSetup.getLogger(SnakeGraphics.class.getName());
    /**
     * A reference to the Graphics2D of the {@code GameGridPanel}.
     */
    private Graphics2D g2d;

    /**
     * A reference to the current {@code Snake} object.
     */
    private Snake snake;

    /**
     * A reference to the current {@code Food} object.
     */
    private Food food;

    /**
     * Default constructor that creates the SnakeGraphic object.
     */
    SnakeGraphics() {}
    /**
     * Draws the current state of the snake using the given the {@link Graphics2D} of the panel to draw on.
     * @param g2d The graphics of the panel.F
     * @param snake The current reference to the snake. Used to draw all segments.
     * @param food The current reference to the food. Used to draw the snake's pupils in the direction of the food.
     */
    public void drawSnake(Graphics2D g2d, Snake snake, Food food) {
        this.g2d = g2d;
        this.snake = snake;
        this.food = food;
        drawBody();
        drawEyes(false);
        LOGGER.finer("Painted living snake");
    }

    /**
     * Draws the body of the {@code Snake}.
     */
    private void drawBody() {
        g2d.setColor(new Color(86, 136, 255));
        drawHead();
        drawTail();
        List<Point> body = snake.getSnake();
        for(int i = 1; i < body.size() - 1; i++) {
            Point cur = body.get(i);
            Point prev = body.get(i - 1);
            Point next = body.get(i + 1);
            int dx1 = cur.x - prev.x;
            int dy1 = cur.y - prev.y;
            int dx2 = next.x - cur.x;
            int dy2 = next.y - cur.y;
            // l->d and u->r = 0 (NW round)
            if ((dx1 == -1 && dy2 == 1) || (dy1 == -1 && dx2 == 1))
                drawSegment(cur, 0);
            // r->d and u->l = 1 (NE round)
            else if ((dx1 == 1 && dy2 == 1) || (dy1 == -1 && dx2 == -1))
                drawSegment(cur, 1);
            // l->u and d->r = 2 (SW round)
            else if ((dx1 == -1 && dy2 == -1) || (dy1 == 1 && dx2 == 1))
                drawSegment(cur, 2);
            // r->u and d->l = 3 (SE round)
            else if ((dx1 == 1 && dy2 == -1) || (dy1 == 1 && dx2 == -1))
                drawSegment(cur, 3);
            // (no round)
            else
                drawSegment(cur, -1);
        }
    }

    /**
     * Draws the head of the {@code Snake}.
     */

    private void drawHead() {
        Point head = snake.getSnake().get(0);
        Point prev = snake.getSnake().get(1);
        int dx = head.x - prev.x;
        int dy = head.y - prev.y;

        // Determine the start angle for the arc based on movement direction
        int startAngle = 0; // Moving up
        if (dx > 0) startAngle = 270; // Moving right
        else if (dx < 0) startAngle = 90;  // Moving left
        else if (dy > 0) startAngle = 180; // Moving down

        // Draw the arc for the head in the direction of movement
        g2d.fillArc(head.x * Constants.CELL_SIZE, head.y * Constants.CELL_SIZE,
                Constants.CELL_SIZE, Constants.CELL_SIZE, startAngle, 180);

        // Calculate the rectangle's starting position and size based on movement direction
        int roundingOffset = Constants.CELL_SIZE % 2;
        int x = head.x*Constants.CELL_SIZE + (dx < 0 ? Constants.CELL_SIZE/2: 0);
        int y = head.y*Constants.CELL_SIZE + (dy < 0 ? Constants.CELL_SIZE/2: 0);
        int width = dx == 0 ? Constants.CELL_SIZE : (Constants.CELL_SIZE/2 + (dx < 0 ? 2 * roundingOffset : roundingOffset));
        int height = dy == 0 ? Constants.CELL_SIZE : (Constants.CELL_SIZE/2 + (dy < 0 ? 2 * roundingOffset : roundingOffset));

        // Draw the rectangle part of the head
        g2d.fillRect(x, y, width, height);
    }

    /**
     * Draws the tail of the {@code Snake}.
     */
    private void drawTail() {
        Point tail = snake.getSnake().get(snake.getSnake().size() - 1);
        Point next = snake.getSnake().get(snake.getSnake().size() - 2);
        int dx = next.x - tail.x;
        int dy = next.y - tail.y;

        // Determine the start angle for the arc based on movement direction
        int startAngle = 0; // Moving down
        if (dx > 0)
            startAngle = 90;  // Moving right
        else if (dx < 0)
            startAngle = 270; // Moving left
        else if (dy < 0)
            startAngle = 180;   // Moving up

        // Draw the arc for the tail in the direction of movement
        g2d.fillArc(tail.x * Constants.CELL_SIZE, tail.y * Constants.CELL_SIZE,
                Constants.CELL_SIZE, Constants.CELL_SIZE, startAngle, 180);

        // Calculate the rectangle's starting position and size based on movement direction
        int roundingOffset = Constants.CELL_SIZE % 2;
        int x = tail.x*Constants.CELL_SIZE + (dx > 0 ? Constants.CELL_SIZE/2 : 0);
        int y = tail.y*Constants.CELL_SIZE + (dy > 0 ? Constants.CELL_SIZE/2 : 0);
        int width = dx == 0 ? Constants.CELL_SIZE : (Constants.CELL_SIZE/2 + (dx < 0 ? 2 * roundingOffset : roundingOffset));
        int height = dy == 0 ? Constants.CELL_SIZE : (Constants.CELL_SIZE/2 + (dy < 0 ? 2 * roundingOffset : roundingOffset));

        // Draw the rectangle part of the tail
        g2d.fillRect(x, y, width, height);
    }


    /**
     * Draws a segment of the {@code Snake}. A segment is any part that is not the head or tail.
     * @param p The position of the segment
     * @param round what corner is round. 0 -> top left, 1 -> top right, 2 -> bottom left, 3-> bottom right.
     */
    private void drawSegment(Point p, int round) {
        int cornerSize = Constants.CELL_SIZE/2;
        int x = p.x * Constants.CELL_SIZE;
        int y = p.y * Constants.CELL_SIZE;
        int roundingOffset = Constants.CELL_SIZE % 2; // if CELL_SIZE is an odd number, there will be rounding errors

        // Draw each corner with conditional rounding
        for (int i = 0; i < 4; i++) {
            switch (i) {
                case 0 -> { // Top-Left
                    if (round == 0)
                        g2d.fillArc(x, y, Constants.CELL_SIZE + roundingOffset, Constants.CELL_SIZE + roundingOffset, 90, 90);
                    else
                        g2d.fillRect(x, y, cornerSize + roundingOffset, cornerSize + roundingOffset);
                }
                case 1 -> { // Top-Right
                    if (round == 1)
                        g2d.fillArc(x, y, Constants.CELL_SIZE, Constants.CELL_SIZE + roundingOffset, 0, 90);
                    else
                        g2d.fillRect(x + cornerSize + roundingOffset, y, cornerSize, cornerSize + roundingOffset);
                }
                case 2 -> { // Bottom-Left
                    if (round == 2)
                        g2d.fillArc(x, y, Constants.CELL_SIZE + roundingOffset, Constants.CELL_SIZE, 180, 90);
                    else
                        g2d.fillRect(x, y + cornerSize + roundingOffset, cornerSize + roundingOffset, cornerSize);
                }
                case 3 -> { // Bottom-Right
                    if (round == 3)
                        g2d.fillArc(x, y, Constants.CELL_SIZE, Constants.CELL_SIZE, 270, 90);
                    else
                        g2d.fillRect(x + cornerSize + roundingOffset, y + cornerSize + roundingOffset, cornerSize, cornerSize);
                }
            }
        }

    }

    /**
     * Draws the eyes of the snake. The eye sockets are always positioned at the same place on the head, however the
     * pupils follow the {@code Food}'s current location. If the {@code Snake} has died, X's will be drawn instead.
     * @param dead the state of the snake. If the {@code Snake} is dead then true, otherwise false.
     */
    private void drawEyes(boolean dead) {
        // Paint the snake head
        Point head = snake.getSnake().get(0);

        // Set color for eyes

        int eyeSize = Constants.CELL_SIZE / 3;
        int offsetX = Constants.CELL_SIZE / 3;
        int offsetY = Constants.CELL_SIZE / 3;
        int pupilOffset = eyeSize / 2;

        // Calculate eye positions
        int leftEyeX = head.x * Constants.CELL_SIZE + offsetX - (eyeSize / 2);
        int rightEyeX = head.x * Constants.CELL_SIZE + 2 * offsetX - (eyeSize / 2);
        int eyeY = head.y * Constants.CELL_SIZE + offsetY - (eyeSize / 2);

        if(dead) {
            g2d.setStroke(new BasicStroke(3));
            g2d.setColor(Color.BLACK);
            // Draw left eye as an X
            g2d.drawLine(leftEyeX, eyeY, leftEyeX + eyeSize, eyeY + eyeSize);
            g2d.drawLine(leftEyeX, eyeY + eyeSize, leftEyeX + eyeSize, eyeY);

            // Draw right eye as an X
            g2d.drawLine(rightEyeX, eyeY, rightEyeX + eyeSize, eyeY + eyeSize);
            g2d.drawLine(rightEyeX, eyeY + eyeSize, rightEyeX + eyeSize, eyeY);
            g2d.setStroke(new BasicStroke(1));
            return;
        }


        // Draw eyes
        g2d.setColor(Color.WHITE);
        g2d.fillOval(leftEyeX, eyeY, eyeSize, eyeSize);
        g2d.fillOval(rightEyeX, eyeY, eyeSize, eyeSize);

        // Find the angle between the food and the head in radians
        // theta = arctan((y2 - y1) / (x2 - x1))
        double fruitHeadAngle = Math.atan2(food.getPosition().y - head.y, food.getPosition().x - head.x);

        // Find the length of the sides of the 45-45-90 triangle where the hypotenuse is the radius of the pupil offset
        // hyp = side*sqrt2 -> side = hyp/sqrt2
        double pupilDisplacement = pupilOffset / Math.sqrt(2);

        // Find the displacement from the center of the eye using the angle and the previously calculated side.
        // cos(theta) = a/h -> cos(theta)*h = a
        // sin(theta> = o/h -> sin(theta)*h = o
        int pupilLookX = (int) (Math.cos(fruitHeadAngle) * pupilDisplacement);
        int pupilLookY = (int) (Math.sin(fruitHeadAngle) * pupilDisplacement);

        // Set color for pupils and draw them
        g2d.setColor(Color.BLACK);
        int pupilSize = (int) (eyeSize * 0.75);

        int pupilLX = leftEyeX + (eyeSize - pupilSize) / 2 + pupilLookX;
        int pupilLY = eyeY + (eyeSize - pupilSize) / 2 + pupilLookY;
        int pupilRX = rightEyeX + (eyeSize - pupilSize) / 2 + pupilLookX;
        int pupilRY = eyeY + (eyeSize - pupilSize) / 2 + pupilLookY;

        g2d.fillOval(pupilLX, pupilLY, pupilSize, pupilSize);
        g2d.fillOval(pupilRX, pupilRY, pupilSize, pupilSize);
        g2d.setColor(new Color(86, 136, 255));
    }

    /**
     * Draws the dead variant of the {@code Snake}.
     */
    public void kill() {
        drawBody();
        drawEyes(true);
        LOGGER.fine("Painted dead snake");
    }
}