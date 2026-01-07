package graphics.utils;

import scene.RenderableTriangle;
import core.math.Triangle;
import core.math.Vector3D;

import java.util.List;

/**
 * Утилиты для геометрических вычислений при растеризации.
 * <p>
 * Содержит методы для проверки принадлежности точки треугольнику, интерполяции глубины и мировых координат.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class GeometryUtils {
    /** Погрешность для сравнений с плавающей точкой. */
    private static final double EPSILON = 1e-9;

    /**
     * Проверяет, находится ли точка внутри треугольника в 2D пространстве.
     * <p>
     * Использует метод площадей (barycentric test). Для точки внутри треугольника
     * все три ориентированные площади имеют одинаковый знак.
     *
     * @param px        X-координата точки
     * @param py        Y-координата точки
     * @param triangle  треугольник в экранных координатах
     * @return {@code true} если точка находится внутри треугольника
     */
    public static boolean isPointIn3DTriangle(double px, double py, Triangle<Vector3D> triangle) {
        List<Vector3D> points = triangle.getPoints();
        Vector3D p1 = points.get(0);
        Vector3D p2 = points.get(1);
        Vector3D p3 = points.get(2);

        double x1 = p1.x(), y1 = p1.y();
        double x2 = p2.x(), y2 = p2.y();
        double x3 = p3.x(), y3 = p3.y();

        double area1 = (x2 - x1) * (py - y1) - (y2 - y1) * (px - x1);
        double area2 = (x3 - x2) * (py - y2) - (y3 - y2) * (px - x2);
        double area3 = (x1 - x3) * (py - y3) - (y1 - y3) * (px - x3);

        boolean hasPositive = (area1 > EPSILON) || (area2 > EPSILON) || (area3 > EPSILON);
        boolean hasNegative = (area1 < -EPSILON) || (area2 < -EPSILON) || (area3 < -EPSILON);

        return !(hasPositive && hasNegative);
    }

    /**
     * Вычисляет глубину (Z-координату) в точке внутри треугольника.
     * <p>
     * Использует барицентрическую интерполяцию по Z-координатам вершин.
     * В случае вырожденного треугольника возвращает среднее значение.
     *
     * @param px        X-координата точки
     * @param py        Y-координата точки
     * @param triangle  треугольник в экранных координатах
     * @return интерполированное значение глубины
     */
    public static double calculateDepthAtPoint(double px, double py, Triangle<Vector3D> triangle) {
        List<Vector3D> points = triangle.getPoints();
        Vector3D A = points.get(0);
        Vector3D B = points.get(1);
        Vector3D C = points.get(2);

        double x1 = A.x(), y1 = A.y(), z1 = A.z();
        double x2 = B.x(), y2 = B.y(), z2 = B.z();
        double x3 = C.x(), y3 = C.y(), z3 = C.z();

        double d = (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3);

        if (Math.abs(d) < EPSILON) {
            return (z1 + z2 + z3) / 3.0;
        }

        double u = ((y2 - y3) * (px - x3) + (x3 - x2) * (py - y3)) / d;
        double v = ((y3 - y1) * (px - x3) + (x1 - x3) * (py - y3)) / d;
        double w = 1.0 - u - v;

        return u * z1 + v * z2 + w * z3;
    }

    /**
     * Восстанавливает мировые координаты точки внутри треугольника.
     * <p>
     * Использует перспективно-корректную барицентрическую интерполяцию
     * с учетом обратных значений W-координаты (1/z).
     * В случае вырожденного треугольника возвращает центр треугольника.
     *
     * @param px        X-координата точки в экранных координатах
     * @param py        Y-координата точки в экранных координатах
     * @param triangle  треугольник с информацией о мировых координатах и invW
     * @return восстановленная позиция в мировых координатах
     */
    public static Vector3D getWorldPositionInTriangle(double px, double py, RenderableTriangle triangle) {
        List<Vector3D> worldPoints = triangle.getOriginalPoints();

        if (worldPoints == null) {
            return Triangle.getCenter(triangle.getCurrentTriangle());
        }

        List<Vector3D> screenPoints = triangle.getCurrentPoints();

        double x1 = screenPoints.get(0).x(), y1 = screenPoints.get(0).y();
        double x2 = screenPoints.get(1).x(), y2 = screenPoints.get(1).y();
        double x3 = screenPoints.get(2).x(), y3 = screenPoints.get(2).y();

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

        double wx = (alpha * worldPoints.get(0).x() * invW1 +
                beta * worldPoints.get(1).x() * invW2 +
                gamma * worldPoints.get(2).x() * invW3) / totalWeight;

        double wy = (alpha * worldPoints.get(0).y() * invW1 +
                beta * worldPoints.get(1).y() * invW2 +
                gamma * worldPoints.get(2).y() * invW3) / totalWeight;

        double wz = (alpha * worldPoints.get(0).z() * invW1 +
                beta * worldPoints.get(1).z() * invW2 +
                gamma * worldPoints.get(2).z() * invW3) / totalWeight;

        return new Vector3D(wx, wy, wz);
    }
}
