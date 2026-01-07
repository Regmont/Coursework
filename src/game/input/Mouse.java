package game.input;

import game.configuration.GameConfig;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

/**
 * Обработчик мыши для управления камерой в стиле FPS.
 * <p>
 * Реализует capture-режим: при захвате курсор скрывается и центрируется,
 * а движение мыши преобразуется в углы поворота камеры.
 * Использует {@link Robot} для программного перемещения курсора.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class Mouse {
    private boolean captured = false;

    private int deltaX = 0;
    private int deltaY = 0;
    private int lastDeltaX = 0;
    private int lastDeltaY = 0;

    private Robot robot;
    private final Component window;
    private int centerX, centerY;
    private boolean needsCentering = false;

    /**
     * Создает обработчик мыши для указанного окна.
     *
     * @param window компонент окна для привязки слушателей
     */
    public Mouse(Component window) {
        this.window = window;

        try {
            robot = new Robot();
            robot.setAutoDelay(0);
            robot.setAutoWaitForIdle(false);
        } catch (AWTException e) {
            System.out.println("Can't create a Robot");
        }

        window.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1 && !captured) {
                    capture();
                }
            }
        });

        window.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                if (captured) {
                    int newDeltaX = e.getX() - centerX;
                    int newDeltaY = e.getY() - centerY;

                    deltaX += Math.max(-50, Math.min(50, newDeltaX));
                    deltaY += Math.max(-50, Math.min(50, newDeltaY));

                    needsCentering = true;
                }
            }

            public void mouseDragged(MouseEvent e) {
                mouseMoved(e);
            }
        });
    }

    /**
     * Обновляет состояние мыши. Должен вызываться каждый кадр.
     * <p>
     * Центрирует курсор если требуется и сглаживает дельты движения.
     */
    public void update() {
        lastDeltaX = deltaX;
        lastDeltaY = deltaY;

        if (needsCentering && captured && robot != null) {
            centerX = window.getWidth() / 2;
            centerY = window.getHeight() / 2;

            try {
                robot.mouseMove(
                        window.getLocationOnScreen().x + centerX,
                        window.getLocationOnScreen().y + centerY
                );
                Thread.yield();
            } catch (Exception e) {
                e.printStackTrace();
            }

            deltaX = Math.max(-10, Math.min(10, deltaX - lastDeltaX / 2));
            deltaY = Math.max(-10, Math.min(10, deltaY - lastDeltaY / 2));

            needsCentering = false;
        }
    }

    /**
     * Возвращает смещение мыши по оси X с момента последнего вызова
     * и применяет сглаживание для оставшегося движения.
     * <p>
     * Сглаживание уменьшает дельту по экспоненциальному закону
     * с коэффициентом {@link GameConfig#MOUSE_SMOOTHING_FACTOR}.
     *
     * @return смещение в пикселях (может быть отрицательным)
     */
    public int getDeltaX() {
        int dx = lastDeltaX;
        deltaX = (int)(deltaX * GameConfig.MOUSE_SMOOTHING_FACTOR);

        return dx;
    }

    /**
     * Возвращает смещение мыши по оси Y с момента последнего вызова
     * и применяет сглаживание для оставшегося движения.
     * <p>
     * Сглаживание уменьшает дельту по экспоненциальному закону
     * с коэффициентом {@link GameConfig#MOUSE_SMOOTHING_FACTOR}.
     *
     * @return смещение в пикселях (может быть отрицательным)
     */
    public int getDeltaY() {
        int dy = lastDeltaY;
        deltaY = (int)(deltaY * GameConfig.MOUSE_SMOOTHING_FACTOR);

        return dy;
    }

    /**
     * Захватывает мышь для управления камерой.
     * <p>
     * Скрывает курсор, сбрасывает накопленные дельты и активирует центрирование.
     */
    public void capture() {
        captured = true;
        deltaX = 0;
        deltaY = 0;
        lastDeltaX = 0;
        lastDeltaY = 0;

        window.setCursor(window.getToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                new Point(0, 0),
                "null"
        ));

        needsCentering = true;
    }

    /**
     * Освобождает мышь (возвращает стандартный курсор).
     */
    public void release() {
        captured = false;
        window.setCursor(Cursor.getDefaultCursor());
    }

    /**
     * Проверяет, находится ли мышь в захваченном режиме.
     *
     * @return {@code true} если мышь захвачена
     */
    public boolean isCaptured() {
        return captured;
    }

    @Override
    public String toString() {
        return String.format(
                "Mouse[captured=%b, delta=(%d,%d), needsCentering=%b]",
                captured, deltaX, deltaY, needsCentering
        );
    }
}
