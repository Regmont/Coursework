package graphics.renderer;

import geometry.*;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class MainRenderer {
    public static void renderScene(List<Mesh> meshes, Color[][] colorBuffer, double[][] depthBuffer,
                            Color backgroundColor) {
        int width = colorBuffer.length;
        int height = colorBuffer[0].length;

        clearBuffers(colorBuffer, depthBuffer, backgroundColor);

        TriangleRenderer.renderTriangle(meshes, colorBuffer, depthBuffer, width, height);
    }

    public static void clearBuffers(Color[][] colorBuffer, double[][] depthBuffer, Color backgroundColor) {
        for (double[] row : depthBuffer) {
            Arrays.fill(row, Double.POSITIVE_INFINITY);
        }

        for (Color[] colors : colorBuffer) {
            Arrays.fill(colors, backgroundColor);
        }
    }
}