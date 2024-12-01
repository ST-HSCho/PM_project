package brickbreaker;

import java.awt.Graphics2D;
import java.util.List;

public class Renderer {
    // 벽돌 그리기
    public void drawBricks(Graphics2D g, List<Brick> bricks) {
        for (Brick brick : bricks) {
            brick.draw(g); // Brick 클래스에 정의된 draw 메소드 사용
        }
    }

    // 공 그리기
    public void drawBalls(Graphics2D g, List<Ball> balls) {
        for (Ball ball : balls) {
            ball.draw(g); // Ball 클래스에 정의된 draw 메소드 사용
        }
    }

    // 패들 그리기
    public void drawPaddle(Graphics2D g, Paddle paddle) {
        paddle.draw(g); // Paddle 클래스에 정의된 draw 메소드 사용
    }
    
    // 점수 그리기
    public void drawScore(Graphics2D g, Score score) {
    	score.draw(g);
    }
}
