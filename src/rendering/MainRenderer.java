package rendering;

import geometricObjects.*;
import rendering.triangles.TriangleRenderer;
import rendering.utils.BufferManager;

import java.awt.*;
import java.util.List;

public class MainRenderer {
    private final TriangleRenderer triangleRenderer;
    private final BufferManager bufferManager;

    public MainRenderer() {
        this.triangleRenderer = new TriangleRenderer();
        this.bufferManager = new BufferManager();
    }

    public void renderScene(List<Mesh> objects, Color[][] colorBuffer, double[][] depthBuffer,
                            Color backgroundColor) {
        int width = colorBuffer.length;
        int height = colorBuffer[0].length;

        bufferManager.clearBuffers(colorBuffer, depthBuffer, backgroundColor);

        triangleRenderer.renderSolidAreas(objects, colorBuffer, depthBuffer,
                width, height);
    }
}