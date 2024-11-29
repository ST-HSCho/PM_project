import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Point;

public class Ball {
    private Point position; // 공의 좌표 (x, y)
    private int size;       // 공의 크기 (지름)
    private int dx;         // X축 이동 속도
    private int dy;         // Y축 이동 속도
    private Color color;    // 공의 색상
    private static final int MAX_SPEED = 15;
    private int MIN_SPEED = 6;

    // 기본 색상
    private static final Color DEFAULT_COLOR = Color.WHITE;

    // 생성자
    public Ball(Point position, int size, int dx, int dy, int min_speed) {
        this.position = position;
        this.size = size;
        this.dx = dx;
        this.dy = dy;
        this.color = DEFAULT_COLOR;
        this.MIN_SPEED = min_speed;
    }

    // 위치 반환
    public Point getPosition() {
        return position;
    }

    // 크기 반환
    public int getSize() {
        return size;
    }

    // 속도 반환
    public int getDx() {
        return dx;
    }

    public int getDy() {
        return dy;
    }

    // 속도 설정
    public void setDx(int dx) {
        this.dx = dx;
    }

    public void setDy(int dy) {
        this.dy = dy;
    }
    
    public int getMINSPEED() {
    	return MIN_SPEED;
    }

    // 공 이동
    public void move() {
        position.x += dx;
        position.y += dy;
    }

    // 벽과 충돌 처리 (아래쪽 경계선 포함)
    public boolean checkWallCollision(int panelWidth, int panelHeight) {
        // 좌우 벽 충돌
        if (position.x <= 0 || position.x + size >= panelWidth) {
            dx = -dx; // X축 방향 반전
        }
        // 위쪽 벽 충돌
        if (position.y <= 0) {
            dy = -dy; // Y축 방향 반전
        }
        // 아래쪽 경계선 충돌 (게임 오버)
        if (position.y + size >= panelHeight) {
            return true; // 공이 아래로 떨어짐, 게임 오버
        }
        return false;
    }


    public boolean checkBrickCollision(Brick brick) {
        // 공의 경계 박스 (Bounding Box) 계산
        int ballLeft = position.x;
        int ballRight = position.x + size;
        int ballTop = position.y;
        int ballBottom = position.y + size;
        
//        double X = (ballLeft + ballRight) / 2;
//        double Y = (ballTop + ballBottom) / 2;

        // 벽돌의 경계 박스 계산
        int brickLeft = brick.getPosition().x;
        int brickRight = brick.getPosition().x + brick.getWidth();
        int brickTop = brick.getPosition().y;
        int brickBottom = brick.getPosition().y + brick.getHeight();
        
        boolean Inside = (ballRight >= brickLeft && ballLeft <= brickRight && ballBottom >= brickTop && ballTop <= brickBottom);

        // 충돌 여부 확인 (경계 박스 기준)
        if (Inside) {
        	if (ballBottom - dy <= brickTop || ballTop - dy >= brickBottom) {
                dy = -dy; // Y축 방향 반전
            }
            if (ballRight - dx <= brickLeft || ballLeft - dx >= brickRight) {
                dx = -dx; // X축 방향 반전
            }
            return true; // 충돌이 발생한 경우 true 반환
        }
        return false; // 충돌이 없는 경우 false 반환
    }

    
 // Paddle과 충돌 확인 및 처리
    public boolean checkPaddleCollision(Paddle paddle) {
        // 패들의 경계 박스 계산
        int paddleLeft = paddle.getPosition().x;
        int paddleTop = paddle.getPosition().y;
        int paddleRight = paddleLeft + paddle.getWidth();
        int paddleBottom = paddleTop + paddle.getHeight();
        double paddleCenter = (paddleLeft + paddleRight) / 2;

        // 공의 경계 박스 계산
        int ballLeft = position.x;
        int ballRight = position.x + size;
        int ballTop = position.y;
        int ballBottom = position.y + size;
        double ballCenter = (ballLeft + ballRight) / 2;

        // 충돌 여부 확인 (경계 박스 기준)
        if (ballRight >= paddleLeft && ballLeft <= paddleRight && ballBottom >= paddleTop && ballTop <= paddleBottom) {
            // 공의 중심이 패들의 어느 위치에 부딪혔는지 계산 0~1 범위
            double hitPosition = (ballCenter - paddleCenter) / (paddle.getWidth() / 2);

            // 부딪힌 위치에 따라 반사 각도를 계산
            double angle = hitPosition * Math.PI / 3; // -30도에서 +30도 사이의 각도
            
            // 공의 현재 속도 크기를 유지하면서 반사 속도 계산
            double speed = Math.sqrt(dx * dx + dy * dy);

            // X축과 Y축 방향 속도를 각도에 맞게 재설정
            double newDx = (int) (Math.sin(angle) * speed);
            double newDy = -(int) (Math.abs(Math.cos(angle) * speed)); // Y축은 위쪽으로 반사되도록 항상 음수로 설정

//            if (Math.abs(newDy) < 3) {
//            	newDy = -3; // 최소 Y축 속도 설정 (항상 위쪽으로)
//            }
            
            double currentSpeed = Math.sqrt(newDx * newDx + newDy * newDy);
            if (currentSpeed < MIN_SPEED) {
                double factor = (MIN_SPEED + 0.5) / currentSpeed;
                newDx *= factor;
                newDy *= factor;
            }
            
            // 스피드 리미트
            if (Math.abs(newDx) > MAX_SPEED) {
            	newDx -= (newDx > 0) ? 1 : -1;
            }
            if (Math.abs(newDy) > MAX_SPEED) {
            	newDy -= (newDy > 0) ? 1 : -1;
            }

            // 속도를 정수형으로 설정하여 반영
            dx = (int) newDx;
            dy = (int) newDy;
            
            // 충돌이 발생한 경우 true 반환
            return true;
        }

        // 충돌이 없는 경우 false 반환
        return false;
    }


    // 공 그리기
    public void draw(Graphics2D g) {
        g.setColor(color);
        g.fillOval(position.x, position.y, size, size);
    }
}