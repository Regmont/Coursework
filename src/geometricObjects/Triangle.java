package geometricObjects;

import math.Point3D;
import rendering.Material;

import java.awt.*;
import java.awt.geom.Point2D;
import java.util.ArrayList;

public class Triangle {
    private final Point3D point1;
    private final Point3D point2;
    private final Point3D point3;
    private final Material material;
    private final Point2D uv1;
    private final Point2D uv2;
    private final Point2D uv3;
    private Point3D normal;
    private final Point3D[] originalPoints;

    private BoundingBox boundingBox = null;
    private boolean boundingBoxDirty = true;

    public Triangle(Point3D point1, Point3D point2, Point3D point3,
                    Material material, Point3D[] originalPoints,
                    Point2D uv1, Point2D uv2, Point2D uv3) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
        this.material = material;
        this.originalPoints = originalPoints;
        this.uv1 = uv1;
        this.uv2 = uv2;
        this.uv3 = uv3;
        this.normal = null;
    }

    public Material getMaterial() {
        return material;
    }

    public ArrayList<Point3D> getPoints() {
        ArrayList<Point3D> points = new ArrayList<>();
        points.add(point1);
        points.add(point2);
        points.add(point3);
        return points;
    }

    public Point2D getUV1() { return uv1; }
    public Point2D getUV2() { return uv2; }
    public Point2D getUV3() { return uv3; }
    public boolean hasUV() { return uv1 != null && uv2 != null && uv3 != null; }

    public BoundingBox getBoundingBox() {
        if (boundingBox != null && !boundingBoxDirty) {
            return boundingBox;
        }

        double[] xs = {point1.getX(), point2.getX(), point3.getX()};
        double[] ys = {point1.getY(), point2.getY(), point3.getY()};

        double minX = Math.min(xs[0], Math.min(xs[1], xs[2]));
        double maxX = Math.max(xs[0], Math.max(xs[1], xs[2]));
        double minY = Math.min(ys[0], Math.min(ys[1], ys[2]));
        double maxY = Math.max(ys[0], Math.max(ys[1], ys[2]));

        boundingBox = new BoundingBox(
                (int)Math.floor(minX), (int)Math.ceil(maxX),
                (int)Math.floor(minY), (int)Math.ceil(maxY)
        );
        boundingBoxDirty = false;

        return boundingBox;
    }

    public Point3D calculateNormal() {
        if (normal != null) {
            return new Point3D(normal);
        }

        double v1x = point2.getX() - point1.getX();
        double v1y = point2.getY() - point1.getY();
        double v1z = point2.getZ() - point1.getZ();

        double v2x = point3.getX() - point1.getX();
        double v2y = point3.getY() - point1.getY();
        double v2z = point3.getZ() - point1.getZ();

        double nx = v1y * v2z - v1z * v2y;
        double ny = v1z * v2x - v1x * v2z;
        double nz = v1x * v2y - v1y * v2x;

        double length = Math.sqrt(nx * nx + ny * ny + nz * nz);
        if (length > 0) {
            nx /= length;
            ny /= length;
            nz /= length;
        }

        normal = new Point3D(nx, ny, nz);

        return new Point3D(normal);
    }

    public boolean isVisibleFromCameraCenter() {
        return calculateNormal().getZ() < 0;
    }

    public Point3D[] getOriginalPoints() {
        return originalPoints;
    }

    public boolean hasOriginalPoints() {
        return originalPoints != null;
    }

    public Color getColor() {
        return material.getColor();
    }

    @Override
    public String toString() {
        return point1 + " " + point2 + " " + point3;
    }
}