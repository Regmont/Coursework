package core.base;

import core.math.Vector3D;

/**
 * Абстрактный базовый класс для всех объектов с трансформацией в 3D сцене.
 * <p>
 * Предоставляет общую функциональность для работы с положением, вращением и масштабом
 * через объект {@link Transform}. Все наследующие классы автоматически получают
 * возможность управления своей трансформацией в пространстве.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public abstract class ObjectInstance {
    private final Transform transform;

    /**
     * Создает экземпляр объекта с указанной трансформацией.
     *
     * @param position начальное положение объекта в мировых координатах
     * @param rotation начальное вращение объекта (углы Эйлера в радианах)
     * @param scale    начальный масштаб объекта по осям XYZ
     */
    public ObjectInstance(Vector3D position, Vector3D rotation, Vector3D scale) {
        transform = new Transform(position, rotation, scale);
    }

    public Transform getTransform() {
        return transform;
    }

    @Override
    public String toString() {
        return String.format(
                "%s[transform=%s]",
                getClass().getSimpleName(),
                transform
        );
    }
}
