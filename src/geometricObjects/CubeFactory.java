package geometricObjects;

import math.Point3D;
import java.awt.Color;

public class CubeFactory {

    public static Mesh createCube(double size,
                                  Color frontColor, Color backColor,
                                  Color leftColor, Color rightColor,
                                  Color topColor, Color bottomColor) {
        Point3D[] vertices = getPoints(size);

        Triangle[] triangles = new Triangle[12];

        // Передняя грань (front - z = -half)
        triangles[0] = new Triangle(vertices[0], vertices[2], vertices[1], frontColor);
        triangles[1] = new Triangle(vertices[1], vertices[2], vertices[3], frontColor);

        // Задняя грань (back - z = half)
        triangles[2] = new Triangle(vertices[5], vertices[7], vertices[4], backColor);
        triangles[3] = new Triangle(vertices[4], vertices[7], vertices[6], backColor);

        // Левая грань (left - x = -half)
        triangles[4] = new Triangle(vertices[4], vertices[6], vertices[0], leftColor);
        triangles[5] = new Triangle(vertices[0], vertices[6], vertices[2], leftColor);

        // Правая грань (right - x = half)
        triangles[6] = new Triangle(vertices[1], vertices[3], vertices[5], rightColor);
        triangles[7] = new Triangle(vertices[5], vertices[3], vertices[7], rightColor);

        // Верхняя грань (top - y = half)
        triangles[8] = new Triangle(vertices[2], vertices[6], vertices[3], topColor);
        triangles[9] = new Triangle(vertices[3], vertices[6], vertices[7], topColor);

        // Нижняя грань (bottom - y = -half)
        triangles[10] = new Triangle(vertices[0], vertices[1], vertices[4], bottomColor);
        triangles[11] = new Triangle(vertices[1], vertices[5], vertices[4], bottomColor);

        return new Mesh(1f, triangles);
    }

    private static Point3D[] getPoints(double size) {
        double half = size / 2.0;

        return new Point3D[]{
                new Point3D(-half, -half, -half), // 0: лево-низ-перед
                new Point3D(half, -half, -half),  // 1: право-низ-перед
                new Point3D(-half, half, -half),  // 2: лево-верх-перед
                new Point3D(half, half, -half),   // 3: право-верх-перед
                new Point3D(-half, -half, half),  // 4: лево-низ-зад
                new Point3D(half, -half, half),   // 5: право-низ-зад
                new Point3D(-half, half, half),   // 6: лево-верх-зад
                new Point3D(half, half, half)     // 7: право-верх-зад
        };
    }
}