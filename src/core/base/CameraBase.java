package core.base;

import core.math.Vector3D;

import org.joml.Matrix4d;

/**
 * Абстрактный базовый класс для камер в 3D сцене.
 * <p>
 * Наследует от {@link ObjectInstance} и добавляет функциональность, специфичную для камер:
 * поле обзора (FOV) и методы для получения матриц вида и проекции.
 * <p>
 * Матрицы используются для преобразования 3D координат в 2D экранные координаты.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public abstract class CameraBase extends ObjectInstance {
    /** Угол обзора (field of view) в радианах. */
    protected double fov;

    /**
     * Создает камеру с указанным положением и вращением.
     * <p>
     * Масштаб камеры всегда единичный (не используется).
     *
     * @param position положение камеры в мировых координатах
     * @param rotation вращение камеры (углы Эйлера в радианах)
     */
    public CameraBase(Vector3D position, Vector3D rotation) {
        super(position, rotation, Vector3D.oneVector);
    }

    public double getFov() {
        return fov;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }

    /**
     * Вычисляет матрицу вида для указанной камеры.
     * <p>
     * Матрица вида преобразует мировые координаты в систему координат камеры
     * (где камера находится в начале и смотрит по оси Z).
     * <p>
     * Вычисляется на основе положения камеры, направления ее взгляда и вектора "вверх".
     *
     * @param camera камера, для которой вычисляется матрица
     * @return матрица вида 4×4
     * @throws NullPointerException если {@code camera} равен {@code null}
     * @throws NullPointerException если {@code camera.getTransform()} возвращает {@code null}
     */
    public static Matrix4d getViewMatrix(CameraBase camera) {
        Vector3D position = camera.getTransform().getPosition();;
        Vector3D lookAt = position.add(camera.getTransform().getForward());
        Vector3D up = camera.getTransform().getUp();

        return new Matrix4d().lookAt(
                position.x(), position.y(), position.z(),
                lookAt.x(), lookAt.y(), lookAt.z(),
                up.x(), up.y(), up.z());
    }

    /**
     * Вычисляет матрицу проекции для указанной камеры.
     * <p>
     * Матрица проекции преобразует координаты камеры в нормализованные координаты устройства (NDC)
     * для последующего отображения на экране.
     * <p>
     * Использует перспективную проекцию с учетом угла обзора, соотношения сторон экрана и
     * расстояний до ближней и дальней плоскостей отсечения.
     *
     * @param camera    камера, для которой вычисляется матрица
     * @param width     ширина области отображения в пикселях
     * @param height    высота области отображения в пикселях
     * @param nearPlane расстояние до ближней плоскости отсечения (должно быть > 0)
     * @param farPlane  расстояние до дальней плоскости отсечения (должно быть > nearPlane)
     * @return матрица проекции 4×4
     * @throws IllegalArgumentException если:
     *         - {@code width} ≤ 0
     *         - {@code height} ≤ 0
     *         - {@code nearPlane} ≤ 0
     *         - {@code farPlane} ≤ {@code nearPlane}
     *         - {@code camera.getFov()} не в диапазоне (0, π)
     */
    public static Matrix4d getProjectionMatrix(CameraBase camera, int width, int height,
                                               double nearPlane, double farPlane) {
        double aspectRatio = (double) width / height;

        return new Matrix4d().perspective(camera.getFov(), aspectRatio, nearPlane, farPlane);
    }

    @Override
    public String toString() {
        return String.format(
                "CameraBase[fov=%.2frad (%.1f°), transform=%s]",
                fov, Math.toDegrees(fov), getTransform()
        );
    }
}
