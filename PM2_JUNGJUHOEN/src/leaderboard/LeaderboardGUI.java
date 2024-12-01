package leaderboard;

import javax.swing.*;
import java.awt.*;
import button.Menu;
import java.util.List;

public class LeaderboardGUI extends JPanel {

    public LeaderboardGUI(JFrame frame, String game1, String game2) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(800, 800));

        JLabel gameTitle = new JLabel("Leaderboard");
        gameTitle.setFont(new Font("Arial", Font.BOLD, 36));
        gameTitle.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.X_AXIS));
        scorePanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JTextArea game1Area = new JTextArea(10, 15);
        game1Area.setEditable(false);
        game1Area.setFont(new Font("Arial", Font.PLAIN, 18));
        game1Area.setMaximumSize(new Dimension(200, 200));
        JScrollPane game1ScrollPane = new JScrollPane(game1Area);

        JTextArea game2Area = new JTextArea(10, 15);
        game2Area.setEditable(false);
        game2Area.setFont(new Font("Arial", Font.PLAIN, 18));
        game2Area.setMaximumSize(new Dimension(200, 200));
        JScrollPane game2ScrollPane = new JScrollPane(game2Area);

        scorePanel.add(game1ScrollPane);
        scorePanel.add(Box.createHorizontalStrut(20));
        scorePanel.add(game2ScrollPane);

        updateLeaderboardDisplay(game1Area, game1);
        updateLeaderboardDisplay(game2Area, game2);

        JButton menuButton = new JButton("Menu");
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuButton.addActionListener(e -> Menu.goToMenu(this));

        add(Box.createVerticalGlue());
        add(gameTitle);
        add(Box.createVerticalStrut(20));
        add(scorePanel);
        add(Box.createVerticalStrut(20));
        add(menuButton);
        add(Box.createVerticalGlue());
    }

    private void updateLeaderboardDisplay(JTextArea leaderboardArea, String game) {
        StringBuilder leaderboard = new StringBuilder(game + " Scores:\n");
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
}