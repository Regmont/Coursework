package rendering.triangles;

import geometricObjects.*;
import math.Point3D;
import rendering.utils.GeometryUtils;

import java.awt.*;
import java.util.List;

public class TriangleRenderer {
    private final GeometryUtils geometryUtils;
    public static final double EDGE_THRESHOLD = 0.5;

    public TriangleRenderer() {
        this.geometryUtils = new GeometryUtils();
    }

    public void renderSolidAreas(List<Mesh> objects, Color[][] colorBuffer,
                                 double[][] depthBuffer,
                                 int width, int height) {
        for (Mesh object : objects) {
            for (Triangle triangle : object.getTriangles()) {
                if (!triangle.isVisibleFromCameraCenter()) {
                    continue;
                }

                BoundingBox boundingBox = triangle.getBoundingBox();

                int minX = Math.max(0, boundingBox.minX());
                int maxX = Math.min(width - 1, boundingBox.maxX());
                int minY = Math.max(0, boundingBox.minY());
                int maxY = Math.min(height - 1, boundingBox.maxY());

                if (minX > maxX || minY > maxY) {
                    continue;
                }

                for (int y = minY; y <= maxY; y++) {
                    for (int x = minX; x <= maxX; x++) {
                        double centerX = x + 0.5;
                        double centerY = y + 0.5;

                        if (geometryUtils.distanceToClosestEdge(centerX, centerY, triangle) < EDGE_THRESHOLD) {
                            continue;
                        }

                        if (!geometryUtils.isPointInTriangle(centerX, centerY, triangle)) {
                            continue;
                        }

                        double depth = geometryUtils.calculateDepthAtPoint(centerX, centerY, triangle);
                        if (depth < depthBuffer[x][y]) {
                            depthBuffer[x][y] = depth;
                            colorBuffer[x][y] = triangle.getColor();
                        }
                    }
                }
            }
        }
    }
}