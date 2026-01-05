package graphics.renderer;

import graphics.SceneSystem;
import graphics.utils.ColorUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

public class GameRenderer {
    private Color[][] colorBuffer;
    private double[][] depthBuffer;
    private BufferedImage frame;
    private final SceneSystem sceneSystem;

    public GameRenderer(SceneSystem sceneSystem) {
        this.sceneSystem = sceneSystem;
    }

    public BufferedImage getFrame(int width, int height) {
        resizeBuffersIfWindowSizeChanged(width, height);
        MainRenderer.renderScene(colorBuffer, depthBuffer, ColorUtils.getSkyColor(sceneSystem.getSky()), sceneSystem);
        copyColorBufferToImage(width, height);

        return frame;
    }

    private void resizeBuffersIfWindowSizeChanged(int width, int height) {
        if (colorBuffer == null || colorBuffer.length != width || colorBuffer[0].length != height) {
            colorBuffer = new Color[width][height];
            depthBuffer = new double[width][height];
            frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
    }

    private void copyColorBufferToImage(int width, int height) {
        int[] pixels = ((DataBufferInt) frame.getRaster().getDataBuffer()).getData();
        int backgroundRGB = ColorUtils.getSkyColor(sceneSystem.getSky()).getRGB();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = colorBuffer[x][y];
                pixels[y * width + x] = (color != null) ? color.getRGB() : backgroundRGB;
            }
        }
    }
}
