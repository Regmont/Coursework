package game;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import javax.swing.JFrame;

public class MouseHandler {
    public void setupMouseListeners(JFrame window, GameController gameController) {
        window.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (!gameController.isMouseCaptured() && e.getButton() == MouseEvent.BUTTON1) {
                    gameController.captureMouse();
                }
            }
        });

        window.addMouseMotionListener(new MouseMotionAdapter() {
            @Override
            public void mouseMoved(MouseEvent e) {
                gameController.handleMouseMove(e);
            }

            @Override
            public void mouseDragged(MouseEvent e) {
                gameController.handleMouseMove(e);
            }
        });
    }
}