package game.input;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

/**
 * Обработчик клавиатуры для управления камерой/игроком.
 * <p>
 * Отслеживает состояние WASD клавиш для движения и Escape для выхода.
 * Используется для управления камерой в 3D сцене.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class Keyboard {
    private boolean wPressed = false;
    private boolean sPressed = false;
    private boolean aPressed = false;
    private boolean dPressed = false;
    private boolean escapePressed = false;

    /**
     * Создает обработчик клавиатуры для указанного компонента окна.
     *
     * @param window компонент окна, к которому привязывается слушатель клавиш
     */
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

    /**
     * Сбрасывает состояние клавиши Escape.
     * <p>
     * Используется после обработки нажатия Escape, чтобы избежать
     * множественных срабатываний.
     */
    public void resetEscape() {
        escapePressed = false;
    }

    /**
     * Проверяет, нажата ли любая клавиша движения (WASD).
     *
     * @return {@code true} если хотя бы одна из WASD нажата
     */
    public boolean isAnyMovementPressed() {
        return wPressed || sPressed || aPressed || dPressed;
    }

    @Override
    public String toString() {
        return String.format(
                "Keyboard[W=%b, A=%b, S=%b, D=%b, ESC=%b]",
                wPressed, aPressed, sPressed, dPressed, escapePressed
        );
    }
}
