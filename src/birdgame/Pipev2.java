package birdgame;
import java.awt.*;

public class Pipev2 {
    int x, y, width, height;
    int speed = 5;
    boolean flipped;

    public Pipev2(int x, int y, int width, int height) {
        this(x, y, width, height, false); // 기본값: 뒤집히지 않음
    }

    public Pipev2(int x, int y, int width, int height, boolean flipped) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
        this.flipped = flipped;
    }

    public void update() {
        x -= speed;
    }

    public void draw(Graphics g) {
        // 그라데이션 효과
        Graphics2D g2d = (Graphics2D) g;
        //GradientPaint gradient = new GradientPaint(x, y, Color.GREEN.darker(), x, y + height, Color.GREEN.brighter());
        //g2d.setPaint(gradient);
        g2d.fillRect(x, y, width, height);

        // 상단 디테일 (원형 헤드)
        //g2d.setColor(Color.GREEN.darker());
        //g2d.fillRoundRect(x, y - 20, width, 20, 15, 15); // 원형 상단 디테일

        GradientPaint gradient;
        if (flipped) {
            // 상단 파이프: 그라데이션 반대로
            gradient = new GradientPaint(x, y + height, Color.GREEN.darker(), x, y, Color.GREEN.brighter());
        } else {
            // 하단 파이프: 일반적인 그라데이션
            gradient = new GradientPaint(x, y, Color.GREEN.darker(), x, y + height, Color.GREEN.brighter());
        }
        g2d.setPaint(gradient);
        g2d.fillRect(x, y, width, height);
        /*
        // 도형을 뒤집는 경우 처리
        if (flipped) {
            AffineTransform original = g2d.getTransform();
            g2d.translate(x, y + height); // 기준점을 설정
            g2d.scale(1, -1); // 상하 대칭
            g2d.fillRect(0, 0, width, height);
            g2d.setTransform(original); // 변환 원상복구
        } else {
            g2d.fillRect(x, y, width, height);
        }

         */
        // 내부 그림자 효과
        g2d.setColor(new Color(0, 0, 0, 50)); // 투명한 검정색
        g2d.fillRect(x + 5, y + 5, width - 10, height - 10);

        // 외곽선 추가
        g2d.setColor(Color.BLACK);
        g2d.drawRect(x, y, width, height);
        //g2d.drawRoundRect(x, y - 20, width, 20, 15, 15); // 상단 외곽선
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, width, height);
    }

    public int getX() {
        return x;
    }
}
