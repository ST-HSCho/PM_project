package brickbreaker;

import javax.swing.*;
import java.awt.*;

public class BrickBreaker extends JFrame {
    private GamePanel BB;
	
    public BrickBreaker() {
        setTitle("Brick Breaker");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setResizable(false);

     // GamePanel 초기화 및 위치 설정
        BB = new GamePanel();  // BB 필드를 직접 초기화합니다.
        add(BB); // 기본 레이아웃인 BorderLayout의 CENTER에 추가됨

        // 프레임 크기를 컴포넌트 크기에 맞게 조정
        pack();

        // JFrame 보이기
        setVisible(true);
    }

    // 게임 종료 후 최종 점수를 반환하는 메서드
    public int getFinalScore() {
        // 게임이 종료될 때까지 대기
        while (BB.isGameRunning()) {
            try {
                Thread.sleep(100); // 게임이 종료될 때까지 100ms 간격으로 체크
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return BB.returnScore();
    }
}