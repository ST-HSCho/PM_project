package birdgame;

import javax.swing.JFrame;

import javax.swing.JFrame;

public class FlappyBird {
    public static void main(String[] args) {
        JFrame frame = new JFrame();
        GamePanelv2 gamePanelv2 = new GamePanelv2();

        frame.add(gamePanelv2);
        frame.setTitle("Flappy Bird");
        frame.setSize(800, 600);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.pack();
        frame.setVisible(true);
    }
}