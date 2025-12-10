package rendering;

import geometricObjects.*;
import math.Point3D;
import sceneObjects.Camera;
import rendering.edges.EdgeRenderer;
import rendering.edges.EdgeClassifier;
import rendering.triangles.TriangleRenderer;
import rendering.utils.BufferManager;

import java.awt.*;
import java.util.*;
import java.util.List;

public class MainRenderer {
    private final EdgeRenderer edgeRenderer;
    private final TriangleRenderer triangleRenderer;
    private final BufferManager bufferManager;
    private final EdgeClassifier edgeClassifier;

    public MainRenderer(Color backgroundColor) {
        this.edgeRenderer = new EdgeRenderer(backgroundColor);
        this.triangleRenderer = new TriangleRenderer();
        this.bufferManager = new BufferManager();
        this.edgeClassifier = new EdgeClassifier();
    }

    public void renderScene(List<Mesh> objects, Color[][] colorBuffer, double[][] depthBuffer,
                            Color backgroundColor) {
        int width = colorBuffer.length;
        int height = colorBuffer[0].length;

        bufferManager.clearBuffers(colorBuffer, depthBuffer, backgroundColor);

        Map<Edge, EdgeType> edgeTypes = edgeClassifier.collectEdgeTypes(objects);

        triangleRenderer.renderSolidAreas(objects, colorBuffer, depthBuffer,
                width, height);
        edgeRenderer.renderEdges(edgeTypes, colorBuffer, depthBuffer,
                width, height);
    }
}