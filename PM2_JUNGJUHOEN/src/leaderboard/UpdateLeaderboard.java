package leaderboard;

import javax.swing.*;
import java.util.List;
public class UpdateLeaderboard {

    public static void updateLeaderboard(String game, String playerName, int score, JPanel panel, JTextField nameField, JPanel inputPanel) {
        if (playerName.isEmpty()) {
            JOptionPane.showMessageDialog(panel, "Please enter your name.", "Error", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Top5Scores.addScore(game, playerName, score);

        panel.remove(inputPanel);
        panel.revalidate();
        panel.repaint();
    }

    public static void updateLeaderboardDisplay(JTextArea leaderboardArea, String game) {
        StringBuilder leaderboard = new StringBuilder(game + " Top 5 Scores:\n");
        List<ScoreEntry> topScores = Top5Scores.getTopScores(game);
        if (topScores.isEmpty()) {
            leaderboard.append("No scores yet.\n");
        } else {
            for (ScoreEntry entry : topScores) {
                leaderboard.append(entry.getName()).append(": ").append(entry.getScore()).append("\n");
            }
        }
        leaderboardArea.setText(leaderboard.toString());
    }

    public static void handleNameSubmission(String game, String playerName, int score, JPanel panel, JTextField nameField, JPanel inputPanel, JTextArea leaderboardArea) {
        updateLeaderboard(game, playerName, score, panel, nameField, inputPanel);
        updateLeaderboardDisplay(leaderboardArea, game);
    }
}