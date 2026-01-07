package graphics;

import core.math.Triangle;
import core.math.Vector3D;

import java.util.List;

public record TriangleBoundingBox(int minX, int maxX, int minY, int maxY) {
    public static TriangleBoundingBox clampToScreen(TriangleBoundingBox triangleBoundingBox, int width, int height) {
        int minX = Math.max(0, triangleBoundingBox.minX());
        int maxX = Math.min(width - 1, triangleBoundingBox.maxX());
        int minY = Math.max(0, triangleBoundingBox.minY());
        int maxY = Math.min(height - 1, triangleBoundingBox.maxY());

        return new TriangleBoundingBox(minX, maxX, minY, maxY);
    }

    public static TriangleBoundingBox getBoundingBox(Triangle<Vector3D> triangle) {
        List<Vector3D> points = triangle.getPoints();

        double[] xs = {points.get(0).getX(), points.get(1).getX(), points.get(2).getX()};
        double[] ys = {points.get(0).getY(), points.get(1).getY(), points.get(2).getY()};

        double minX = Math.min(xs[0], Math.min(xs[1], xs[2]));
        double maxX = Math.max(xs[0], Math.max(xs[1], xs[2]));
        double minY = Math.min(ys[0], Math.min(ys[1], ys[2]));
        double maxY = Math.max(ys[0], Math.max(ys[1], ys[2]));

        return new TriangleBoundingBox(
                (int)Math.floor(minX), (int)Math.ceil(maxX),
                (int)Math.floor(minY), (int)Math.ceil(maxY)
        );
    }
}
