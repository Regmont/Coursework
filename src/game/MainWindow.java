package game;

import sceneObjects.Camera;
import geometricObjects.Mesh;
import rendering.MainRenderer;
import rendering.SceneTransformer;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class MainWindow extends JFrame {
    private ArrayList<Mesh> objects;
    private final RenderPanel renderPanel;
    private final Color backgroundColor;

    public MainWindow(ArrayList<Mesh> objects, String title, int width, int height,
                      Color backgroundColor) {
        this.objects = objects;
        this.backgroundColor = backgroundColor;

        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(width, height);
        setLocationRelativeTo(null);

        renderPanel = new RenderPanel();
        add(renderPanel);
    }

    public void updateObjects(ArrayList<Mesh> newObjects) {
        this.objects = newObjects;
        renderPanel.repaint();
    }

    class RenderPanel extends JPanel {
        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);

            int width = getWidth();
            int height = getHeight();

            SceneTransformer.setScreenSize(width, height);

            Color[][] colorBuffer = new Color[width][height];
            double[][] depthBuffer = new double[width][height];

            MainRenderer renderer = new MainRenderer();
            renderer.renderScene(objects, colorBuffer, depthBuffer, backgroundColor);

            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    Color color = colorBuffer[x][y];
                    int rgb = (color != null) ? color.getRGB() : backgroundColor.getRGB();
                    image.setRGB(x, y, rgb);
                }
            }

            g.drawImage(image, 0, 0, null);
        }
    }
}