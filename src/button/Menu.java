package button;

import javax.swing.SwingUtilities;
import mainmenu.MenuGUI;
import javax.swing.JComponent;
import brickbreaker.BrickBreaker;

public class Menu {
    public static void goToMenu(JComponent component) {
        // Stop the music if we're coming from BrickBreaker
        if (SwingUtilities.getWindowAncestor(component) instanceof BrickBreaker) {
            BrickBreaker game = (BrickBreaker) SwingUtilities.getWindowAncestor(component);
            game.stopBackgroundMusic();
        }
        
        SwingUtilities.getWindowAncestor(component).dispose();
        new MenuGUI();
    }
}
