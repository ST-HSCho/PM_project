package brickbreaker;
import java.awt.Color;
import java.awt.Point;

public class Brick {
    private Point position; // 좌표
    private boolean isSpecial; // 특수 여부
    private Color color; 	// 색상
    private int width; 		// 너비
    private int height; 	// 높이
    
    // 기본 색상 정의 (일반 블록, 특수 블록)
    private static final Color DEFAULT_COLOR = Color.BLUE;
    private static final Color SPECIAL_COLOR = Color.YELLOW;
    
	public Brick(Point position, boolean isSpecial, int width, int height) {
		this.position = position;
        this.isSpecial = isSpecial;
        this.width = width;
        this.height = height;
        this.color = isSpecial ? SPECIAL_COLOR : DEFAULT_COLOR;
	}
	
	// 위치 반환
    public Point getPosition() {
        return position;
    }

    // 색상 반환
    public Color getColor() {
        return color;
    }

    // 크기 반환
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // 특수 블록 여부 반환
    public boolean isSpecial() {
        return isSpecial;
    }

    // 블록을 그리는 메소드
    public void draw(java.awt.Graphics2D g) {
        g.setColor(color);
        g.fillRoundRect(position.x, position.y, width, height, 10, 10);
    }

    // 충돌 검사 메소드
    public boolean checkBallCollision(Point ballPoint) {
        return (ballPoint.x >= position.x && ballPoint.x <= position.x + width &&
                ballPoint.y >= position.y && ballPoint.y <= position.y + height);
    }
}
