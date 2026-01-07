package game.input;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Keyboard {
    private boolean wPressed = false;
    private boolean sPressed = false;
    private boolean aPressed = false;
    private boolean dPressed = false;
    private boolean escapePressed = false;

    public Keyboard(Component window) {
        window.addKeyListener(new KeyAdapter() {
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_W) wPressed = true;
                if (key == KeyEvent.VK_S) sPressed = true;
                if (key == KeyEvent.VK_A) aPressed = true;
                if (key == KeyEvent.VK_D) dPressed = true;
                if (key == KeyEvent.VK_ESCAPE) escapePressed = true;
            }

            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_W) wPressed = false;
                if (key == KeyEvent.VK_S) sPressed = false;
                if (key == KeyEvent.VK_A) aPressed = false;
                if (key == KeyEvent.VK_D) dPressed = false;
            }
        });
    }

    public boolean isWPressed() { return wPressed; }
    public boolean isAPressed() { return aPressed; }
    public boolean isSPressed() { return sPressed; }
    public boolean isDPressed() { return dPressed; }
    public boolean isEscapePressed() { return escapePressed; }

    public void resetEscape() {
        escapePressed = false;
    }

    public boolean isAnyMovementPressed() {
        return wPressed || sPressed || aPressed || dPressed;
    }
}
