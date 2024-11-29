package pingpong;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

public class PingPong extends JPanel implements KeyListener, Runnable {
    private float ballX = 250, ballY = 150;       // 공의 위치
    private float ball_x_mov = 2, ball_y_mov = 2; // 공 이동 방향 (속도)
    private int paddle1y = 100, paddle2y = 100; // 패들 위치
    private int player1Score = 0, player2Score = 0; // 점수
    private final int BALL_SIZE = 15;
    private final int PADDLE_WIDTH = 10, PADDLE_HEIGHT = 60;
    private final int WINDOW_WIDTH = 500, WINDOW_HEIGHT = 300;
    private final Random random = new Random();
    private final float MAX_SPEED = 5;
    private final float MIN_SPEED = 2;

    public PingPong() {
        this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setBackground(new Color(0, 76, 177)); // 하늘색 배경
        this.setFocusable(true);
        this.addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 테이블 경계선 (중앙)
        g.setColor(Color.WHITE);
        g.drawLine(WINDOW_WIDTH / 2, 0, WINDOW_WIDTH / 2, WINDOW_HEIGHT);

        // 점수 표시
        g.setFont(new Font("Arial", Font.BOLD, 24));
        g.drawString(String.valueOf(player1Score), WINDOW_WIDTH / 4, 30); // 플레이어 1 점수
        g.drawString(String.valueOf(player2Score), 3 * WINDOW_WIDTH / 4, 30); // 플레이어 2 점수

        // 공
        g.setColor(new Color(255, 120, 0)); // 찐한 주황
        g.fillOval((int) ballX, (int) ballY, BALL_SIZE, BALL_SIZE);

        // 패들
        g.setColor(new Color(189, 0, 0));
        g.fillRect(20, paddle1y, PADDLE_WIDTH, PADDLE_HEIGHT); // 플레이어 1
        g.fillRect(WINDOW_WIDTH - 30, paddle2y, PADDLE_WIDTH, PADDLE_HEIGHT); // 플레이어 2
    }

    private void update() {
        // 공 이동
        ballX += ball_x_mov;
        ballY += ball_y_mov;

        // 공의 벽 충돌 처리
        if (ballY <= 0 || ballY >= WINDOW_HEIGHT - BALL_SIZE) {
            ball_y_mov = -ball_y_mov;
        }

        // 공과 패들 충돌 처리, 속도가 반대면 뒷면에 닿은 거니까 무시하도록.
        if (ballX <= 30 && ball_x_mov < 0) {
            if (ballY + BALL_SIZE >= paddle1y && ballY <= paddle1y + PADDLE_HEIGHT) {
                if (Math.abs(ballX - 30) <= 2) {
                    ball_x_mov = -ball_x_mov;
                    ball_y_mov += getRandomOffset(); // Y 방향에 랜덤성 추가
                }
            }
        }
        // 플레이어 2 패들 앞면 충돌
        if (ballX >= WINDOW_WIDTH - 45 && ball_x_mov > 0) {
            if (ballY + BALL_SIZE >= paddle2y && ballY <= paddle2y + PADDLE_HEIGHT) {
                if (Math.abs(ballX - (WINDOW_WIDTH - 45)) <= 2) {
                    ball_x_mov = -ball_x_mov;
                    ball_y_mov += getRandomOffset(); // Y 방향에 랜덤성 추가
                }
            }
        }

        // 공이 화면을 벗어나면 점수 추가 및 공 리셋
        if (ballX < 0) { // 플레이어 2가 점수 획득
            player2Score++;
            resetBall();
        } else if (ballX > WINDOW_WIDTH) { // 플레이어 1이 점수 획득
            player1Score++;
            resetBall();
        }
    }

    private float getRandomOffset() {
        // -2에서 +2 사이의 랜덤 값 반환
        float offset = random.nextFloat(5) - 2;
        // Y 방향 속도 제한 (최소, 최대)
        if (Math.abs(ball_y_mov + offset) > MAX_SPEED) {
            offset = 0; // 최대 속도 초과 시 변경 없음
        }
        if (Math.abs(ball_y_mov + offset) < MIN_SPEED) {
            offset = MIN_SPEED * (ball_y_mov < 0 ? -1 : 1) - ball_y_mov; // 최소 속도 보장
        }
        return offset;
    }

    private void resetBall() {
        ballX = WINDOW_WIDTH / 2;
        ballY = WINDOW_HEIGHT / 2;
        ball_x_mov = random.nextBoolean() ? 2 : -2; // X 방향 초기 속도
        ball_y_mov = random.nextBoolean() ? 2 : -2; // Y 방향 초기 속도
    }

    @Override
    public void run() {
        while (true) {
            update();
            repaint();
            try {
                Thread.sleep(16); // 게임 속도 (60hz가 대략 16ms)
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        // 1플레이어 (W/S 키)
        if (key == KeyEvent.VK_W && paddle1y > 0) {
            paddle1y -= 10;
        } else if (key == KeyEvent.VK_S && paddle1y < WINDOW_HEIGHT - PADDLE_HEIGHT) {
            paddle1y += 10;
        }
        // 2플레이어 (방향키 업/다운)
        if (key == KeyEvent.VK_UP && paddle2y > 0) {
            paddle2y -= 10;
        } else if (key == KeyEvent.VK_DOWN && paddle2y < WINDOW_HEIGHT - PADDLE_HEIGHT) {
            paddle2y += 10;
        }
    }

    @Override
    public void keyReleased(KeyEvent e) {}

    @Override
    public void keyTyped(KeyEvent e) {}

    public static void main(String[] args) {
        JFrame frame = new JFrame("Ping Pong");
        PingPong game = new PingPong();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setResizable(false);
        frame.add(game);
        frame.pack();
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);

        Thread gamethread = new Thread(game);
        gamethread.start();
    }
}
