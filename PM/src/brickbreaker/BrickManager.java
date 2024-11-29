package brickbreaker;

import java.awt.Point;
import java.util.ArrayList;

public class BrickManager {
    public static ArrayList<Brick> createBricks(int rows, int cols, int brickWidth, int brickHeight, int padding) {
        ArrayList<Brick> bricks = new ArrayList<>();
        for (int row = 0; row < rows; row++) {
            for (int col = 0; col < cols; col++) {
                Point position = new Point(
                        50 + col * (brickWidth + padding),
                        50 + row * (brickHeight + padding)
                );
                boolean isSpecial = (row + col) % 5 == 0; // 특수 블록은 5개 간격으로 배치
                bricks.add(new Brick(position, isSpecial, brickWidth, brickHeight));
            }
        }
        return bricks;
    }
}
