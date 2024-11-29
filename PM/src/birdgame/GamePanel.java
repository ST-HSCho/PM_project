package birdgame;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.Timer;
import java.util.*;

public class GamePanel extends JPanel implements ActionListener {
    Timer timer;
    Bird bird = new Bird(100,250);
    static int PANEL_HEIGHT = 600;
    static int PANEL_WIDTH = 800;
    ArrayList<Pipe> pipes = new ArrayList<>();
    Random rand = new Random();
    boolean gameOver = false;

    public GamePanel() {
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
        setFocusable(true); //혹시라도 입력 안들어갈까봐
        requestFocusInWindow(); // 일단 적어둠.
        addPipe();
    }

    private void addPipe() {
        int gap = 200; // 파이프 사이에 적당한 통로가 있어야 하니까
        int width = 50;
        int height = rand.nextInt(300) + 100;
        pipes.add(new Pipe(PANEL_WIDTH, 0, width, height)); //화면 오른쪽 끝에서 생성
        pipes.add(new Pipe(PANEL_WIDTH, height + gap, width, PANEL_HEIGHT - height - gap));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (gameOver) {
            return;
        }
        bird.y += bird.velocity;
        bird.velocity += bird.GRAVITY;

        // 천장과 바닥을 넘어가지 않도록 구속 조건 추가
        if (bird.y < 0) {
            bird.y = 0;
            bird.velocity = 0;
        } else if (bird.y + bird.Bird_Height > PANEL_HEIGHT) {
            bird.y = PANEL_HEIGHT - bird.Bird_Height;
            bird.velocity = 0;
        }

        //bird.update(); //이거 왜 안됨???

        // 이터레이터가 화면 밖에 나간 파이프를 제거해줌
        Iterator<Pipe> iter = pipes.iterator();
        while (iter.hasNext()) {
            Pipe pipe = iter.next();
            pipe.update();
            if (pipe.getX() + pipe.getBounds().width < 0) {
                iter.remove();
            }
        }

        // 새로운 장애물 추가
        if (pipes.isEmpty() || pipes.get(pipes.size() - 1).getX() < (PANEL_WIDTH / 2)) {
            addPipe(); //파이프가 하나도 없거나 (init) 파이프가 중앙을 넘어가면 추가
        }

        // 충돌 감지
        for (Pipe pipe : pipes) {
            if (pipe.getBounds().intersects(bird.getBounds())) {
                gameOver = true;
                timer.stop();
            }
        }

        repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        bird.draw(g);

        for (Pipe pipe : pipes) {
            pipe.draw(g);
        }

        if (gameOver) {
            g.setColor(Color.BLACK);
            g.drawString("Game Over", 350, 300);
        }
    }
}