package game.input;

import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;
import java.awt.image.BufferedImage;

public class Mouse {
    private boolean captured = false;
    private int deltaX = 0;
    private int deltaY = 0;

    private Robot robot;
    private final Component window;
    private int centerX, centerY;

    public Mouse(Component window) {
        this.window = window;

        try {
            robot = new Robot();
        } catch (AWTException e) {
            System.out.println("Can't create a Robot");
        }

        window.addMouseListener(new MouseAdapter() {
            public void mousePressed(MouseEvent e) {
                if (e.getButton() == MouseEvent.BUTTON1) {
                    if (!captured) {
                        capture();
                    }
                }
            }
        });

        window.addMouseMotionListener(new MouseMotionAdapter() {
            public void mouseMoved(MouseEvent e) {
                if (captured) {
                    deltaX = e.getX() - centerX;
                    deltaY = e.getY() - centerY;
                }
            }

            public void mouseDragged(MouseEvent e) {
                mouseMoved(e);
            }
        });
    }

    public void update() {
        if (captured && robot != null) {
            centerX = window.getWidth() / 2;
            centerY = window.getHeight() / 2;

            robot.mouseMove(
                    window.getLocationOnScreen().x + centerX,
                    window.getLocationOnScreen().y + centerY
            );
        }
    }

    public int getDeltaX() {
        int dx = deltaX;
        deltaX = 0;

        return dx;
    }

    public int getDeltaY() {
        int dy = deltaY;
        deltaY = 0;

        return dy;
    }

    public void capture() {
        captured = true;
        window.setCursor(window.getToolkit().createCustomCursor(
                new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB),
                new Point(0, 0),
                "null"
        ));
    }

    public void release() {
        captured = false;
        window.setCursor(Cursor.getDefaultCursor());
    }

    public boolean isCaptured() { return captured; }
}