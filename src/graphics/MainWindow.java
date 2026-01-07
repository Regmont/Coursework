package graphics;

import graphics.renderer.GameRenderer;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

public class MainWindow extends Frame {
    private final GameRenderer gameRenderer;

    public MainWindow(SceneSystem sceneSystem, String title, int width, int height) {
        gameRenderer = new GameRenderer(sceneSystem);

        setTitle(title);
        setSize(width, height);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    @Override
    public void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        BufferedImage frame = gameRenderer.getFrame(width, height);

        g.drawImage(frame, 0, 0, null);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }
}
