package birdgame;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;

public class GamePanelv2 extends JPanel implements ActionListener {
    Timer timer;
    Bird bird = new Bird(100, 250);
    static int PANEL_HEIGHT = 600;
    static int PANEL_WIDTH = 800;
    ArrayList<Pipev2> pipes = new ArrayList<>();
    Random rand = new Random();
    boolean gameOver = false;
    int score = 0, realscore = 0; // 점수 변수 추가


    public GamePanelv2() {
        setPreferredSize(new Dimension(PANEL_WIDTH, PANEL_HEIGHT));
        timer = new Timer(20, this);
        timer.start();
        addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_SPACE && !gameOver) {
                    bird.jump();
                }
            }
        });
        setFocusable(true);
        requestFocusInWindow();
        addPipe();
    }

    private void addPipe() {
        int gap = 100 + rand.nextInt(100); // 파이프 사이의 통로 크기 랜덤
        int width = 50;
        int height = rand.nextInt(300) + 100;
        pipes.add(new Pipev2(PANEL_WIDTH, 0, width, height, true)); // 상단 파이프 (뒤집힘)
        pipes.add(new Pipev2(PANEL_WIDTH, height + gap, width, PANEL_HEIGHT - height - gap)); // 하단 파이프 (기본)
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return;
        }
        bird.y += bird.velocity;
        bird.velocity += bird.GRAVITY;

        // 천장과 바닥을 넘어가지 않도록 구속 조건
        if (bird.y < 0) {
            bird.y = 0;
            bird.velocity = 0;
        } else if (bird.y + bird.Bird_Height > PANEL_HEIGHT) {
            bird.y = PANEL_HEIGHT - bird.Bird_Height;
            bird.velocity = 0;
        }

        // 파이프 이동 및 제거
        Iterator<Pipev2> iter = pipes.iterator();
        while (iter.hasNext()) {
            Pipev2 pipe = iter.next();
            pipe.update();
            if (pipe.getX() + pipe.getBounds().width < 0) {
                iter.remove();
            }
        }

        // 새로운 파이프 추가
        if (pipes.isEmpty() || pipes.get(pipes.size() - 1).getX() < (PANEL_WIDTH / 2)) {
            addPipe();
        }

        // 점수 계산: 새가 지나간 파이프의 인덱스를 기반으로 점수 추가


        // 충돌 감지
        for (Pipev2 pipe : pipes) {
            if (pipe.getBounds().intersects(bird.getBounds())) {
                gameOver = true;
                timer.stop();
            }
            if (!pipe.flipped && pipe.getX() + pipe.getBounds().width < bird.x) {
                score = score + 1;
            }
        }
        realscore = score /2;
        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        bird.draw(g);

        for (Pipev2 pipe : pipes) {
            pipe.draw(g);
        }

        // 점수 표시
        g.setColor(Color.BLACK);
        g.drawString("Score: " + realscore, 10, 20);

        if (gameOver) {
            g.setColor(Color.RED);
            g.drawString("Game Over", 350, 300);
        }
    }
}
