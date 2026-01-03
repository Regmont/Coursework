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
    private int lastDeltaX = 0;
    private int lastDeltaY = 0;

    private Robot robot;
    private final Component window;
    private int centerX, centerY;
    private boolean needsCentering = false;

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
                Thread.sleep(0);
            } catch (Exception e) {
                e.printStackTrace();
            }

            deltaX = Math.max(-10, Math.min(10, deltaX - lastDeltaX / 2));
            deltaY = Math.max(-10, Math.min(10, deltaY - lastDeltaY / 2));

            needsCentering = false;
        }
    }

    public int getDeltaX() {
        int dx = lastDeltaX;
        deltaX = deltaX * 2 / 3;

        return dx;
    }

    public int getDeltaY() {
        int dy = lastDeltaY;
        deltaY = deltaY * 2 / 3;

        return dy;
    }

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

    public void release() {
        captured = false;
        window.setCursor(Cursor.getDefaultCursor());
    }

    public boolean isCaptured() {
        return captured;
    }
}