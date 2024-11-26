package brickbreaker;

import javax.swing.JPanel;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Point;
import java.util.ArrayList;
import java.util.List;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;
    private Renderer renderer;
    private Timer timer;

    // 게임 패널의 크기
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 800;

    public GamePanel() {
    	// 패널 크기 설정
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK); // 배경색 설정 (확실히 검정색으로 보이도록 설정)
        setLayout(null); // GamePanel에서도 null 레이아웃을 사용할 수 있도록 설정
        
        // 게임 객체 초기화
        paddle = new Paddle(new Point(350, 700), 100, 10);
        balls = new ArrayList<>();
        balls.add(new Ball(new Point(400, 600), 10, 0, 6));
        bricks = new ArrayList<>();
        
        // 벽돌 초기화 (5x10 배열로 벽돌 생성, 벽돌 간의 간격 최소화)
        int brickWidth = 80;  // 벽돌의 폭 (패널 크기를 벽돌 개수로 나누어 빈틈이 없도록)
        int brickHeight = 40; // 벽돌의 높이
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                bricks.add(new Brick(new Point(j * brickWidth, i * brickHeight), false, brickWidth, brickHeight));
            }
        }

        renderer = new Renderer();
        
        // 타이머 설정 (게임 루프를 60 FPS로 설정)
        timer = new Timer(10, this); // 1000ms / 10
        timer.start();
        
        // 키 리스너 추가
        setFocusable(true);
        addKeyListener(this);
    }

    // paintComponent에서 게임 그리기
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Renderer 사용하여 객체 그리기
        renderer.drawBricks(g2d, bricks);
        renderer.drawBalls(g2d, balls);
        renderer.drawPaddle(g2d, paddle);
    }

    // 게임 상태 업데이트 (매 틱마다 호출됨)
    public void updateGame() {
        for (Ball ball : balls) {
            ball.move();

            // 벽과의 충돌 확인
            if (ball.checkWallCollision(PANEL_WIDTH, PANEL_HEIGHT)) {
                balls.remove(ball);
                if (balls.isEmpty()) {
                    // 게임 오버 처리 (공이 모두 사라진 경우)
                    System.out.println("Game Over!");
                    timer.stop();
                }
                break;
            }

            // 패들과의 충돌 확인
            ball.checkPaddleCollision(paddle);

            // 벽돌과의 충돌 확인
            for (Brick brick : bricks) {
                if (ball.checkBrickCollision(brick)) {
                    bricks.remove(brick);
                    break;
                }
            }
        }
        
        // 패들 이동 업데이트
        paddle.move(PANEL_WIDTH);
    }

    // ActionListener 구현: 타이머 이벤트 처리 (게임 루프)
    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint(); // 화면 갱신
    }

    // KeyListener 구현: 키 눌렀을 때
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            paddle.setDx(-15); // 왼쪽으로 이동
        } else if (key == KeyEvent.VK_RIGHT) {
            paddle.setDx(15); // 오른쪽으로 이동 
        }
    }

    // 키를 떼었을 때
    @Override
    public void keyReleased(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT || key == KeyEvent.VK_RIGHT) {
            paddle.setDx(0); // 이동 멈춤
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {
        // 사용하지 않음
    }
}
