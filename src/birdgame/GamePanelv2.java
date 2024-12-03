package birdgame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;
import leaderboard.ScoreFlappyBird;
import pausegame.PauseGameHandler;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.Image;

public class GamePanelv2 extends JPanel implements ActionListener {
    Timer timer;
    Bird bird = new Bird(100, 250);
    static int PANEL_HEIGHT = 600;
    static int PANEL_WIDTH = 800;
    ArrayList<Pipev2> pipes = new ArrayList<>();
    Random rand = new Random();
    boolean gameOver = false;
    int score_temp = 0, score = 0;
    JFrame frame;
    boolean running = false;
    boolean paused = false;
    PauseGameHandler pauseHandler = new PauseGameHandler();
    private Clip flapSound;
    private Image backgroundImage;
    private Clip crashSound;

    public GamePanelv2(JFrame frame) {
        this.frame = frame;
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        timer = new Timer(20, this);
        timer.start();
        initFlapSound();
        initCrashSound();
        loadBackgroundImage();

        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_SPACE && !gameOver && !paused) {
                    bird.jump();
                    playFlapSound();
                } else if (key == KeyEvent.VK_ESCAPE) {
                    togglePause();
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
        addPipe();
    }

    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("src/Image/IMG_FB.jpg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addPipe() {
        int gap = 100 + rand.nextInt(100);
        int width = 50;
        int height = rand.nextInt(300) + 100;
        pipes.add(new Pipev2(PANEL_WIDTH, 0, width, height, true));
        pipes.add(new Pipev2(PANEL_WIDTH, height + gap, width, PANEL_HEIGHT - height - gap));
    }

    private void togglePause() {
        PauseGameHandler.handleEscape(this, () -> {
            if (paused) {
                timer.start();
                running = true;
                new Thread(this::gameLoop).start();
            } else {
                timer.stop();
                running = false;
            }
            paused = !paused;
            repaint();
        });
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver || paused) {
            return;
        }
        bird.y += bird.velocity;
        bird.velocity += bird.GRAVITY;

        if (bird.y < 0) {
            bird.y = 0;
            bird.velocity = 0;
        } else if (bird.y + bird.Bird_Height > PANEL_HEIGHT) {
            bird.y = PANEL_HEIGHT - bird.Bird_Height;
            bird.velocity = 0;
        }

        Iterator<Pipev2> iter = pipes.iterator();
        while (iter.hasNext()) {
            Pipev2 pipe = iter.next();
            pipe.update();
            if (pipe.getX() + pipe.getBounds().width < 0) {
                iter.remove();
            }
        }

        if (pipes.isEmpty() || pipes.get(pipes.size() - 1).getX() < (PANEL_WIDTH / 2)) {
            addPipe();
        }

        for (Pipev2 pipe : pipes) {
            if (pipe.getBounds().intersects(bird.getBounds())) {
                gameOver = true;
                timer.stop();
                playCrashSound();
                showScorePanel();
            }
            if (!pipe.flipped && pipe.getX() + pipe.getBounds().width < bird.x) {
                score_temp = score_temp + 1;
            }
        }
        score = score_temp / 2;
        repaint();
    }

    private void showScorePanel() {
        SwingUtilities.invokeLater(() -> {
            frame.remove(this);
            frame.add(new ScoreFlappyBird(score));
            frame.revalidate();
            frame.repaint();
        });
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }
        bird.draw(g);

        for (Pipev2 pipe : pipes) {
            pipe.draw(g);
        }

        g.setColor(Color.BLACK);
        g.drawString("Score: " + score, 10, 20);

        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over", 350, 300);
        }
    }

    public void gameLoop() {
        while (running) {
            try {
                Thread.sleep(20);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void initFlapSound() {
        try {
            File soundFile = new File("src/music/E_birdflapping.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            flapSound = AudioSystem.getClip();
            flapSound.open(audioStream);
            
            FloatControl gainControl = (FloatControl) flapSound.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-20.0f);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void initCrashSound() {
        try {
            File soundFile = new File("src/music/E_FB_Crash.wav");
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            crashSound = AudioSystem.getClip();
            crashSound.open(audioStream);
            
            FloatControl gainControl = (FloatControl) crashSound.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-20.0f);
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException e) {
            e.printStackTrace();
        }
    }

    private void playFlapSound() {
        if (flapSound != null) {
            if (flapSound.isRunning()) {
                flapSound.stop();
            }
            flapSound.setFramePosition(0);
            flapSound.start();
        }
    }

    private void playCrashSound() {
        if (crashSound != null) {
            if (crashSound.isRunning()) {
                crashSound.stop();
            }
            crashSound.setFramePosition(0);
            crashSound.start();
        }
    }
}

