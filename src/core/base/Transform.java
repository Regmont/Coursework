package core.base;

import core.math.Vector3D;

import org.joml.Matrix4d;
import org.joml.Quaterniond;
import org.joml.Vector3d;

/**
 * Класс для представления трансформации (положения, поворота и масштаба) объекта в 3D пространстве.
 * <p>
 * Использует библиотеку JOML для матричных и кватернионных вычислений.
 * Хранит трансформацию в виде трех векторов: позиции, вращения (углы Эйлера в радианах) и масштаба.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class Transform {
    private Vector3D position;
    private Vector3D rotation;
    private Vector3D scale;

    /**
     * Создает трансформацию с указанными параметрами.
     *
     * @param position положение объекта в мировых координатах
     * @param rotation вращение объекта в углах Эйлера (в радианах) по осям XYZ
     * @param scale    масштаб объекта по осям XYZ
     */
    public Transform(Vector3D position, Vector3D rotation, Vector3D scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
    }

    public Vector3D getPosition() {
        return position;
    }
    public Vector3D getRotation() {
        return rotation;
    }
    public Vector3D getScale() {
        return scale;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }
    public void setRotation(Vector3D rotation) {
        this.rotation = rotation;
    }
    public void setScale(Vector3D scale) {
        this.scale = scale;
    }

    /**
     * Вычисляет матрицу модели для этой трансформации.
     * <p>
     * Матрица модели используется для преобразования вершин объекта из локальных
     * координат в мировые. Вычисляется как:
     * <pre>
     * M = T * R * S
     * где:
     *   T - матрица перемещения
     *   R - матрица вращения
     *   S - матрица масштабирования
     * </pre>
     *
     * @return матрица модели 4x4
     */
    public Matrix4d getModelMatrix() {
        return new Matrix4d()
                .translate(position.x(), position.y(), position.z())
                .rotateXYZ(rotation.x(), rotation.y(), rotation.z())
                .scale(scale.x(), scale.y(), scale.z());
    }

    /**
     * Возвращает направление "вперед" объекта в мировых координатах.
     * <p>
     * Вычисляется как результат поворота локального вектора (0, 0, 1)
     * в соответствии с текущим вращением объекта.
     *
     * @return единичный вектор направления вперед
     */
    public Vector3D getForward() {
        Quaterniond q = new Quaterniond()
                .rotateY(rotation.y())
                .rotateX(rotation.x())
                .rotateZ(rotation.z());

        Vector3d localForward = new Vector3d(0, 0, 1);
        q.transform(localForward);

        return new Vector3D(localForward.x, localForward.y, localForward.z).normalize();
    }

    /**
     * Возвращает направление "вверх" объекта в мировых координатах.
     * <p>
     * Вычисляется как результат поворота локального вектора (0, 1, 0)
     * в соответствии с текущим вращением объекта.
     *
     * @return единичный вектор направления вверх
     */
    public Vector3D getUp() {
        Quaterniond q = new Quaterniond()
                .rotateY(rotation.y())
                .rotateX(rotation.x())
                .rotateZ(rotation.z());

        Vector3d localUp = new Vector3d(0, 1, 0);
        q.transform(localUp);

        return new Vector3D(localUp.x, localUp.y, localUp.z).normalize();
    }

    /**
     * Возвращает направление "вправо" объекта в мировых координатах.
     * <p>
     * Вычисляется как векторное произведение направлений "вперед" и "вверх".
     *
     * @return единичный вектор направления вправо
     * @see #getForward()
     * @see #getUp()
     * @see Vector3D#cross(Vector3D)
     */
    public Vector3D getRight() {
        return getForward().cross(getUp()).normalize();
    }

    @Override
    public String toString() {
        return String.format(
                "Transform[position=(%.2f, %.2f, %.2f), rotation=(%.2f, %.2f, %.2f), scale=(%.2f, %.2f, %.2f)]",
                position.x(), position.y(), position.z(),
                rotation.x(), rotation.y(), rotation.z(),
                scale.x(), scale.y(), scale.z()
        );
    }
}
