package rendering.utils;

import geometricObjects.*;
import math.Point3D;

import java.util.ArrayList;

public class GeometryUtils {
    public static final double EPSILON_POINT_IN_TRIANGLE = 1e-9;
    public static final double EPSILON_AREA = 1e-10;

    public boolean isPointInTriangle(double pointX, double pointY, Triangle triangle) {
        ArrayList<Point3D> points = triangle.getPoints();

        Point3D p1 = points.get(0);
        Point3D p2 = points.get(1);
        Point3D p3 = points.get(2);

        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();
        double x3 = p3.getX();
        double y3 = p3.getY();

        double cross1 = (x2 - x1) * (pointY - y1) - (y2 - y1) * (pointX - x1);
        double cross2 = (x3 - x2) * (pointY - y2) - (y3 - y2) * (pointX - x2);
        double cross3 = (x1 - x3) * (pointY - y3) - (y1 - y3) * (pointX - x3);

        return (cross1 >= -EPSILON_POINT_IN_TRIANGLE && cross2 >= -EPSILON_POINT_IN_TRIANGLE &&
                cross3 >= -EPSILON_POINT_IN_TRIANGLE) || (cross1 <= EPSILON_POINT_IN_TRIANGLE &&
                cross2 <= EPSILON_POINT_IN_TRIANGLE && cross3 <= EPSILON_POINT_IN_TRIANGLE);
    }

    public double calculateDepthAtPoint(double pointX, double pointY, Triangle triangle) {
        ArrayList<Point3D> points = triangle.getPoints();

        Point3D A = points.get(0);
        Point3D B = points.get(1);
        Point3D C = points.get(2);

        double x1 = A.getX(), y1 = A.getY(), z1 = A.getZ();
        double x2 = B.getX(), y2 = B.getY(), z2 = B.getZ();
        double x3 = C.getX(), y3 = C.getY(), z3 = C.getZ();

        double areaABC = Math.abs((x2 - x1) * (y3 - y1) - (x3 - x1) * (y2 - y1)) / 2.0;

        if (areaABC < EPSILON_AREA) {
            return (z1 + z2 + z3) / 3.0;
        }

        double areaPBC = Math.abs((x2 - pointX) * (y3 - pointY) - (x3 - pointX) * (y2 - pointY)) / 2.0;
        double areaAPC = Math.abs((pointX - x1) * (y3 - y1) - (x3 - x1) * (pointY - y1)) / 2.0;
        double areaABP = Math.abs((x2 - x1) * (pointY - y1) - (pointX - x1) * (y2 - y1)) / 2.0;

        double u = areaPBC / areaABC;
        double v = areaAPC / areaABC;
        double w = areaABP / areaABC;

        return u * z1 + v * z2 + w * z3;
    }
}