package core.math;

import java.util.ArrayList;
import java.util.List;

/**
 * Класс для представления треугольника в трехмерном пространстве.
 * <p>
 * Треугольник определяется тремя точками произвольного типа {@code T}.
 * Класс является неизменяемым (immutable) после создания.
 * Предоставляет утилитарные методы для работы с треугольниками из {@link Vector3D}.
 *
 * @param <T> тип точек треугольника (например, {@link Vector3D})
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class Triangle<T> {
    private final T point1;
    private final T point2;
    private final T point3;

    /**
     * Создает треугольник по трем точкам.
     *
     * @param point1 первая вершина треугольника
     * @param point2 вторая вершина треугольника
     * @param point3 третья вершина треугольника
     */
    public Triangle(T point1, T point2, T point3) {
        this.point1 = point1;
        this.point2 = point2;
        this.point3 = point3;
    }

    /**
     * Создает треугольник из списка точек.
     * <p>
     * Список должен содержать минимум 3 точки.
     * Используются первые три элемента списка.
     *
     * @param points список точек треугольника
     * @throws IndexOutOfBoundsException если список содержит меньше 3 элементов
     * @throws NullPointerException если {@code points} равен {@code null}
     */
    public Triangle(List<T> points) {
        this.point1 = points.get(0);
        this.point2 = points.get(1);
        this.point3 = points.get(2);
    }

    /**
     * Возвращает список вершин треугольника.
     * <p>
     * Порядок точек в списке соответствует порядку в конструкторе.
     *
     * @return новый список, содержащий копии вершин треугольника
     */
    public List<T> getPoints() {
        List<T> points = new ArrayList<>();

        points.add(point1);
        points.add(point2);
        points.add(point3);

        return points;
    }

    /**
     * Вычисляет нормаль треугольника в трехмерном пространстве.
     *
     * @param p1 первая вершина треугольника
     * @param p2 вторая вершина треугольника
     * @param p3 третья вершина треугольника
     * @return единичный вектор нормали к плоскости треугольника
     * @throws NullPointerException если любой параметр равен {@code null}
     * @see Vector3D#cross(Vector3D)
     * @see Vector3D#normalize()
     */
    public static Vector3D calculateNormal(Vector3D p1, Vector3D p2, Vector3D p3) {
        Vector3D v1 = p2.subtract(p1);
        Vector3D v2 = p3.subtract(p1);

        return v1.cross(v2).normalize();
    }

    /**
     * Вычисляет центр треугольника в трехмерном пространстве.
     *
     * @param triangle треугольник для которого вычисляется центр
     * @return радиус-вектор центра треугольника
     */
    public static Vector3D getCenter(Triangle<Vector3D> triangle) {
        List<Vector3D> points = triangle.getPoints();

        Vector3D p1 = points.get(0);
        Vector3D p2 = points.get(1);
        Vector3D p3 = points.get(2);

        return new Vector3D(
                (p1.x() + p2.x() + p3.x()) / 3.0,
                (p1.y() + p2.y() + p3.y()) / 3.0,
                (p1.z() + p2.z() + p3.z()) / 3.0
        );
    }

    @Override
    public String toString() {
        return String.format("Triangle[%s, %s, %s]", point1, point2, point3);
    }
}
