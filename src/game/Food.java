package game;

import java.awt.Point;
import java.util.Random;

public class Food {
    private Point position;
    Food(Point initialPos) {
        position = initialPos;
    }
    public void spawn(Snake snake) {
        Random rand = new Random();
        Point pos;
        do {
            pos = new Point(rand.nextInt(Constants.NUM_COLS), rand.nextInt(Constants.NUM_ROWS));
        } while(snake.getSnake().contains(pos));
        position = pos;
    }

    public Point getPosition() {
        return position;
    }
}
