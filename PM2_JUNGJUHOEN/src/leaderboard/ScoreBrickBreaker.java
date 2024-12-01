package leaderboard;

import javax.swing.*;
import java.awt.*;
import button.Retry;
import button.Menu;

public class ScoreBrickBreaker extends JPanel {
    private int score;
    private JTextField nameField;
    private JTextArea leaderboardArea;
    private JPanel inputPanel;

    public ScoreBrickBreaker(int score) {
        this.score = score;
        initializeUI();
        UpdateLeaderboard.updateLeaderboardDisplay(leaderboardArea, "BrickBreaker"); // Initialize leaderboard display
    }

    private void initializeUI() {
        setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
        
        setPreferredSize(new Dimension(800, 800));

        JLabel scoreLabel = new JLabel("Your score: " + score);
        scoreLabel.setFont(new Font("Arial", Font.BOLD, 24));
        scoreLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        inputPanel = new JPanel();
        inputPanel.setLayout(new BoxLayout(inputPanel, BoxLayout.X_AXIS));
        inputPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        inputPanel.setMaximumSize(new Dimension(400, 50)); // Set maximum size for input panel

        JLabel nameLabel = new JLabel("Enter your name: ");
        nameField = new JTextField(20);
        nameField.setMaximumSize(new Dimension(200, 30));
        nameField.addActionListener(e -> {
            String playerName = nameField.getText().trim();
            UpdateLeaderboard.handleNameSubmission("BrickBreaker", playerName, score, this, nameField, inputPanel, leaderboardArea);
        });

        inputPanel.add(nameLabel);
        inputPanel.add(Box.createHorizontalStrut(10)); 
        inputPanel.add(nameField);

        leaderboardArea = new JTextArea(10, 30);
        leaderboardArea.setEditable(false);
        leaderboardArea.setFont(new Font("Arial", Font.PLAIN, 18));
        leaderboardArea.setMaximumSize(new Dimension(400, 200)); 
        JScrollPane scrollPane = new JScrollPane(leaderboardArea);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JButton retryButton = new JButton("Retry");
        retryButton.addActionListener(e -> Retry.retryGame(this, "BrickBreaker"));

        JButton menuButton = new JButton("Menu");
        menuButton.addActionListener(e -> Menu.goToMenu(this));

        buttonPanel.add(retryButton);
        buttonPanel.add(Box.createHorizontalStrut(10)); 
        buttonPanel.add(menuButton);

        add(Box.createVerticalGlue());
        add(scoreLabel);
        add(Box.createVerticalStrut(20));
        add(inputPanel);
        add(Box.createVerticalStrut(20));
        add(scrollPane);
        add(Box.createVerticalStrut(20));
        add(buttonPanel);
        add(Box.createVerticalGlue());
    }
}