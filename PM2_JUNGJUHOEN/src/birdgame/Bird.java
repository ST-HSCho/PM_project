package birdgame;
import java.awt.*;
import java.awt.geom.Ellipse2D;

public class Bird {
    int x, y;
    int velocity = 0;

    final int GRAVITY = 1;
    final int JUMP = -10;
    final int Bird_Height = 20;
    final int Bird_Width = 30;

    public Bird(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public void jump() {
        velocity = JUMP;
    }

    public void update() {
        y += velocity;
        velocity += GRAVITY;

        // 천장과 바닥을 넘어가지 않도록 구속 조건 추가
        if (y < 0) {
            y = 0;
            velocity = 0;
        } else if (y + Bird_Height > GamePanelv2.HEIGHT) {
            y = GamePanelv2.PANEL_HEIGHT - Bird_Height;
            velocity = 0;
        }
    }

    /*
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, Bird_Width, Bird_Height);
    }

    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setColor(Color.RED);
        g2d.fill(new Ellipse2D.Double(x, y, Bird_Width, Bird_Height));
    }
    */
    public void draw(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;

        // 부드러운 렌더링 활성화
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // 새의 몸체 (타원)
        g2d.setColor(Color.GRAY);
        g2d.fill(new Ellipse2D.Double(x, y, Bird_Width, Bird_Height));

        // 새의 머리 (작은 타원)
        g2d.setColor(Color.ORANGE);
        g2d.fill(new Ellipse2D.Double(x + Bird_Width - 10, y + 5, 15, 15));

        // 새의 눈 (흰색 원 + 검은색 원)
        g2d.setColor(Color.WHITE);
        g2d.fillOval(x + Bird_Width - 8, y + 8, 5, 5); // 흰색 눈
        g2d.setColor(Color.BLACK);
        g2d.fillOval(x + Bird_Width - 6, y + 10, 2, 2); // 검은색 눈동자

        // 새의 부리 (삼각형)
        g2d.setColor(Color.YELLOW);
        int[] beakX = {x + Bird_Width, x + Bird_Width + 10, x + Bird_Width};
        int[] beakY = {y + 10, y + 15, y + 20};
        g2d.fillPolygon(beakX, beakY, 3);

        // 새의 날개 (반원)
        g2d.setColor(Color.DARK_GRAY);
        g2d.fillArc(x + 5, y + 5, 20, 20, 0, 180);
    }
    public Rectangle getBounds() {
        return new Rectangle(x, y, Bird_Width, Bird_Height);
    }
}