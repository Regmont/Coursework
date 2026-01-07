package graphics.utils;

import scene.RenderableTriangle;
import core.math.Triangle;
import core.math.Vector3D;
import graphics.TriangleBoundingBox;

import java.util.List;

public class GeometryUtils {
    private static final double EPSILON = 1e-9; //Погрешность вычисления координат

    public static boolean isPointIn3DTriangle(double px, double py, Triangle<Vector3D> triangle) {
        List<Vector3D> points = triangle.getPoints();
        Vector3D p1 = points.get(0);
        Vector3D p2 = points.get(1);
        Vector3D p3 = points.get(2);

        double x1 = p1.getX(), y1 = p1.getY();
        double x2 = p2.getX(), y2 = p2.getY();
        double x3 = p3.getX(), y3 = p3.getY();

        double area1 = (x2 - x1) * (py - y1) - (y2 - y1) * (px - x1);
        double area2 = (x3 - x2) * (py - y2) - (y3 - y2) * (px - x2);
        double area3 = (x1 - x3) * (py - y3) - (y1 - y3) * (px - x3);

        boolean hasPositive = (area1 > EPSILON) || (area2 > EPSILON) || (area3 > EPSILON);
        boolean hasNegative = (area1 < -EPSILON) || (area2 < -EPSILON) || (area3 < -EPSILON);

        return !(hasPositive && hasNegative);
    }

    public static double calculateDepthAtPoint(double px, double py, Triangle<Vector3D> triangle) {
        List<Vector3D> points = triangle.getPoints();
        Vector3D A = points.get(0);
        Vector3D B = points.get(1);
        Vector3D C = points.get(2);

        double x1 = A.getX(), y1 = A.getY(), z1 = A.getZ();
        double x2 = B.getX(), y2 = B.getY(), z2 = B.getZ();
        double x3 = C.getX(), y3 = C.getY(), z3 = C.getZ();

        double d = (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3);

        if (Math.abs(d) < EPSILON) {
            return (z1 + z2 + z3) / 3.0;
        }

        double u = ((y2 - y3) * (px - x3) + (x3 - x2) * (py - y3)) / d;
        double v = ((y3 - y1) * (px - x3) + (x1 - x3) * (py - y3)) / d;
        double w = 1.0 - u - v;

        return u * z1 + v * z2 + w * z3;
    }

    public static Vector3D getWorldPositionInTriangle(double px, double py, RenderableTriangle triangle) {
        List<Vector3D> worldPoints = triangle.getOriginalPoints();

        if (worldPoints == null) {
            return Triangle.getCenter(triangle.getCurrentTriangle());
        }

        List<Vector3D> screenPoints = triangle.getCurrentPoints();

        double x1 = screenPoints.get(0).getX(), y1 = screenPoints.get(0).getY();
        double x2 = screenPoints.get(1).getX(), y2 = screenPoints.get(1).getY();
        double x3 = screenPoints.get(2).getX(), y3 = screenPoints.get(2).getY();

        double denom = (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3);

        if (Math.abs(denom) < EPSILON) {
            return Triangle.getCenter(triangle.getCurrentTriangle());
        }

        double alpha = ((y2 - y3) * (px - x3) + (x3 - x2) * (py - y3)) / denom;
        double beta = ((y3 - y1) * (px - x3) + (x1 - x3) * (py - y3)) / denom;
        double gamma = 1 - alpha - beta;

        List<Double> invWs = triangle.getInvWs();

        double invW1 = invWs.get(0);
        double invW2 = invWs.get(1);
        double invW3 = invWs.get(2);

        double weight1 = alpha * invW1;
        double weight2 = beta * invW2;
        double weight3 = gamma * invW3;
        double totalWeight = weight1 + weight2 + weight3;

        if (Math.abs(totalWeight) < EPSILON) {
            return Triangle.getCenter(triangle.getCurrentTriangle());
        }

        double wx = (alpha * worldPoints.get(0).getX() * invW1 +
                beta * worldPoints.get(1).getX() * invW2 +
                gamma * worldPoints.get(2).getX() * invW3) / totalWeight;

        double wy = (alpha * worldPoints.get(0).getY() * invW1 +
                beta * worldPoints.get(1).getY() * invW2 +
                gamma * worldPoints.get(2).getY() * invW3) / totalWeight;

        double wz = (alpha * worldPoints.get(0).getZ() * invW1 +
                beta * worldPoints.get(1).getZ() * invW2 +
                gamma * worldPoints.get(2).getZ() * invW3) / totalWeight;

        return new Vector3D(wx, wy, wz);
    }
}