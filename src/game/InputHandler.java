package game;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import javax.swing.JFrame;

public class InputHandler {
    private boolean wPressed = false;
    private boolean sPressed = false;
    private boolean aPressed = false;
    private boolean dPressed = false;

    public void setupKeyListeners(JFrame window, GameController gameController) {
        window.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int keyCode = e.getKeyCode();

                if (keyCode == KeyEvent.VK_ESCAPE) {
                    if (gameController.isMouseCaptured()) {
                        gameController.releaseMouse();
                    } else {
                        gameController.captureMouse();
                    }
                    return;
                }

                char keyChar = Character.toLowerCase(e.getKeyChar());
                switch (keyChar) {
                    case 'w':
                    case 'ц': wPressed = true; break;
                    case 's':
                    case 'ы': sPressed = true; break;
                    case 'a':
                    case 'ф': aPressed = true; break;
                    case 'd':
                    case 'в': dPressed = true; break;
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                char keyChar = Character.toLowerCase(e.getKeyChar());
                switch (keyChar) {
                    case 'w':
                    case 'ц': wPressed = false; break;
                    case 's':
                    case 'ы': sPressed = false; break;
                    case 'a':
                    case 'ф': aPressed = false; break;
                    case 'd':
                    case 'в': dPressed = false; break;
                }
            }
        });
    }

    public boolean isWPressed() {
        return wPressed;
    }

    public boolean isSPressed() {
        return sPressed;
    }

    public boolean isAPressed() {
        return aPressed;
    }

    public boolean isDPressed() {
        return dPressed;
    }

    public boolean isAnyMovementKeyPressed() {
        return wPressed || sPressed || aPressed || dPressed;
    }
}