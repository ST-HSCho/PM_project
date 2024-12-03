package button;

import javax.swing.SwingUtilities;
import birdgame.FlappyBird;
import brickbreaker.BrickBreaker;
import javax.swing.JComponent;

public class Retry {
    public static void retryGame(JComponent component, String game) {
        SwingUtilities.getWindowAncestor(component).dispose();

        if ("FlappyBird".equalsIgnoreCase(game)) {
            FlappyBird.main(new String[] {});
        } else if ("BrickBreaker".equalsIgnoreCase(game)) {
            new BrickBreaker();
        }
    }
}
