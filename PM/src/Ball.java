import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class Ball extends JPanel implements ActionListener {
    public int x = 0, y = 0; // 초기 위치
    public int dx = 2, dy = 2; // 속도
    public Timer timer; // 화면 업데이트용 타이머 (:=주사율)

    public Ball() {
        timer = new Timer(10, this); //대충 10ms
        timer.start();
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillOval(x, y, 30, 30); // 공의 크기 30x30 픽셀
    }

    public void actionPerformed(ActionEvent e) {

        x += dx;
        y += dy; // x = x + vt (vt = dx * 1)

        if (x <= 0 || x >= getWidth() - 30) // 만약 화면의 경계를 벗어나면 or 원점 (0,0) 벗어나면
        {
            dx = -dx; // x축 방향 반사, 완전탄성충돌 가정
        }
        if (y <= 0 || y >= getHeight() - 30)
        {
            dy = -dy;
        }

        repaint(); //repaint는 다시 그릴 때, update는 새로 그릴 때 사용(repaint가 대부분)
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("공굴리기"); //그림 그릴 캔버스 생성
        Ball panel = new Ball();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400); // 창 크기 설정
        frame.add(panel);
        frame.setVisible(true);
    }
}

//https://m.blog.naver.com/scyan2011/221781355770
//https://blog.naver.com/PostView.nhn?blogId=rain483&logNo=220736840879
