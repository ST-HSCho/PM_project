package birdgame;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;

public class Bird {
    int x, y;
    int velocity = 0;

    final int GRAVITY = 1;
    final int JUMP = -10;
    final int Bird_Height = 20;
    final int Bird_Width = 20;

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
        } else if (y + Bird_Height > GamePanel.HEIGHT) {
            y = GamePanel.PANEL_HEIGHT - Bird_Height;
            velocity = 0;
        }
    }

    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, Bird_Width, Bird_Height);
    }

    public Rectangle getBounds() {
        return new Rectangle(x, y, Bird_Width, Bird_Height);
    }
}