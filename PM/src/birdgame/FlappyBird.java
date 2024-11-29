package birdgame;

import javax.swing.JFrame;

public class FlappyBird {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        GamePanel gamePanel = new GamePanel();

        frame.add(gamePanel);
        frame.setTitle("Flappy Bird");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}