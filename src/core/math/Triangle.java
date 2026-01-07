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

    public static Vector3D calculateNormal(Vector3D p1, Vector3D p2, Vector3D p3) {
        Vector3D v1 = p2.subtract(p1);
        Vector3D v2 = p3.subtract(p1);

        return v1.cross(v2).normalize();
    }

    public static Vector3D getCenter(Triangle<Vector3D> triangle) {
        List<Vector3D> points = triangle.getPoints();

        Vector3D p1 = points.get(0);
        Vector3D p2 = points.get(1);
        Vector3D p3 = points.get(2);

        return new Vector3D(
                (p1.getX() + p2.getX() + p3.getX()) / 3.0,
                (p1.getY() + p2.getY() + p3.getY()) / 3.0,
                (p1.getZ() + p2.getZ() + p3.getZ()) / 3.0
        );
    }
}
