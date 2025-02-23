package pingpong;

import javax.swing.*;
import pausegame.PauseGameHandler;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;
import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class PingPong extends JPanel implements KeyListener, Runnable {
    // Frame and game area sizes
    private static final int FRAME_WIDTH = 800;
    private static final int FRAME_HEIGHT = 800;
    private static final int WINDOW_WIDTH = 700;
    private static final int WINDOW_HEIGHT = 700;

    // Paddles, ball sizes, and speed
    private static final int PADDLE_WIDTH = 30;
    private static final int PADDLE_HEIGHT = 150;
    private static final int BALL_SIZE = 16;
    private static final int PADDLE_SPEED = 10;

    // Offsets for centering the game area
    private static final int OFFSET_X = (FRAME_WIDTH - WINDOW_WIDTH) / 2;
    private static final int OFFSET_Y = (FRAME_HEIGHT - WINDOW_HEIGHT) / 2;

    // Ball movement and speed limits
    private int ballX = WINDOW_WIDTH / 2, ballY = WINDOW_HEIGHT / 2;
    private int ball_x_mov = 2, ball_y_mov = 2;
    private final float MAX_SPEED = 9.0f;
    private final float MIN_SPEED = 2.5f;

    // Paddle positions
    private int paddle1y = (WINDOW_HEIGHT - PADDLE_HEIGHT) / 2;
    private int paddle2y = (WINDOW_HEIGHT - PADDLE_HEIGHT) / 2;

    // Game state variables
    private boolean running = true;
    private final boolean[] keyStates = new boolean[256];
    private int player1Score = 0, player2Score = 0;

    // Random generator and sound effects
    private final Random random = new Random();
    private Clip[] paddleHitSounds;
    private static final int NUM_SOUNDS = 3;

    public PingPong() {
        this.setPreferredSize(new Dimension(FRAME_WIDTH, FRAME_HEIGHT));
        this.setBackground(Color.BLACK); // Outer frame background
        this.setFocusable(true);
        this.addKeyListener(this);
        this.requestFocusInWindow();

        initSounds();
    }

    private void initSounds() {
        paddleHitSounds = new Clip[NUM_SOUNDS];
        try {
            File sound1 = new File("src/music/E_PP1.wav");
            File sound2 = new File("src/music/E_PP2.wav");
            File sound3 = new File("src/music/E_PP3.wav");

            paddleHitSounds[0] = loadClip(sound1);
            paddleHitSounds[1] = loadClip(sound2);
            paddleHitSounds[2] = loadClip(sound3);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private Clip loadClip(File soundFile) throws IOException, UnsupportedAudioFileException, LineUnavailableException {
        AudioInputStream audio = AudioSystem.getAudioInputStream(soundFile);
        Clip clip = AudioSystem.getClip();
        clip.open(audio);
        setVolume(clip, -20.0f); // Adjust volume
        return clip;
    }

    private void setVolume(Clip clip, float volume) {
        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
        gainControl.setValue(volume);
    }

    private void playRandomPaddleHit() {
        if (paddleHitSounds != null) {
            int randomIndex = random.nextInt(NUM_SOUNDS);
            Clip sound = paddleHitSounds[randomIndex];
            if (sound.isRunning()) sound.stop();
            sound.setFramePosition(0);
            sound.start();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        g.setColor(Color.BLACK);
        g.fillRect(0, 0, FRAME_WIDTH, FRAME_HEIGHT);

        g.setColor(new Color(0, 76, 177)); // Sky blue
        g.fillRect(OFFSET_X, OFFSET_Y, WINDOW_WIDTH, WINDOW_HEIGHT);

        g.setColor(Color.WHITE);
        g.drawRect(OFFSET_X, OFFSET_Y, WINDOW_WIDTH, WINDOW_HEIGHT);
        g.drawLine(OFFSET_X + WINDOW_WIDTH / 2, OFFSET_Y, OFFSET_X + WINDOW_WIDTH / 2, OFFSET_Y + WINDOW_HEIGHT);

        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString(String.valueOf(player1Score), OFFSET_X + WINDOW_WIDTH / 4, OFFSET_Y + 30);
        g.drawString(String.valueOf(player2Score), OFFSET_X + 3 * WINDOW_WIDTH / 4, OFFSET_Y + 30);

        g.setColor(new Color(255, 120, 0)); // Orange
        g.fillOval(OFFSET_X + ballX, OFFSET_Y + ballY, BALL_SIZE, BALL_SIZE);

        g.setColor(new Color(189, 0, 0)); // Red
        g.fillRect(OFFSET_X + 20, OFFSET_Y + paddle1y, PADDLE_WIDTH, PADDLE_HEIGHT); // 1P paddle
        g.fillRect(OFFSET_X + WINDOW_WIDTH - PADDLE_WIDTH - 20, OFFSET_Y + paddle2y, PADDLE_WIDTH, PADDLE_HEIGHT); // 2P paddle
    }

    private void Paddlehandler() {
        // 공과 패들의 충돌 영역 (Bounding Box)
        Rectangle ballBounds = new Rectangle(OFFSET_X + ballX, OFFSET_Y + ballY, BALL_SIZE, BALL_SIZE);
        Rectangle paddle1Bounds = new Rectangle(OFFSET_X + 20, OFFSET_Y + paddle1y, PADDLE_WIDTH, PADDLE_HEIGHT);
        Rectangle paddle2Bounds = new Rectangle(OFFSET_X + WINDOW_WIDTH - PADDLE_WIDTH - 20, OFFSET_Y + paddle2y, PADDLE_WIDTH, PADDLE_HEIGHT);

        // 패들 1 충돌 처리
        if (ballBounds.intersects(paddle1Bounds) && ball_x_mov < 0) {
            Physics(paddle1y, true);
        }

        // 패들 2 충돌 처리
        if (ballBounds.intersects(paddle2Bounds) && ball_x_mov > 0) {
            Physics(paddle2y, false);
        }
    }

    private void Physics(int paddleY, boolean isLeftPaddle) { //함부로 수정하면 큰일남
        // 패들에서 공이 충돌한 위치를 기준으로 반사 각도를 계산
        double hitPosition = (ballY + BALL_SIZE / 2.0) - (paddleY + PADDLE_HEIGHT / 2.0);
        double normalizedRelativeY = hitPosition / (PADDLE_HEIGHT / 2.0);
        double reflectionAngle = normalizedRelativeY * Math.PI / 4; // -45도에서 +45도

        // 속도 계산 (패들 이동 방향의 영향 포함)
        int paddleSpeedEffect = 0;
        if (isLeftPaddle) {
            paddleSpeedEffect = (keyStates[KeyEvent.VK_W] ? -1 : 0) + (keyStates[KeyEvent.VK_S] ? 1 : 0);
        } else {
            paddleSpeedEffect = (keyStates[KeyEvent.VK_UP] ? -1 : 0) + (keyStates[KeyEvent.VK_DOWN] ? 1 : 0);
        }

        // 반사된 공의 새로운 속도 및 방향 설정
        int newBallXMov = (int) (Math.cos(reflectionAngle) * MAX_SPEED);
        int newBallYMov = (int) (Math.sin(reflectionAngle) * MAX_SPEED) + paddleSpeedEffect * 2;

        // 속도 제한
        newBallYMov = Math.max(Math.min(newBallYMov, (int) MAX_SPEED), -(int) MAX_SPEED);

        // 공 회전 효과 추가 (가상으로 처리)
        if (Math.abs(normalizedRelativeY) > 0.5) {
            // 충돌 위치가 패들의 가장자리일 경우 더 큰 스핀 효과
            ball_y_mov += (normalizedRelativeY > 0 ? 1 : -1) * random.nextInt(2);
        }

        // X 방향 반전 및 최종 속도 적용
        ball_x_mov = isLeftPaddle ? Math.abs(newBallXMov) : -Math.abs(newBallXMov);
        ball_y_mov = newBallYMov;

        // 충돌 사운드 재생
        playRandomPaddleHit();
    }

    private void update() {
        if (!running) return;

        // 패들 이동 처리
        if (keyStates[KeyEvent.VK_W] && paddle1y > 0) paddle1y -= PADDLE_SPEED;
        if (keyStates[KeyEvent.VK_S] && paddle1y < WINDOW_HEIGHT - PADDLE_HEIGHT) paddle1y += PADDLE_SPEED;
        if (keyStates[KeyEvent.VK_UP] && paddle2y > 0) paddle2y -= PADDLE_SPEED;
        if (keyStates[KeyEvent.VK_DOWN] && paddle2y < WINDOW_HEIGHT - PADDLE_HEIGHT) paddle2y += PADDLE_SPEED;

        // 공 이동 처리
        ballX += ball_x_mov;
        ballY += ball_y_mov;

        // 화면 경계 충돌 처리
        if (ballY <= 0 || ballY + BALL_SIZE >= WINDOW_HEIGHT) {
            ball_y_mov = -ball_y_mov;
        }

        // 패들 충돌 처리
        Paddlehandler();

        // 득점 처리
        if (ballX < 0) { 
            player2Score++;
            resetBall();
        } else if (ballX > WINDOW_WIDTH) {
            player1Score++;
            resetBall();
        }
    }

    private float getRandomOffset() {
        float offset = random.nextFloat(5) - 2.5f;
        if (Math.abs(ball_y_mov + offset) > MAX_SPEED) return 0;
        if (Math.abs(ball_y_mov + offset) < MIN_SPEED) {
            offset = MIN_SPEED * (ball_y_mov < 0 ? -1 : 1) - ball_y_mov;
        }
        return offset;
    }

    private void resetBall() {
        ballX = WINDOW_WIDTH / 2;
        ballY = WINDOW_HEIGHT / 2;
        ball_x_mov = random.nextBoolean() ? 2 : -2;
        ball_y_mov = random.nextBoolean() ? 2 : -2;
    }

    @Override
    public void run() {
        while (true) {
            update();
            repaint();
            try {
                Thread.sleep(16); // ~60 FPS
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key >= 0 && key < keyStates.length) {
            keyStates[key] = true;
        }
        if (key == KeyEvent.VK_ESCAPE) {
            PauseGameHandler.handleEscape(this, () -> setRunning(!running));
        }
    }
    
    public void setRunning(boolean running) {       //내가추가
        this.running = running;
    }

    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key >= 0 && key < keyStates.length) {
            keyStates[key] = false;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {

        JFrame frame = new JFrame("Ping Pong");
        PingPong game = new PingPong();
        frame.add(game);
        frame.setSize(FRAME_WIDTH, FRAME_HEIGHT);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setVisible(true);

        Thread gameThread = new Thread(game);
        gameThread.start();

    }
}
