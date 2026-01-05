package core.math;

import java.util.ArrayList;
import java.util.List;

public class Triangle<T> {
    private final T point1;
    private final T point2;
    private final T point3;

    public Triangle(T point1, T point2, T point3) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
    }

    public Triangle(List<T> points) {
        this.point1 = points.get(0);
        this.point2 = points.get(1);
        this.point3 = points.get(2);
    }

    public List<T> getPoints() {
        List<T> points = new ArrayList<>();

        points.add(point1);
        points.add(point2);
        points.add(point3);

        return points;
    }

    public T getPoint(int index) {
        return switch (index) {
            case 0 -> point1;
            case 1 -> point2;
            case 2 -> point3;
            default -> null;
        };
    }

    public static Vector3D calculateNormal(Vector3D p1, Vector3D p2, Vector3D p3) {
        double v1x = p2.getX() - p1.getX();
        double v1y = p2.getY() - p1.getY();
        double v1z = p2.getZ() - p1.getZ();

        double v2x = p3.getX() - p1.getX();
        double v2y = p3.getY() - p1.getY();
        double v2z = p3.getZ() - p1.getZ();

        double nx = v1y * v2z - v1z * v2y;
        double ny = v1z * v2x - v1x * v2z;
        double nz = v1x * v2y - v1y * v2x;

        double length = Math.sqrt(nx * nx + ny * ny + nz * nz);

        if (length > 0) {
            nx /= length;
            ny /= length;
            nz /= length;
        }

        return new Vector3D(nx, ny, nz);
    }

    public static Vector3D getTriangleCenter(Triangle<Vector3D> triangle) {
        Vector3D p1 = triangle.getPoint(0);
        Vector3D p2 = triangle.getPoint(1);
        Vector3D p3 = triangle.getPoint(2);

        return new Vector3D(
                (p1.getX() + p2.getX() + p3.getX()) / 3.0,
                (p1.getY() + p2.getY() + p3.getY()) / 3.0,
                (p1.getZ() + p2.getZ() + p3.getZ()) / 3.0
        );
    }
}
