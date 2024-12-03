package brickbreaker;


import javax.swing.*;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import leaderboard.ScoreBrickBreaker;

public class BrickBreaker extends JFrame {
    private GamePanel BB;
    private Clip backgroundMusic;
	
    public BrickBreaker() {
        setTitle("Brick Breaker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

        playBackgroundMusic();  // Start music when game launches

        BB = new GamePanel();
        add(BB);

        pack();
        setVisible(true);

        showScoreWhenGameEnds();
    }

    private void playBackgroundMusic() {
        try {
            File musicFile = new File("src/music/B_BB.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioStream);
            
            // Add volume control to match B_main.wav
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-20.0f);  // Set volume to match main menu music
            
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
            backgroundMusic.start();
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void showScoreWhenGameEnds() {
        new Thread(() -> {
            int finalScore = getFinalScore();
            SwingUtilities.invokeLater(() -> {
                stopBackgroundMusic();
                
                remove(BB);
                add(new ScoreBrickBreaker(finalScore));
                revalidate();
                repaint();
                pack();
            });
        }).start();
    }


    public int getFinalScore() {
        while (BB.isGameRunning()) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return BB.returnScore();
    }

    public void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }
}

