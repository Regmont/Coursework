package scene.gameObjects;

import core.base.ObjectInstance;
import core.math.Vector3D;
import scene.Mesh;
import scene.Object3D;

/**
 * Простой 3D объект с геометрией в сцене.
 * <p>
 * Объединяет трансформацию ({@link ObjectInstance}) с геометрией ({@link Mesh}).
 * Создает глубокую копию меша при создании, что позволяет изменять объект независимо от исходной модели.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class SimpleObject extends ObjectInstance {
    private final Mesh mesh;

    /**
     * Создает простой объект из загруженной 3D модели.
     * <p>
     * Создает копию меша модели, чтобы изменения материала и трансформаций не затрагивали другие объекты, использующие ту же модель.
     *
     * @param object    загруженная 3D модель
     * @param position  начальная позиция объекта
     * @param rotation  начальное вращение (углы Эйлера в радианах)
     * @param scale     начальный масштаб
     */
    public SimpleObject(Object3D object, Vector3D position, Vector3D rotation, Vector3D scale) {
        super(position, rotation, scale);
        mesh = object.getMesh().getCopy();
    }

    public Mesh getMesh() {
        return mesh;
    }

    @Override
    public String toString() {
        return String.format(
                "SimpleObject[%s, triangles=%d]",
                getTransform(),
                mesh.triangles().size()
        );
    }
}
