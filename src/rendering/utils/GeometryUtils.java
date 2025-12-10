package rendering.utils;

import geometricObjects.*;
import math.Point3D;

import java.awt.geom.Point2D;
import java.util.ArrayList;

public class GeometryUtils {
    public static final double EPSILON_POINT_IN_TRIANGLE = 1e-9;
    public static final double EPSILON_DISTANCE_COMPARE = 1e-10;
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

    public double distanceToClosestEdge(double pointX, double pointY, Triangle triangle) {
        ArrayList<Point3D> points = triangle.getPoints();

        Point3D p1 = points.get(0);
        Point3D p2 = points.get(1);
        Point3D p3 = points.get(2);

        double dist1 = distanceToSegment(pointX, pointY, p1, p2);
        double dist2 = distanceToSegment(pointX, pointY, p2, p3);
        double dist3 = distanceToSegment(pointX, pointY, p3, p1);

        return Math.min(Math.min(dist1, dist2), dist3);
    }

    public double distanceToSegment(double x0, double y0, Point3D p1, Point3D p2) {
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();

        if (x1 == x2 && y1 == y2) {
            return Math.sqrt((x0 - x1) * (x0 - x1) + (y0 - y1) * (y0 - y1));
        }

        double A = y2 - y1;
        double B = x1 - x2;
        double C = x2 * y1 - x1 * y2;

        double numerator = Math.abs(A * x0 + B * y0 + C);
        double denominator = Math.sqrt(A * A + B * B);
        double distanceToLine = numerator / denominator;

        double dot1 = (x0 - x1) * (x2 - x1) + (y0 - y1) * (y2 - y1);
        double dot2 = (x0 - x2) * (x1 - x2) + (y0 - y2) * (y1 - y2);

        if (dot1 <= 0) {
            return Math.sqrt(Math.pow(x0 - x1, 2) + Math.pow(y0 - y1, 2));
        }
        if (dot2 <= 0) {
            return Math.sqrt((x0 - x2) * (x0 - x2) + (y0 - y2) * (y0 - y2));
        }

        return distanceToLine;
    }

    public Point2D projectToClosestEdge(double pointX, double pointY, Triangle triangle) {
        ArrayList<Point3D> points = triangle.getPoints();

        Point3D A = points.get(0);
        Point3D B = points.get(1);
        Point3D C = points.get(2);

        double distAB = distanceToSegment(pointX, pointY, A, B);
        double distBC = distanceToSegment(pointX, pointY, B, C);
        double distCA = distanceToSegment(pointX, pointY, C, A);

        double minDist = Math.min(Math.min(distAB, distBC), distCA);

        if (Math.abs(distAB - minDist) < EPSILON_DISTANCE_COMPARE) {
            return projectToSegment(pointX, pointY, A, B);
        } else if (Math.abs(distBC - minDist) < EPSILON_DISTANCE_COMPARE) {
            return projectToSegment(pointX, pointY, B, C);
        } else {
            return projectToSegment(pointX, pointY, C, A);
        }
    }

    public Point2D projectToSegment(double x0, double y0, Point3D p1, Point3D p2) {
        double x1 = p1.getX();
        double y1 = p1.getY();
        double x2 = p2.getX();
        double y2 = p2.getY();

        double dx = x2 - x1;
        double dy = y2 - y1;

        if (dx == 0 && dy == 0) {
            return new Point2D.Double(x1, y1);
        }

        double t = ((x0 - x1) * dx + (y0 - y1) * dy) / (dx * dx + dy * dy);

        t = Math.max(0, Math.min(1, t));

        double projX = x1 + t * dx;
        double projY = y1 + t * dy;

        return new Point2D.Double(projX, projY);
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

    public int performSupersampling(double pointX, double pointY, Triangle triangle,
                                    int xPointsNumber, int yPointsNumber) {
        if (xPointsNumber <= 0 || yPointsNumber <= 0) {
            return 0;
        }

        int pointsInside = 0;

        double subPixelWidth = 1.0 / xPointsNumber;
        double subPixelHeight = 1.0 / yPointsNumber;

        for (int i = 0; i < xPointsNumber; i++) {
            for (int j = 0; j < yPointsNumber; j++) {
                double subX = pointX + (i + 0.5) * subPixelWidth;
                double subY = pointY + (j + 0.5) * subPixelHeight;

                if (isPointInTriangle(subX, subY, triangle)) {
                    pointsInside++;
                }
            }
        }

        return pointsInside;
    }

    public BoundingBox getEdgeBoundingBox(Edge edge, int width, int height) {
        Point3D p1 = edge.getP1();
        Point3D p2 = edge.getP2();

        int minX = Math.max(0, (int) Math.floor(Math.min(p1.getX(), p2.getX())));
        int maxX = Math.min(width - 1, (int) Math.ceil(Math.max(p1.getX(), p2.getX())));
        int minY = Math.max(0, (int) Math.floor(Math.min(p1.getY(), p2.getY())));
        int maxY = Math.min(height - 1, (int) Math.ceil(Math.max(p1.getY(), p2.getY())));

        if (minX > maxX || minY > maxY) {
            return null;
        }

        return new BoundingBox(minX, maxX, minY, maxY);
    }
}