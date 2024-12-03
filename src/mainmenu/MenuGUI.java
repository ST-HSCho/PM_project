package mainmenu;

import javax.swing.*;
import brickbreaker.BrickBreaker;
import pingpong.PingPong;
import java.awt.Color;
import java.awt.Font;
import leaderboard.LeaderboardGUI;
import javax.sound.sampled.*;
import javax.swing.JSlider;
import java.awt.Graphics;
import java.awt.Image;
import java.io.File;
import javax.imageio.ImageIO;

public class MenuGUI extends JFrame {

    private Clip backgroundMusic;
    private float currentVolume = 50.0f; // Starting volume as percentage (50%)
    private Image backgroundImage;

    public MenuGUI() {
        setTitle("Game Start!"); // 창 제목 설정
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 닫기 시 종료
        setSize(800, 800); // 창 크기 설정
        setLayout(null); // 레이아웃 설정

        // 배경 이미지 로드 (동기 처리로 딜레이 방지)
        loadBackgroundImage();

        JPanel backgroundPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        backgroundPanel.setLayout(null);
        setContentPane(backgroundPanel);

        // 버튼 추가
        addButtons();

        // 배경음악 및 볼륨 슬라이더 설정
        initializeBackgroundMusic();
        addVolumeControl();

        setVisible(true); // 프레임 화면 표시
    }

    private void loadBackgroundImage() {
        try {
            // 이미지를 동기적으로 로드하여 딜레이 제거
            backgroundImage = ImageIO.read(new File("src/Image/IMG_Main.jpg"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addButtons() {
        JLabel welcomelabel = new JLabel("Welcome to the Game!");
        welcomelabel.setFont(new Font("Arial", Font.BOLD, 35));
        welcomelabel.setForeground(Color.BLACK);
        welcomelabel.setHorizontalAlignment(SwingConstants.CENTER);
        welcomelabel.setBounds(0, 50, 800, 50);
        add(welcomelabel);

        JButton birdgameButton = new JButton("Flappy Bird"); // 플래피버드
        birdgameButton.setBounds(100, 200, 150, 50);
        birdgameButton.setBackground(Color.YELLOW);
        birdgameButton.setForeground(Color.BLACK);
        birdgameButton.setFocusPainted(false);
        birdgameButton.addActionListener(e -> {
            stopBackgroundMusic();
            dispose();
            birdgame.FlappyBird.main(new String[] {});
        });
        add(birdgameButton);

        JButton brickgameButton = new JButton("Brick Breaker"); // 벽돌깨기
        brickgameButton.setBounds(300, 200, 150, 50);
        brickgameButton.setBackground(Color.YELLOW);
        brickgameButton.setForeground(Color.BLACK);
        brickgameButton.setFocusPainted(false);
        brickgameButton.addActionListener(e -> {
            stopBackgroundMusic();
            dispose();
            new BrickBreaker();
        });
        add(brickgameButton);

        JButton pingpongButton = new JButton("Pingpong"); // 핑퐁
        pingpongButton.setBounds(500, 200, 150, 50);
        pingpongButton.setBackground(Color.YELLOW);
        pingpongButton.setForeground(Color.BLACK);
        pingpongButton.setFocusPainted(false);
        pingpongButton.addActionListener(e -> {
            stopBackgroundMusic();
            PingPong game = new PingPong();
            setContentPane(game);
            revalidate();
            repaint();
            game.requestFocusInWindow();

            Thread gameThread = new Thread(game);
            gameThread.start();
        });
        add(pingpongButton);

        JButton leaderboardButton = new JButton("Leaderboard");
        leaderboardButton.setBounds(300, 350, 150, 50);
        leaderboardButton.setBackground(Color.GREEN);
        leaderboardButton.setForeground(Color.BLACK);
        leaderboardButton.setFocusPainted(false);
        leaderboardButton.addActionListener(e -> {
            setContentPane(new LeaderboardGUI(MenuGUI.this, "FlappyBird", "BrickBreaker"));
            revalidate();
            repaint();
        });
        add(leaderboardButton);

        JButton exitButton = new JButton("Exit"); // 종료 버튼
        exitButton.setBounds(300, 500, 150, 50);
        exitButton.setBackground(Color.CYAN);
        exitButton.setForeground(Color.BLACK);
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(e -> System.exit(0));
        add(exitButton);
    }

    private void initializeBackgroundMusic() {
        try {
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(
                getClass().getResource("/music/B_main.wav")); // 배경음악 파일 로드
            backgroundMusic = AudioSystem.getClip();
            backgroundMusic.open(audioInputStream);

            // 초기 볼륨 설정
            FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
            float range = gainControl.getMaximum() - gainControl.getMinimum();
            float gain = (range * (currentVolume / 100.0f)) + gainControl.getMinimum();
            gainControl.setValue(gain);

            // 반복 재생 설정
            backgroundMusic.loop(Clip.LOOP_CONTINUOUSLY);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addVolumeControl() {
        JSlider volumeSlider = new JSlider(JSlider.HORIZONTAL, 0, 100, (int) currentVolume);
        volumeSlider.setBounds(300, 600, 200, 50);
        volumeSlider.setMajorTickSpacing(20);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);

        volumeSlider.addChangeListener(e -> {
            currentVolume = volumeSlider.getValue();
            if (backgroundMusic != null) {
                FloatControl gainControl = (FloatControl) backgroundMusic.getControl(FloatControl.Type.MASTER_GAIN);
                float range = gainControl.getMaximum() - gainControl.getMinimum();
                float gain = (range * (currentVolume / 100.0f)) + gainControl.getMinimum();
                gainControl.setValue(gain);
            }
        });

        add(volumeSlider);

        JLabel volumeLabel = new JLabel("Volume");
        volumeLabel.setBounds(350, 570, 100, 30);
        volumeLabel.setHorizontalAlignment(SwingConstants.CENTER);
        add(volumeLabel);
    }

    private void stopBackgroundMusic() {
        if (backgroundMusic != null && backgroundMusic.isRunning()) {
            backgroundMusic.stop();
            backgroundMusic.close();
        }
    }

    @Override
    public void dispose() {
        stopBackgroundMusic();
        super.dispose();
    }

    public static void main(String[] args) {
        new MenuGUI();
    }
}