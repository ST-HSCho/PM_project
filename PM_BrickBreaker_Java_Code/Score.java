package brickbreaker;

import java.awt.Point;
import java.awt.Color;
import java.awt.Graphics2D;

public class Score {
	private Point position;
    private int score;
    private Color color = Color.WHITE; // 점수 글씨 색상
    
    // 생성자
    public Score(Point position) {
    	this.position = position;
        this.score = 0;
    }
    
    // 점수 증가 메서드
    public void addScore(int points) {
        this.score += points;
    }
    
    // 점수 초기화
    public void reset() {
        this.score = 0;
    }
    
    // 점수를 화면에 그리는 메서드
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.drawString("Score: " + score, position.x, position.y);
    }
    
    // 점수 반환
    public int getScore() {
        return score;
    }
}
