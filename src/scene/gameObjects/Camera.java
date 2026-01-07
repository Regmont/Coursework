package scene.gameObjects;

import core.base.CameraBase;
import core.math.Vector3D;
import scene.config.SceneConfig;

/**
 * Камера для навигации по 3D сцене.
 * <p>
 * Наследует базовую функциональность камеры и добавляет скорость перемещения.
 * Используется для навигации по сцене с помощью управления пользователя.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class Camera extends CameraBase {
    /** Камера по умолчанию из настроек сцены. */
    public static final Camera DEFAULT_CAMERA =
            new Camera(SceneConfig.INITIAL_CAMERA_POSITION, SceneConfig.INITIAL_CAMERA_ROTATION);

    private double speed;

    /**
     * Создает камеру с указанной позицией и вращением.
     * <p>
     * Скорость и поле обзора берутся из настроек {@link SceneConfig}.
     *
     * @param position начальная позиция камеры в мировых координатах
     * @param rotation начальное вращение камеры (углы Эйлера в радианах)
     */
    public Camera(Vector3D position, Vector3D rotation) {
        super(position, rotation);
        this.speed = SceneConfig.CAMERA_SPEED;
        fov = SceneConfig.CAMERA_FOV;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    @Override
    public String toString() {
        return String.format(
                "Camera[position=%s, rotation=%s, fov=%.2frad (%.1f°), speed=%.2f]",
                getTransform().getPosition(),
                getTransform().getRotation(),
                fov,
                Math.toDegrees(fov),
                speed
        );
    }
}
