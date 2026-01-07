package graphics;

import core.math.Triangle;
import core.math.Vector3D;

import java.util.List;

/**
 * Ограничивающий прямоугольник (bounding box) для треугольника в экранных координатах.
 * <p>
 * Используется в растеризации для определения области экрана, которую занимает треугольник.
 * Координаты представляют границы прямоугольника в пикселях (целые числа).
 *
 * @param minX минимальная X координата (левый край, включительно)
 * @param maxX максимальная X координата (правый край, включительно)
 * @param minY минимальная Y координата (верхний край, включительно)
 * @param maxY максимальная Y координата (нижний край, включительно)
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public record TriangleBoundingBox(int minX, int maxX, int minY, int maxY) {
    /**
     * Ограничивает bounding box размерами экрана.
     * <p>
     * Гарантирует, что координаты не выходят за пределы [0, width-1] и [0, height-1].
     * Используется перед растеризацией для предотвращения доступа к несуществующим пикселям.
     *
     * @param triangleBoundingBox   исходный bounding box
     * @param width                 ширина области отрисовки в пикселях
     * @param height                высота области отрисовки в пикселях
     * @return новый bounding box, обрезанный по границам экрана
     */
    public static TriangleBoundingBox clampToScreen(TriangleBoundingBox triangleBoundingBox, int width, int height) {
        int minX = Math.max(0, triangleBoundingBox.minX());
        int maxX = Math.min(width - 1, triangleBoundingBox.maxX());
        int minY = Math.max(0, triangleBoundingBox.minY());
        int maxY = Math.min(height - 1, triangleBoundingBox.maxY());

        return new TriangleBoundingBox(minX, maxX, minY, maxY);
    }

    /**
     * Вычисляет bounding box для треугольника в экранных координатах.
     * <p>
     * Координаты вершин должны быть уже спроецированы на экран (преобразованы в пиксели).
     * Использует {@link Math#floor(double)} и {@link Math#ceil(double)} для гарантии,
     * что весь треугольник будет покрыт bounding box.
     *
     * @param triangle треугольник в экранных координатах (после projection/viewport преобразований)
     * @return bounding box, содержащий все вершины треугольника
     */
    public static TriangleBoundingBox getBoundingBox(Triangle<Vector3D> triangle) {
        List<Vector3D> points = triangle.getPoints();

        double[] xs = {points.get(0).x(), points.get(1).x(), points.get(2).x()};
        double[] ys = {points.get(0).y(), points.get(1).y(), points.get(2).y()};

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
