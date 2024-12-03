package leaderboard;

import javax.swing.*;
import java.awt.*;
import button.Menu;
import java.util.List;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class LeaderboardGUI extends JPanel {
    private Image backgroundImage;

    public LeaderboardGUI(JFrame frame, String game1, String game2) {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        setPreferredSize(new Dimension(800, 800));
        setOpaque(false);

        loadBackgroundImage();

        JLabel gameTitle = new JLabel("Leaderboard");
        gameTitle.setFont(new Font("Arial", Font.BOLD, 36));
        gameTitle.setAlignmentX(Component.CENTER_ALIGNMENT);
        gameTitle.setForeground(Color.WHITE);

        JPanel scorePanel = new JPanel();
        scorePanel.setLayout(new BoxLayout(scorePanel, BoxLayout.X_AXIS));
        scorePanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        scorePanel.setOpaque(false);

        JTextArea game1Area = new JTextArea(6, 30);
        game1Area.setEditable(false);
        game1Area.setFont(new Font("Arial", Font.PLAIN, 18));
        game1Area.setMaximumSize(new Dimension(350, 180));
        game1Area.setOpaque(false);
        JScrollPane game1ScrollPane = new JScrollPane(game1Area);
        game1ScrollPane.setMaximumSize(new Dimension(350, 180));
        game1ScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        game1ScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        game1ScrollPane.setOpaque(false);
        

        JTextArea game2Area = new JTextArea(6, 30);
        game2Area.setEditable(false);
        game2Area.setFont(new Font("Arial", Font.PLAIN, 18));
        game2Area.setMaximumSize(new Dimension(350, 180));
        game2Area.setOpaque(false);
        JScrollPane game2ScrollPane = new JScrollPane(game2Area);
        game2ScrollPane.setMaximumSize(new Dimension(350, 180));
        game2ScrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        game2ScrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);
        game2ScrollPane.setOpaque(false);


        scorePanel.add(game1ScrollPane);
        scorePanel.add(Box.createHorizontalStrut(100));
        scorePanel.add(game2ScrollPane);

        updateLeaderboardDisplay(game1Area, game1);
        updateLeaderboardDisplay(game2Area, game2);

        JButton menuButton = new JButton("Menu");
        menuButton.setAlignmentX(Component.CENTER_ALIGNMENT);
        menuButton.addActionListener(e -> Menu.goToMenu(this));

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setOpaque(false);
        buttonPanel.add(menuButton);

        add(Box.createVerticalGlue());
        add(gameTitle);
        add(Box.createVerticalStrut(20));
        add(scorePanel);
        add(Box.createVerticalStrut(20));
        add(buttonPanel);
        add(Box.createVerticalGlue());
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("src/Image/IMG_Board.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }

    private void updateLeaderboardDisplay(JTextArea leaderboardArea, String game) {
        StringBuilder leaderboard = new StringBuilder(game + " Scores\n");
        List<ScoreEntry> topScores = Top5Scores.getTopScores(game);

        for (ScoreEntry entry : topScores) {
            leaderboard.append(entry.getName()).append(": ").append(entry.getScore()).append("\n");
        }
        leaderboardArea.setText(leaderboard.toString());
    }
}