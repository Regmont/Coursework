package scene;

import java.util.*;

/**
 * Модель (сетка) состоящая из треугольников для рендеринга.
 * <p>
 * Представляет собой коллекцию {@link RenderableTriangle} с возможностью управления материалами и создания копий.
 * <p>
 * Использует record для автоматической реализации equals, hashCode, toString и неизменяемости списка треугольников.
 *
 * @param triangles список треугольников, составляющих модель
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public record Mesh(List<RenderableTriangle> triangles) {
    /**
     * Устанавливает одинаковый материал для всех треугольников модели.
     * <p>
     * Изменяет состояние существующих объектов треугольников.
     *
     * @param material материал для установки
     */
    public void setMaterial(Material material) {
        for (RenderableTriangle triangle : triangles) {
            triangle.setMaterial(material);
        }
    }

    /**
     * Создает глубокую копию модели.
     * <p>
     * Создает новые объекты для всех треугольников с копированием исходных данных,
     * но без копирования текущих координат (используются оригинальные).
     *
     * @return новая независимая копия модели
     */
    public Mesh getCopy() {
        List<RenderableTriangle> newTriangles = new ArrayList<>();

        for (RenderableTriangle triangle : triangles) {
            RenderableTriangle newTriangle = new RenderableTriangle(
                    triangle.getOriginalPoints(),
                    triangle.getUVs()
            );

            newTriangle.setMaterial(triangle.getMaterial());

            newTriangles.add(newTriangle);
        }

        return new Mesh(newTriangles);
    }

    @Override
    public String toString() {
        return "Mesh[" + triangles.size() + " triangles]";
    }
}
