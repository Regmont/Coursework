package graphics.renderer;

import geometry.*;
import graphics.light.AmbienceLight;
import math.Vector3D;
import graphics.utils.GeometryUtils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.List;

public class TriangleRenderer {
    public static AmbienceLight ambienceLight = new AmbienceLight(0.5, Color.WHITE);

    public static void renderTriangles(List<Mesh> meshes, Color[][] colorBuffer, double[][] depthBuffer,
                                       int width, int height) {
        for (Mesh mesh : meshes) {
            for (Triangle triangle : mesh.triangles()) {
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

                        if (!GeometryUtils.isPointInTriangle(centerX, centerY, triangle)) {
                            continue;
                        }

                        double depth = GeometryUtils.calculateDepthAtPoint(centerX, centerY, triangle);

                        if (depth < depthBuffer[x][y]) {
                            depthBuffer[x][y] = depth;

                            Material material = triangle.getMaterial();
                            Color pixelColor;

                            if (material.hasTexture() && triangle.hasUV()) {
                                pixelColor = getTextureColor(centerX, centerY, triangle);
                            } else {
                                pixelColor = material.getColor();
                            }

                            pixelColor = ambienceLight.applyAmbienceLightToTriangles(pixelColor);

                            colorBuffer[x][y] = pixelColor;
                        }
                    }
                }
            }
        }
    }

    private static Color getTextureColor(double x, double y, Triangle triangle) {
        Point2D uv1 = triangle.getUV1();
        Point2D uv2 = triangle.getUV2();
        Point2D uv3 = triangle.getUV3();

        Vector3D A = triangle.getPoints().get(0);
        Vector3D B = triangle.getPoints().get(1);
        Vector3D C = triangle.getPoints().get(2);

        double z1 = A.getZ();
        double z2 = B.getZ();
        double z3 = C.getZ();

        double denom = (B.getY() - C.getY()) * (A.getX() - C.getX()) + (C.getX() - B.getX()) * (A.getY() - C.getY());

        double alpha = ((B.getY() - C.getY()) * (x - C.getX()) + (C.getX() - B.getX()) * (y - C.getY())) / denom;
        double beta = ((C.getY() - A.getY()) * (x - C.getX()) + (A.getX() - C.getX()) * (y - C.getY())) / denom;
        double gamma = 1 - alpha - beta;

        double interpolatedInvZ = alpha * (1.0/z1) + beta * (1.0/z2) + gamma * (1.0/z3);

        double uOverZ = alpha * (uv1.getX() / z1) + beta * (uv2.getX() / z2) + gamma * (uv3.getX() / z3);
        double vOverZ = alpha * (uv1.getY() / z1) + beta * (uv2.getY() / z2) + gamma * (uv3.getY() / z3);

        double u = uOverZ / interpolatedInvZ;
        double v = vOverZ / interpolatedInvZ;

        return sampleTexture(triangle.getMaterial().getTexture(), u, v);
    }

    private static Color sampleTexture(Image texture, double u, double v) {
        u = Math.max(0, Math.min(1, u));
        v = Math.max(0, Math.min(1, v));

        int texWidth = texture.getWidth(null);
        int texHeight = texture.getHeight(null);

        int texX = (int)(u * (texWidth - 1));
        int texY = (int)((1 - v) * (texHeight - 1));

        java.awt.image.BufferedImage buffered = (java.awt.image.BufferedImage) texture;

        return new Color(buffered.getRGB(texX, texY));
    }
}