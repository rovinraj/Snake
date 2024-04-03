package game.core;

import game.LoggerSetup;
import game.utils.CollisionDetector;
import game.utils.Constants;

import javax.swing.*;
import java.util.logging.Logger;

/**
 * The {@code GameEngine} is used to store instances of all major
 * components of the current game loop and manages the state of the game.
 */
public class GameEngine {
    /**
     * Logger for logging information.
     * @hidden
     */
    private static final Logger LOGGER = LoggerSetup.getLogger(GameEngine.class.getName());
    /** Specifies whether the game is currently running. */
    private boolean isRunning;

    /**
     * The {@code Snake} object of the current game loop. Its initial position is determined by the
     * {@link Constants#SNAKE_INITIAL_POSITION} from the {@code Constants} class.
     */
    private Snake snake;

    /**
     * The {@code Food} object of the current game loop. Its initial position is determined by the
     * {@link Constants#FOOD_INITIAL_POSITION} from the {@code Constants} class.
     */
    private Food food;
    /**
     * The {@code CollisionDetector} object of the current game loop.
     * Checks interactions between game elements.
     */
    private CollisionDetector cd;

    /**
     * The current {@code score} of the player, as determined by the number of fruits eaten.
     */
    private int score;
    /**
     * An instance of the game loop {@code timer}.
     */
    private final Timer timer;

    /**
     * Creates a {@code GameEngine}. By default, {@code isRunning} is set to false.
     * @param timer an instance of the game loop timer so we can start and stop the game.
     */
    public GameEngine(Timer timer) {
        this.timer = timer;
        isRunning = false;
        LOGGER.config("GameEngine initialized.");
    }

    /**
     * Initializes the game elements initial values determined by the {@link Constants} file.
     * The game state is set to running and the score is set to 0.
     */
    public void startGame() {
        isRunning = true;
        snake = new Snake();
        food = new Food(snake);
        cd = new CollisionDetector(snake, food);
        score = 0;
        timer.start();
        LOGGER.config("Game started. Snake, Food, and CD initialized.");
    }

    /**
     * Updates the game state on each call. The game state updates by moving the {@code snake} and updating its
     * direction, and updating attributes based on a collision check. If the {@code snake} has run into itself or the
     * wall, {@code endGame} is called. If the {@code snake} has run into {@code food} then the {@code snake} will
     * grow, a new food will spawn, and the {@code score} will be incremented.
     */
    public void updateGame() {
        if (!isRunning) {
            LOGGER.fine("Game updated when not running.");
            return;
        }
        // keep old tail because when the snake grows the tail stays in one spot for a frame
        snake.move();
        LOGGER.finest("Snake moved and direction updated.");
        if (cd.checkFoodCollision()) {
            snake.grow();
            food.spawn();
            score++;
            LOGGER.finer("Snake ate food. Score is now " + score + ".");
        } else if (cd.checkWallCollision() || cd.checkSelfCollision()) {
            LOGGER.info("Collision Detected.");
            endGame();
        }
    }

    /**
     * If the game is running, ends the game by updating {@code isRunning} to {@code false}, moving the {@code snake}
     * backwards, and stopping the {@code timer}.
     */
    public void endGame() {
        if(!isRunning) {
            LOGGER.info("Attempted to end game while not running.");
            return;
        }
        snake.moveBackwards();
        isRunning = false;
        timer.stop();
        LOGGER.info("Game over. Final score " + score + ".");
    }

    /**
     * Returns {@code true} if the game has ended.
     * @return true if the game has ended, false otherwise.
     */
    public boolean hasEnded() {
        return !isRunning;
    }

    /**
     * Toggles the state of the timer. If the timer is running then it will stop, otherwise it will start.
     */
    public void togglePause() {
        if(timer.isRunning())
            timer.stop();
        else
            timer.start();
    }

    /**
     * Gets the instance of {@code snake} for the current game loop.
     * @return The current {@code snake} instance.
     */
    public Snake getSnake() {
        return snake;
    }

    /**
     * Gets the instance of {@code food} for the current game loop.
     * @return The current {@code food} instance.
     */
    public Food getFood() {
        return food;
    }

    /**
     * Gets the {@code score} of the current game loop.
     * @return The current score.
     */
    public int getScore() {
        return score;
    }
}
