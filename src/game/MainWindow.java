package game;

import geometry.Mesh;
import graphics.SceneSystem;
import graphics.light.SkyLight;
import graphics.renderer.MainRenderer;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;
import java.util.List;

public class MainWindow extends Frame {
    private final SkyLight skyLight;
    private final SceneSystem sceneSystem;

    private Color[][] colorBuffer;
    private double[][] depthBuffer;
    private BufferedImage image;

    public MainWindow(SceneSystem sceneSystem, String title, int width, int height,
                      Color bgColor, double bgBrightness) {
        this.sceneSystem = sceneSystem;

        setTitle(title);
        setSize(width, height);
        setLocationRelativeTo(null);

        skyLight = new SkyLight(bgColor, bgBrightness);

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

        if (colorBuffer == null || colorBuffer.length != width || colorBuffer[0].length != height) {
            colorBuffer = new Color[width][height];
            depthBuffer = new double[width][height];
            image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }

        MainRenderer.clearBuffers(colorBuffer, depthBuffer, skyLight.getSkyColor());

        List<Mesh> meshes = sceneSystem.getTransformedMeshes(width, height);
        MainRenderer.renderScene(meshes, colorBuffer, depthBuffer, skyLight.getSkyColor());

        copyColorBufferToImage(width, height);

        g.drawImage(image, 0, 0, null);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

    private void copyColorBufferToImage(int width, int height) {
        int[] pixels = ((DataBufferInt) image.getRaster().getDataBuffer()).getData();
        int backgroundRGB = skyLight.getSkyColor().getRGB();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = colorBuffer[x][y];
                pixels[y * width + x] = (color != null) ? color.getRGB() : backgroundRGB;
            }
        }
    }
}