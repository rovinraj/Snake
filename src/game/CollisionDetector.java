package game;


import java.awt.Point;

public class CollisionDetector {
    // Checks if the snake has collided with the walls
    public boolean checkWallCollision(Snake snake) {
        Point head = snake.getSnake().get(0);
        return head.x < 0 || head.x >= Constants.NUM_COLS || head.y < 0 || head.y >= Constants.NUM_ROWS;
    }

    // Checks if the snake has collided with fruit
    public boolean checkFoodCollision(Snake snake, Food food) {
        Point head = snake.getSnake().get(0);
        return head.equals(food.getPosition());
    }

    // Checks if the snake has collided with itself
    public boolean checkSelfCollision(Snake snake) {
        Point head = snake.getSnake().get(0);
        for (int i = 1; i < snake.getSnake().size(); i++) {
            if (head.equals(snake.getSnake().get(i))) {
                return true;
            }
        }
        return false;
    }

}
