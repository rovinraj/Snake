package game.utils;

import game.LoggerSetup;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Class used to load and save highscores to a given csv file.
 */
public class HighScoreManager {
    /**
     * Logger for logging information.
     * @hidden
     */
    private static final Logger LOGGER = LoggerSetup.getLogger(HighScoreManager.class.getName());

    /**
     * A list containing all saved highscores.
     */
    private final List<HighScore> highscores;
    /**
     * The path to the highscores file.
     */
    private final String path;

    /**
     * Constructs a HighScoreManger and loads highscores from the given csv file name.
     * @param path the path to the highscores file.
     * @throws IllegalArgumentException if the file found at the path is not of type .csv
     */
    public HighScoreManager(String path) {
        if (!path.endsWith(".csv")) {
            LOGGER.severe("The file at the path provided was not of type .csv. Path: " + path);
            throw new IllegalArgumentException("The file must be of type .csv");
        }
        this.path = path;
        highscores = new ArrayList<>();
        loadHighScores();
    }

    /**
     * Loads all highscores saved in the csv file into a List. Each line is read by a {@link BufferedReader}
     * and is then split by every ','. Each token is then parsed into the correct data type and
     * stored as a HighScore record within the list.
     */
    private void loadHighScores() {
        try (BufferedReader br = new BufferedReader(new FileReader(path))) {
            String line;
            while ((line = br.readLine()) != null) {
                try {
                    String[] tokens = line.split(",");
                    int score = Integer.parseInt(tokens[0]);
                    String name = tokens[1];
                    LocalDate date = LocalDate.parse(tokens[2]);
                    highscores.add(new HighScore(score, name, date));
                } catch (DateTimeParseException | NumberFormatException e) {
                    LOGGER.log(Level.WARNING, "Skipping invalid high score entry: " + line, e);
                }
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load high scores from file: " + path, e);
        }
    }

    /**
     * Saves a new {@code HighScore} if it's a valid and inserts it into the {@code highscores} list in the correct position
     * based on a binary search. The list is then written back into the file.
     *
     * @param score the score the player got in the current round.
     * @param name the name the player entered after losing.
     * @throws IllegalArgumentException if {@code score} is negative or if {@code name} is {@code null} or blank
     */
    public void saveHighScore(int score, String name) {
        if (score < 0 || name == null || name.isBlank()) {
            LOGGER.warning("Cannot save invalid score entry (name: " + name + ", score: " + score + ").");
            throw new IllegalArgumentException("Invalid score entry entered. Score must be non-negative and name " +
                    "cannot be null or blank.");
        }
        HighScore newScore = new HighScore(score, name, LocalDate.now());

        int l = 0;
        int r = highscores.size() - 1;
        int m;

        while (l <= r) {
            m = (l + r) / 2;
            HighScore curScore = highscores.get(m);

            if (curScore.score() < score) {
                r = m - 1;
            } else if (curScore.score() > score) {
                l = m + 1;
            } else {
                l = m + 1;
                break;
            }
        }

        highscores.add(l, newScore);

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(path))) {
            for (HighScore hs : highscores) {
                bw.write(String.format("%d,%s,%s\n", hs.score(), hs.name(), hs.date()));
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save highscore to file at " + path + ".", e);
        }
        LOGGER.info("Saved Highscore (name: " + name + " score: " + score + " date: " + newScore.date + ").");
    }

    /**
     * Returns a list of all saved highscores.
     * @return a new list of all saved highscores
     */
    public List<HighScore> hslist() {
        return new ArrayList<>(highscores);
    }

    /**
     * A record describing a high score entry.
     * @param score the score the player got in the current round
     * @param name the name the player entered after losing
     * @param date the current date
     */
    public record HighScore(int score, String name, LocalDate date){}

}
