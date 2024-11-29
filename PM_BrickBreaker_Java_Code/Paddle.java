import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Paddle {
    private Point position; // 패들의 좌표 (왼쪽 상단 기준)
    private int width;      // 패들의 너비
    private int height;     // 패들의 높이
    private int dx;         // 이동 속도 (좌우 이동만)

    private static final Color DEFAULT_COLOR = Color.GREEN;

    public Paddle(Point position, int width, int height) {
        this.position = position;
        this.width = width;
        this.height = height;
        this.dx = 0; // 초기 속도는 0
    }

    // 패들 이동
    public void move(int panelWidth) {
    	position.x += dx;
    	
    	// 패들이 화면을 벗어나지 않도록 제한
    	if (position.x < 0) {
    		position.x = 0;
    	} else if (position.x + width > panelWidth) {
    		position.x = panelWidth - width;
    	}
    }

    // 패들 그리기
    public void draw(Graphics2D g) {
    	g.setColor(DEFAULT_COLOR);
    	g.fillRect(position.x, position.y, width, height);
    }

    // 위치 반환
    public Point getPosition() {
        return position;
    }

    // 크기 반환
    public int getWidth() {
        return width;
    }

    public int getHeight() {
        return height;
    }

    // 속도 설정
    public void setDx(int dx) {
        this.dx = dx;
    }
}
