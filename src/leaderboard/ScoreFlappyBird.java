package leaderboard;

import javax.swing.*;
import java.awt.*;
import button.Retry;
import button.Menu;
import javax.imageio.ImageIO; // Import for image loading
import java.awt.Image; // Import for Image class
import java.io.File; // Import for File class
import java.io.IOException; // Import for IOException

public class ScoreFlappyBird extends JPanel {
    private int score;
    private JTextField nameField;
    private JTextArea leaderboardArea;
    private JPanel inputPanel;
    private Image backgroundImage; // Added for background image

    public ScoreFlappyBird(int score) {
        this.score = score;
        initializeUI();
        loadBackgroundImage(); // Load the background image
        UpdateLeaderboard.updateLeaderboardDisplay(leaderboardArea, "FlappyBird"); // Initialize leaderboard display
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
        inputPanel.setMaximumSize(new Dimension(200, 50)); 

        JLabel nameLabel = new JLabel("Enter your name: ");
        nameField = new JTextField(20);
        nameField.setMaximumSize(new Dimension(200, 30));
        nameField.addActionListener(e -> {
            String playerName = nameField.getText().trim();
            UpdateLeaderboard.handleNameSubmission("FlappyBird", playerName, score, this, nameField, inputPanel, leaderboardArea);
        });

        inputPanel.add(nameLabel);
        inputPanel.add(Box.createHorizontalStrut(10)); 
        inputPanel.add(nameField);

        leaderboardArea = new JTextArea(6, 30);
        leaderboardArea.setEditable(false);
        leaderboardArea.setFont(new Font("Arial", Font.PLAIN, 18));
        leaderboardArea.setMaximumSize(new Dimension(350, 180));
        JScrollPane scrollPane = new JScrollPane(leaderboardArea);
        scrollPane.setAlignmentX(Component.CENTER_ALIGNMENT);
        scrollPane.setMaximumSize(new Dimension(350, 180));
        scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER);
        scrollPane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER);

        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.X_AXIS));
        buttonPanel.setAlignmentX(Component.CENTER_ALIGNMENT);
        buttonPanel.setOpaque(false);  // Make button panel transparent

        JButton retryButton = new JButton("Retry");
        retryButton.addActionListener(e -> Retry.retryGame(this, "FlappyBird"));

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

    // Load the background image
    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("src/Image/IMG_Board.jpg")); // Load the image
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        // Draw the background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
    }
}