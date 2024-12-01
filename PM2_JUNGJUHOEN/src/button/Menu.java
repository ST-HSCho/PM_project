package button;

import javax.swing.SwingUtilities;
import mainmenu.MenuGUI;
import javax.swing.JComponent;

public class Menu {
    public static void goToMenu(JComponent component) {
        SwingUtilities.getWindowAncestor(component).dispose();
        new MenuGUI();
    }
}
