package pausegame;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import mainmenu.MenuGUI;
import brickbreaker.BrickBreaker;

public class PauseGameHandler {

    public static void handleEscape(JPanel gamePanel, Runnable onPause) {
        onPause.run(); 

        JDialog pauseDialog = new JDialog((JFrame) SwingUtilities.getWindowAncestor(gamePanel), "Pause Menu", true);
        pauseDialog.setLayout(new GridLayout(2, 1));
        pauseDialog.setSize(200, 100);
        pauseDialog.setLocationRelativeTo(gamePanel);

        JButton resumeButton = new JButton("Resume");
        resumeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseDialog.dispose();
                onPause.run();
            }
        });

        JButton menuButton = new JButton("Menu");
        menuButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                pauseDialog.dispose();
                JFrame frame = (JFrame) SwingUtilities.getWindowAncestor(gamePanel);
                if (frame instanceof BrickBreaker) {
                    ((BrickBreaker) frame).stopBackgroundMusic(); // Stop music when going to menu
                }
                frame.dispose();
                new MenuGUI();
            }
        });

        pauseDialog.add(resumeButton);
        pauseDialog.add(menuButton);
        pauseDialog.setVisible(true);
    }
}
