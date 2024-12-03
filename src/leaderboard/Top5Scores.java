package leaderboard;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Top5Scores {
    private static Map<String, List<ScoreEntry>> gameScores = new HashMap<>();

    public static void addScore(String game, String playerName, int score) {
        gameScores.putIfAbsent(game, new ArrayList<>());

        List<ScoreEntry> scores = gameScores.get(game);
        scores.add(new ScoreEntry(playerName, score));

        scores.sort(Comparator.comparingInt(ScoreEntry::getScore).reversed());
        if (scores.size() > 5) {
            scores = scores.subList(0, 5);
        }

        gameScores.put(game, scores);
    }

    public static List<ScoreEntry> getTopScores(String game) {
        return new ArrayList<>(gameScores.getOrDefault(game, new ArrayList<>())); 
    }
}
