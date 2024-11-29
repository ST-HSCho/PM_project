package brickbreaker;

import javax.swing.*;
import java.awt.*;

public class BrickBreaker extends JFrame {

    public BrickBreaker() {
        setTitle("Brick Breaker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);
        
        // GamePanel 추가 및 위치 설정
        GamePanel gamePanel = new GamePanel();
        add(gamePanel); // 기본 레이아웃인 BorderLayout의 CENTER에 추가됨

        // 프레임 크기를 컴포넌트 크기에 맞게 조정
        pack();

        // JFrame 보이기
        setVisible(true);
    }
    

    public static void main(String[] args) {
        // BrickBreaker 프레임 실행
        new BrickBreaker();
    }
}
