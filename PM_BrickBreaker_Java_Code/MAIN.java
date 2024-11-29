public class MAIN {

	public static void main(String[] args) {
        // BrickBreaker 프레임 실행
        BrickBreaker game = new BrickBreaker();        
        int score = game.getFinalScore(); // 게임이 끝난 후 점수를 가져옴

        // 최종 점수 출력
        System.out.println("최종 점수: " + score);
	}

}
