package scene;

import core.math.Triangle;
import core.math.Vector3D;

import java.awt.geom.Point2D;
import java.util.List;

/**
 * Класс, представляющий треугольник для рендеринга с дополнительной информацией.
 * <p>
 * Содержит геометрические данные треугольника, материал, UV-координаты для текстурирования и предвычисленные нормали.
 * Используется в графическом конвейере рендеринга.
 * <p>
 * Хранит два набора координат:
 * <ul>
 *   <li>{@code originalPoints} - исходные координаты в мировом пространстве</li>
 *   <li>{@code currentPoints} - текущие координаты после трансформаций через камеру</li>
 * </ul>
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class RenderableTriangle {
    private final Triangle<Vector3D> originalPoints;
    private final Triangle<Vector3D> currentPoints;
    private Material material;
    private final Triangle<Point2D> uvs;
    private final Triangle<Double> invWs;
    private Vector3D cameraNormal;
    private Vector3D worldNormal;

    /**
     * Основной конструктор с полным набором параметров.
     *
     * @param originalPoints    исходные вершины треугольника в мировых координатах (3 точки)
     * @param currentPoints     текущие вершины треугольника (после трансформаций) (3 точки)
     * @param material          материал треугольника
     * @param uvs               UV-координаты для текстурирования (3 точки)
     * @param invWs             обратные значения W-координаты для перспективной коррекции (3 значения)
     * @throws IllegalArgumentException если любой список содержит не 3 элемента
     */
    public RenderableTriangle(List<Vector3D> originalPoints, List<Vector3D> currentPoints, Material material,
                              List<Point2D> uvs, List<Double> invWs) {
        this.originalPoints = new Triangle<>(originalPoints);
        this.currentPoints = new Triangle<>(currentPoints);
        this.material = material;
        this.uvs = new Triangle<>(uvs);
        this.invWs = new Triangle<>(invWs);
        this.cameraNormal = null;
        this.worldNormal = calculateWorldNormal();
    }

    /**
     * Упрощенный конструктор для треугольника без материала и инверсных W-координат.
     * <p>
     * Использует {@link Material#DEFAULT_MATERIAL} и единичные значения для invWs.
     *
     * @param originalPoints исходные вершины треугольника (3 точки)
     * @param uvs            UV-координаты для текстурирования (3 точки)
     */
    public RenderableTriangle(List<Vector3D> originalPoints, List<Point2D> uvs) {
        this.originalPoints = new Triangle<>(originalPoints);
        this.currentPoints = new Triangle<>(originalPoints);
        this.material = Material.DEFAULT_MATERIAL;
        this.uvs = new Triangle<>(uvs);
        this.invWs = new Triangle<>(1d, 1d, 1d);
    }

    public List<Vector3D> getOriginalPoints() {
        return originalPoints.getPoints();
    }
    public List<Vector3D> getCurrentPoints() {
        return currentPoints.getPoints();
    }

    public Triangle<Vector3D> getOriginalTriangle() {
        return originalPoints;
    }
    public Triangle<Vector3D> getCurrentTriangle() {
        return currentPoints;
    }

    /**
     * Проверяет, есть ли у треугольника UV-координаты.
     * <p>
     * UV-координаты используются для наложения текстур.
     *
     * @return {@code true} если UV-координаты заданы (ровно 3 точки)
     */
    public boolean hasUV() {
        return uvs.getPoints().size() == 3;
    }

    public List<Point2D> getUVs() {
        return uvs.getPoints();
    }
    public List<Double> getInvWs() {
        return invWs.getPoints();
    }
    public Material getMaterial() {
        return material;
    }
    public Vector3D getWorldNormal() {
        return worldNormal;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    /**
     * Проверяет, виден ли треугольник из центра камеры.
     * <p>
     * Треугольник считается видимым, если его нормаль в пространстве камеры
     * направлена в сторону камеры (компонента Z отрицательна).
     *
     * @return {@code true} если треугольник обращен к камере
     */
    public boolean isVisibleFromCameraCenter() {
        return calculateCameraNormal().z() < 0;
    }

    /**
     * Вычисляет нормаль треугольника в мировых координатах.
     * <p>
     * Использует статический метод {@link Triangle#calculateNormal(Vector3D, Vector3D, Vector3D)}.
     *
     * @return нормаль треугольника по исходным вершинам
     * @see Triangle#calculateNormal(Vector3D, Vector3D, Vector3D)
     */
    private Vector3D calculateWorldNormal() {
        List<Vector3D> points = originalPoints.getPoints();

        return Triangle.calculateNormal(points.get(0), points.get(1), points.get(2));
    }

    /**
     * Вычисляет нормаль треугольника в пространстве камеры.
     * <p>
     * Результат кэшируется для повторного использования.
     * Использует метод {@link Triangle#calculateNormal(Vector3D, Vector3D, Vector3D)}.
     *
     * @return нормаль треугольника в координатах камеры
     * @see Triangle#calculateNormal(Vector3D, Vector3D, Vector3D)
     */
    private Vector3D calculateCameraNormal() {
        if (cameraNormal != null) {
            return cameraNormal;
        }

        List<Vector3D> points = currentPoints.getPoints();
        cameraNormal = Triangle.calculateNormal(points.get(0), points.get(1), points.get(2));

        return cameraNormal;
    }

    @Override
    public String toString() {
        return String.format(
                "RenderableTriangle[\n" +
                        "  original=%s,\n" +
                        "  current=%s,\n" +
                        "  material=%s,\n" +
                        "  hasUV=%b,\n" +
                        "  worldNormal=%s\n" +
                        "]",
                originalPoints,
                currentPoints,
                material,
                hasUV(),
                worldNormal
        );
    }
}
