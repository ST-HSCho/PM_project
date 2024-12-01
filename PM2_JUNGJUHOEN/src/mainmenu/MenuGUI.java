package mainmenu;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import brickbreaker.BrickBreaker;
import pingpong.PingPong;
import java.awt.Color;
import java.awt.Font;
import leaderboard.LeaderboardGUI;


public class MenuGUI extends JFrame {
	
	public MenuGUI() {
		
		setTitle("Game Start!"); // 창 제목 설정
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 창 닫기 시 종료
		setSize(800,800); // 창 크기 설정
		setLayout(null); // 레이아웃 설정
		
		
		JLabel welcomelabel = new JLabel("Welcome to the Game!");
		welcomelabel.setFont(new Font("Arial", Font.BOLD, 35)); // 글꼴 설정
		welcomelabel.setForeground(Color.BLACK); // 글자 색깔 설정
		welcomelabel.setHorizontalAlignment(SwingConstants.CENTER); // 중앙 정렬
		welcomelabel.setBounds(0, 50, 800, 50); // 위치 크기 설정
		add(welcomelabel);
		
		
		JButton birdgameButton = new JButton("Flappy Bird"); // 플래피버드
		birdgameButton.setBounds(100,200,150,50);
		birdgameButton.setBackground(Color.YELLOW);
		birdgameButton.setForeground(Color.BLACK);
		birdgameButton.setFocusPainted(false); // 테두리지우기
		
		birdgameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				
				dispose();
				birdgame.FlappyBird.main(new String[] {});
				
			}
			
		});
		
		add(birdgameButton);
		
		JButton brickgameButton = new JButton("Brick Breaker"); // 벽돌깨기
		brickgameButton.setBounds(300,200,150,50);
		brickgameButton.setBackground(Color.YELLOW);
		brickgameButton.setForeground(Color.BLACK);
		brickgameButton.setFocusPainted(false); // 테두리지우기
		brickgameButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				new BrickBreaker();
			}
			
		});
		
		
		add(brickgameButton);
		
		JButton pingpongButton = new JButton("Pingpong"); // 핑퐁
		pingpongButton.setBounds(500,200,150,50);
		pingpongButton.setBackground(Color.YELLOW);
		pingpongButton.setForeground(Color.BLACK);
		pingpongButton.setFocusPainted(false); // 테두리지우기
		pingpongButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				dispose();
				JFrame frame = new JFrame("Ping Pong");
				PingPong game = new PingPong();
				
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				frame.setResizable(false);
				frame.add(game);
				frame.pack();
				frame.setLocationRelativeTo(null);
				frame.setVisible(true);
				
				Thread gamethread = new Thread(game);
				gamethread.start();
			}
			
		});
		
		add(pingpongButton);
		
		JButton leaderboardButton = new JButton("Leaderboard");
		
		leaderboardButton.setBounds(300,350,150,50);
		leaderboardButton.setBackground(Color.GREEN);
		leaderboardButton.setForeground(Color.BLACK);
		leaderboardButton.setFocusPainted(false); // 테두리지우기
		leaderboardButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				setContentPane(new LeaderboardGUI(MenuGUI.this, "FlappyBird", "BrickBreaker"));
				revalidate();
				repaint();
			}
		});
		
		add(leaderboardButton);
		
		JButton exitButton = new JButton("Exit"); // 종료 버튼
	
		exitButton.setBounds(300,500,150,50);
		exitButton.setBackground(Color.CYAN);
		exitButton.setForeground(Color.BLACK);
		exitButton.setFocusPainted(false); // 테두리지우기
		exitButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				System.exit(0); // 종료 
			}
			
		});
		
		add(exitButton);
		setVisible(true); // 프레임 화면표시

		
	}

	public static void main(String[] args) {
		
		new MenuGUI();

	}

}
