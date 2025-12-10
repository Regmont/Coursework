package rendering.edges;

import geometricObjects.*;
import rendering.utils.GeometryUtils;

import java.awt.*;
import java.util.Map;

public class EdgeRenderer {
    private final GeometryUtils geometryUtils;
    private final PixelRenderer pixelRenderer;
    public static final double EDGE_RENDERING_DISTANCE = 0.7;

    public EdgeRenderer(Color backgroundColor) {
        this.geometryUtils = new GeometryUtils();
        this.pixelRenderer = new PixelRenderer(backgroundColor);
    }

    public void renderEdges(Map<Edge, EdgeType> edgeTypes, Color[][] colorBuffer,
                            double[][] depthBuffer, int width, int height) {
        for (Map.Entry<Edge, EdgeType> entry : edgeTypes.entrySet()) {
            Edge edge = entry.getKey();
            EdgeType type = entry.getValue();

            Triangle visibleTriangle = edge.getVisibleTriangle();
            if (visibleTriangle == null) {
                continue;
            }

            BoundingBox boundingBox = geometryUtils.getEdgeBoundingBox(edge, width, height);
            if (boundingBox == null) {
                continue;
            }

            for (int y = boundingBox.minY(); y <= boundingBox.maxY(); y++) {
                for (int x = boundingBox.minX(); x <= boundingBox.maxX(); x++) {
                    double centerX = x + 0.5;
                    double centerY = y + 0.5;

                    if (geometryUtils.distanceToSegment(centerX, centerY,
                            edge.getP1(), edge.getP2()) >= EDGE_RENDERING_DISTANCE) {
                        continue;
                    }

                    if (type == EdgeType.BOUNDARY) {
                        pixelRenderer.renderBoundaryEdgePixel(x, y, centerX, centerY,
                                visibleTriangle, colorBuffer, depthBuffer);
                    } else if (type == EdgeType.INTERIOR) {
                        pixelRenderer.renderInteriorEdgePixel(x, y, centerX, centerY,
                                edge, colorBuffer, depthBuffer);
                    }
                }
            }
        }
    }
}