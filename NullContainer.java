import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class NullContainer extends JPanel implements ActionListener {
    int r = 50; //초기 diameter
    int x = 0, y = 0;
    public int dx = 2;
    public int dy = 2;
    public Timer timer; // 화면 업데이트용 타이머 (:=주사율)

    public NullContainer() {
        timer = new Timer(10, this); //대충 10ms
        timer.start();
        //(int) Math.random() * (최댓값-최소값+1) + 최소값
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.RED);
        g.fillOval(x,y,r,r);
    }

    public void actionPerformed(ActionEvent e) {

        x += dx;
        y += dy; // x = x + vt (vt = dx * 1)

        if (x <= 0 || x >= getWidth() - 30) // 만약 화면의 경계를 벗어나면 or 원점 (0,0) 벗어나면
        {
            dx = -dx; // x축 방향 반사, 완전탄성충돌 가정
            r = (int) (Math.random() * (100 - 50 + 1)) + 50;
        }
        if (y <= 0 || y >= getHeight() - 30)
        {
            dy = -dy;
            r = (int) (Math.random() * (100 - 50 + 1)) + 50;
        }

        repaint(); //repaint는 다시 그릴 때, update는 새로 그릴 때 사용(repaint가 대부분)
    }

    public static void main(String[] args) {
        JFrame frame = new JFrame("공굴리기"); //그림 그릴 캔버스 생성
        NullContainer panel = new NullContainer();

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 400); // 창 크기 설정
        frame.add(panel);
        frame.setVisible(true);
    }
}
