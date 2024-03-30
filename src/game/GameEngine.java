package game;

public class GameEngine {
    private boolean isRunning;
    private Snake snake;
    private Food food;
    private CollisionDetector cd;
    private int score;

    GameEngine() {
        isRunning = false;
    }
    public void startGame() {
        isRunning = true;
        snake = new Snake(Constants.SNAKE_INITIAL_POSITION, Constants.SNAKE_INITIAL_LENGTH);
        food = new Food(Constants.FOOD_INITIAL_POSITION);
        cd = new CollisionDetector();
        score = 0;
    }

    public void updateGame() {
        if (!isRunning)
            return;
        snake.setDirection();
        snake.move();
        if (cd.checkFoodCollision(snake, food)) {
            snake.grow();
            food.spawn(snake);
            score++;
        } else if (cd.checkWallCollision(snake) || cd.checkSelfCollision(snake)) {
            endGame();
        }
    }

    public void endGame() {
        isRunning = false;
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Snake getSnake() {
        return snake;
    }

    public Food getFood() {
        return food;
    }

    public int getScore() {
        return score;
    }

}
