import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

public class PingPong extends JPanel implements KeyListener, Runnable {
    private int ballX = 250, ballY = 150;       // 공의 위치
    private int ball_x_mov = 2, ball_y_mov = 2; // 공 이동 방향
    private int paddle1y = 100, paddle2y = 100; // 패들 위치
    private final int  BALL_SIZE = 15;
    private final int PADDLE_WIDTH = 10, PADDLE_HEIGHT = 60;
    private final int WINDOW_WIDTH = 500, WINDOW_HEIGHT = 300;

    public PingPong() {
        this.setPreferredSize(new Dimension(WINDOW_WIDTH, WINDOW_HEIGHT));
        this.setBackground(new Color(0, 76, 177)); //이게 하늘색 rgb값
        this.setFocusable(true);
        this.addKeyListener(this);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);

        // 테이블 경계선 (중앙)
        g.setColor(Color.WHITE);
        g.drawLine(WINDOW_WIDTH / 2, 0, WINDOW_WIDTH / 2, WINDOW_HEIGHT);

        // 공
        g.setColor(new Color(255, 120, 0)); //찐한 주황
        g.fillOval(ballX, ballY, BALL_SIZE, BALL_SIZE);

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

        // 공과 패들 충돌 처리
        if (ballX <= 30 && ballY + BALL_SIZE >= paddle1y && ballY <= paddle1y + PADDLE_HEIGHT) {
            ball_x_mov = -ball_x_mov;
        }
        if (ballX >= WINDOW_WIDTH - 45 && ballY + BALL_SIZE >= paddle2y && ballY <= paddle2y + PADDLE_HEIGHT) {
            ball_x_mov = -ball_x_mov;
        }

        // 공이 화면을 벗어나면 리셋
        if (ballX < 0 || ballX > WINDOW_WIDTH) {
            ballX = WINDOW_WIDTH / 2;
            ballY = WINDOW_HEIGHT / 2;
            ball_x_mov = -ball_x_mov; // 반대 방향으로 리셋
        }
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
        // 2플레이어 (방향키 업다운)
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
