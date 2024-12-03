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
import java.util.Random;
import java.util.Iterator;
import pausegame.PauseGameHandler;
import javax.imageio.ImageIO;
import java.awt.Image;
import java.io.File;
import java.io.IOException;

public class GamePanel extends JPanel implements ActionListener, KeyListener {
    private Paddle paddle;
    private List<Ball> balls;
    private List<Brick> bricks;
    private Renderer renderer;
    private Timer timer;
    private Score score;
    private boolean gameRunning = true;
    private boolean paused = false;
    private Image backgroundImage;

    // 게임 패널의 크기
    private static final int PANEL_WIDTH = 800;
    private static final int PANEL_HEIGHT = 800;

    public GamePanel() {
    	// 패널 크기 설정
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        setBackground(Color.BLACK); // 배경색 설정 (확실히 검정색으로 보이도록 설정)
        setLayout(null); // GamePanel에서도 null 레이아웃을 사용할 수 있도록 설정
        
        // Load the background image
        loadBackgroundImage();

        // 게임 객체 초기화
        paddle = new Paddle(new Point(350, 700), 100, 13);
        balls = new ArrayList<>();
        balls.add(new Ball(new Point(400, 600), 12, 0, 10, 7));
        bricks = new ArrayList<>();
        score = new Score(new Point(0, 790));
        
        // 벽돌 초기화 (5x10 배열로 벽돌 생성, 벽돌 간의 간격 최소화)
        int brickWidth = 80;  // 벽돌의 폭 (패널 크기를 벽돌 개수로 나누어 빈틈이 없도록)
        int brickHeight = 40; // 벽돌의 높이
        for (int i = 0; i < 5; i++) {
            for (int j = 0; j < 10; j++) {
                bricks.add(new Brick(new Point(j * brickWidth, i * brickHeight), secialP(2), brickWidth, brickHeight));
            }
        }

        renderer = new Renderer();
        
        // 타이머 설정 (게임 루프를 1000/N FPS로 설정)
        timer = new Timer(10,this);
        timer.start();

        // 키 리스너 추가
        setFocusable(true);
        addKeyListener(this);
    }

    // Load the background image
    private void loadBackgroundImage() {
        try {
            backgroundImage = ImageIO.read(new File("src/Image/IMG_BB.jpg")); // Load the image
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // paintComponent에서 게임 그리기
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the background image
        if (backgroundImage != null) {
            g2d.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
        }

        // Renderer 사용하여 객체 그리기
        renderer.drawBricks(g2d, bricks);
        renderer.drawBalls(g2d, balls);
        renderer.drawPaddle(g2d, paddle);
        renderer.drawScore(g2d, score);
    }

    // 게임 상태 업데이트 (매 틱마다 호출됨)
    public void updateGame() {
        Iterator<Ball> iterator = balls.iterator();
        List<Ball> newBalls = new ArrayList<>(); // 새로 추가할 공을 저장할 리스트

        while (iterator.hasNext()) {
            Ball ball = iterator.next();
            ball.move();

            // 벽과의 충돌 확인
            if (ball.checkWallCollision(PANEL_WIDTH, PANEL_HEIGHT)) {
                iterator.remove();
                if (balls.isEmpty()) {
                    // 게임 오버 처리 (공이 모두 사라진 경우)
                    System.out.println("Game Over!");
                    timer.stop();
                    gameRunning = false; // 게임 종료 상태 설정
                }
                continue;
            }

            // 패들과의 충돌 확인
            ball.checkPaddleCollision(paddle);

            // 벽돌과의 충돌 확인
            Iterator<Brick> brickIterator = bricks.iterator();
            while (brickIterator.hasNext()) {
                Brick brick = brickIterator.next();
                
                if (ball.checkBrickCollision(brick)) {
                    brickIterator.remove();
                    score.addScore(10);
                    
                    if (brick.isSpecial()) {
                        // 새로 추가할 공을 newBalls 리스트에 저장
                        Point newBallPosition = new Point(ball.getPosition().x, ball.getPosition().y);
                        int newDx = ball.getDx() + ((ball.getDx() > 0) ? 1 : -1);
                        int newDy = ball.getDy() + ((ball.getDy() > 0) ? 1 : -1);
                        newBalls.add(new Ball(newBallPosition, 12, newDx, newDy, ball.getMINSPEED() + 2));
                    }
                    if(bricks.isEmpty()) {
                    	// 공 갯수 만큼 점수 추가
                        score.addScore(balls.size() * 100);
                        System.out.println("Game Over!");
                        timer.stop();
                        gameRunning = false; // 게임 종료 상태 설정
                    }
                    break;
                }
            }
        }

        // 반복이 끝난 후 새로운 공을 추가
        balls.addAll(newBalls);

        // 패들 이동 업데이트
        paddle.move(PANEL_WIDTH);
    }

    // ActionListener 구현: 타이머 이벤트 처리 (게임 루프)
    @Override
    public void actionPerformed(ActionEvent e) {
        if (!paused) {
            updateGame();
            repaint(); // 화면 갱신
        }
    }

    // KeyListener 구현: 키 눌렀을 때
    @Override
    public void keyPressed(KeyEvent e) {
        int key = e.getKeyCode();
        if (key == KeyEvent.VK_LEFT) {
            paddle.setDx(-9); // 왼쪽으로 이동
        } else if (key == KeyEvent.VK_RIGHT) {
            paddle.setDx(9); // 오른쪽으로 이동 
        } else if (key == KeyEvent.VK_ESCAPE) {
            togglePause();
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
    
    
    public boolean secialP(int P) {
        Random random = new Random();
        int randomNumber = random.nextInt(10) + 1; // 1부터 10까지의 숫자
        if (randomNumber <= P) {
        	return true;
        }
    	return false;
    }
    
    public int returnScore() {
        return score.getScore();
    }
    
    public boolean isGameRunning() {
        return gameRunning;
    }

    private void togglePause() {
        PauseGameHandler.handleEscape(this, () -> {
            if (paused) {           // 일시정지 상태라면
                timer.start();
            } else {               // 일시정지 상태가 아니라면
                timer.stop();
            }
            paused = !paused;
            repaint();
        });
    }
}