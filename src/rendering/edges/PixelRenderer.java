package rendering.edges;

import geometricObjects.*;
import rendering.utils.GeometryUtils;
import rendering.utils.ColorUtils;

import java.awt.*;
import java.awt.geom.Point2D;

public class PixelRenderer {
    private final GeometryUtils geometryUtils;
    private final ColorUtils colorUtils;
    private final Color backgroundColor;
    public static final int SUPERSAMPLING_X = 4;
    public static final int SUPERSAMPLING_Y = 4;

    public PixelRenderer(Color backgroundColor) {
        this.geometryUtils = new GeometryUtils();
        this.colorUtils = new ColorUtils();
        this.backgroundColor = backgroundColor;
    }

    public void renderBoundaryEdgePixel(int x, int y, double centerX, double centerY,
                                        Triangle triangle, Color[][] colorBuffer,
                                        double[][] depthBuffer) {
        int hits = geometryUtils.performSupersampling(x, y, triangle, SUPERSAMPLING_X, SUPERSAMPLING_Y);
        if (hits == 0) {
            return;
        }

        double depth;
        if (geometryUtils.isPointInTriangle(centerX, centerY, triangle)) {
            depth = geometryUtils.calculateDepthAtPoint(centerX, centerY, triangle);
        } else {
            Point2D projectedPoint = geometryUtils.projectToClosestEdge(centerX, centerY, triangle);
            depth = geometryUtils.calculateDepthAtPoint(projectedPoint.getX(),
                    projectedPoint.getY(), triangle);
        }

        if (depth < depthBuffer[x][y]) {
            depthBuffer[x][y] = depth;

            double alpha = (double) hits / (SUPERSAMPLING_X * SUPERSAMPLING_Y);
            Color triColor = triangle.getColor();
            colorBuffer[x][y] = colorUtils.blendColors(backgroundColor, triColor, alpha);
        }
    }

    public void renderInteriorEdgePixel(int x, int y, double centerX, double centerY,
                                        Edge edge, Color[][] colorBuffer,
                                        double[][] depthBuffer) {
        Triangle triangle1 = edge.getTriangle1();
        Triangle triangle2 = edge.getTriangle2();

        if (!triangle1.isVisibleFromCameraCenter() ||
                !triangle2.isVisibleFromCameraCenter()) {
            return;
        }

        if (geometryUtils.isPointInTriangle(centerX, centerY, triangle1)) {
            double depth = geometryUtils.calculateDepthAtPoint(centerX, centerY, triangle1);
            if (depth < depthBuffer[x][y]) {
                depthBuffer[x][y] = depth;
                colorBuffer[x][y] = triangle1.getColor();
            }
        }

        if (geometryUtils.isPointInTriangle(centerX, centerY, triangle2)) {
            double depth = geometryUtils.calculateDepthAtPoint(centerX, centerY, triangle2);
            if (depth < depthBuffer[x][y]) {
                depthBuffer[x][y] = depth;
                colorBuffer[x][y] = triangle2.getColor();
            }
        }
    }
}