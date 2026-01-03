package graphics.renderer;

import geometry.*;
import graphics.light.LightCalculator;
import graphics.light.PointLight;
import math.Vector3D;
import graphics.utils.GeometryUtils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

public class TriangleRenderer {
    private static SpatialGrid spatialGrid;
    private static int lastWidth = -1;
    private static int lastHeight = -1;

    public static void renderTriangles(List<Mesh> meshes, Color[][] colorBuffer, double[][] depthBuffer,
                                       int width, int height, List<PointLight> pointLights) {
        updateSpatialGridIfSizeChanged(width, height);
        spatialGrid.clear();

        for (Mesh mesh : meshes) {
            for (Triangle triangle : mesh.triangles()) {
                spatialGrid.addTriangle(triangle);
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                List<Triangle> trianglesInCell = spatialGrid.getTriangles(x, y);
                if (trianglesInCell.isEmpty()) continue;

                double centerX = x + 0.5;
                double centerY = y + 0.5;
                double closestDepth = Double.POSITIVE_INFINITY;
                Color closestColor = null;
                Triangle closestTriangle = null;

                for (Triangle triangle : trianglesInCell) {
                    if (!GeometryUtils.isPointInTriangle(centerX, centerY, triangle)) {
                        continue;
                    }

                    double depth = GeometryUtils.calculateDepthAtPoint(centerX, centerY, triangle);
                    if (depth >= closestDepth) {
                        continue;
                    }

                    closestDepth = depth;
                    closestTriangle = triangle;

                    Material material = triangle.getMaterial();
                    closestColor = material.hasTexture() && triangle.hasUV() ?
                            getTextureColor(centerX, centerY, triangle) : material.getColor();
                }

                if (closestColor != null && closestDepth < depthBuffer[x][y]) {
                    Vector3D pixelWorldPos = GeometryUtils.getWorldPositionInTriangle(centerX, centerY,
                            closestTriangle);
                    Color finalColor = LightCalculator.calculatePixelColor(
                            closestColor, closestTriangle, pixelWorldPos, pointLights
                    );

                    depthBuffer[x][y] = closestDepth;
                    colorBuffer[x][y] = finalColor;
                }
            }
        }
    }

    private static void updateSpatialGridIfSizeChanged(int width, int height) {
        if (spatialGrid == null || width != lastWidth || height != lastHeight) {
            spatialGrid = new SpatialGrid(width, height);
            lastWidth = width;
            lastHeight = height;
        }
    }

    private static Color getTextureColor(double x, double y, Triangle triangle) {
        Point2D uv1 = triangle.getUV1();
        Point2D uv2 = triangle.getUV2();
        Point2D uv3 = triangle.getUV3();

        if (uv1 == null || uv2 == null || uv3 == null) {
            return Color.BLACK;
        }

        Vector3D A = triangle.getPoints().get(0);
        Vector3D B = triangle.getPoints().get(1);
        Vector3D C = triangle.getPoints().get(2);

        double x1 = A.getX(), y1 = A.getY();
        double x2 = B.getX(), y2 = B.getY();
        double x3 = C.getX(), y3 = C.getY();

        double denom = (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3);

        if (Math.abs(denom) < 1e-9) {
            return Color.BLACK;
        }

        double alpha = ((y2 - y3) * (x - x3) + (x3 - x2) * (y - y3)) / denom;
        double beta = ((y3 - y1) * (x - x3) + (x1 - x3) * (y - y3)) / denom;
        double gamma = 1 - alpha - beta;

        double invW1 = triangle.getInvW1();
        double invW2 = triangle.getInvW2();
        double invW3 = triangle.getInvW3();

        double interpolatedInvW = alpha * invW1 + beta * invW2 + gamma * invW3;

        if (Math.abs(interpolatedInvW) < 1e-9) {
            return Color.BLACK;
        }

        double uOverW = alpha * (uv1.getX() * invW1) +
                beta * (uv2.getX() * invW2) +
                gamma * (uv3.getX() * invW3);

        double vOverW = alpha * (uv1.getY() * invW1) +
                beta * (uv2.getY() * invW2) +
                gamma * (uv3.getY() * invW3);

        double u = uOverW / interpolatedInvW;
        double v = vOverW / interpolatedInvW;

        u = Math.max(0, Math.min(1, u));
        v = Math.max(0, Math.min(1, v));

        return sampleTexture(triangle.getMaterial().getTexture(), u, v);
    }

    private static Color sampleTexture(Image texture, double u, double v) {
        u = Math.max(0, Math.min(1, u));
        v = Math.max(0, Math.min(1, v));

        int texWidth = texture.getWidth(null);
        int texHeight = texture.getHeight(null);

        int texX = (int)(u * (texWidth - 1));
        int texY = (int)((1 - v) * (texHeight - 1));

        BufferedImage buffered = (java.awt.image.BufferedImage) texture;

        return new Color(buffered.getRGB(texX, texY));
    }
}