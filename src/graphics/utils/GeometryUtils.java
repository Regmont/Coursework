package graphics.utils;

import geometry.*;
import math.Vector3D;

import java.util.List;

public class GeometryUtils {
    public static final double EPSILON = 1e-9;

    public static boolean isPointInTriangle(double px, double py, Triangle triangle) {
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

    public static double calculateDepthAtPoint(double px, double py, Triangle triangle) {
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
}