package game;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

public class HighScoreManager {
    private final List<HighScore> highscores;
    private final String filename;

    public HighScoreManager(String filename) {
        this.filename = filename;
        highscores = new ArrayList<>();
        loadHighScores();
    }

    private void loadHighScores() {
        String line;
        try (BufferedReader br = new BufferedReader(new FileReader(filename))) {
            while ((line = br.readLine()) != null) {
                String[] tokens = line.split(",");
                int score = Integer.parseInt(tokens[0]);
                String name = tokens[1];
                LocalDate date = LocalDate.parse(tokens[2]);
                highscores.add(new HighScore(score, name, date));
            }
        } catch (IOException | DateTimeParseException e) {
            e.printStackTrace();
        }
    }

    public void saveHighScore(int score, String name) {
        if (score < 0 || name == null || name.isBlank()) {
            System.err.println("Invalid high score entry");
            return;
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

        try (BufferedWriter bw = new BufferedWriter(new FileWriter(filename))) {
            for (HighScore hs : highscores) {
                bw.write(String.format("%d,%s,%s\n", hs.score(), hs.name(), hs.date()));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<HighScore> hslist() {
        return new ArrayList<>(highscores);
    }

    public record HighScore(int score, String name, LocalDate date){}

}
