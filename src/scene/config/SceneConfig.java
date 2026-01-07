package scene.config;

import core.math.Vector3D;

import java.awt.*;

/**
 * Конфигурационные параметры сцены по умолчанию.
 * <p>
 * Содержит значения по умолчанию для всех компонентов сцены: освещения, фона, камеры и других визуальных параметров.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class SceneConfig {
    /** Цвет окружающего (ambient) освещения. */
    public static final Color AMBIENCE_COLOR = Color.WHITE;

    /** Интенсивность окружающего освещения (0.0 - нет, 1.0 - максимум). */
    public static final double AMBIENCE_INTENSITY = 0.5;

    /** Цвет точечных источников света. */
    public static final Color POINT_LIGHT_COLOR = Color.YELLOW;

    /** Интенсивность точечных источников света (0.0 - нет, 1.0 - максимум). */
    public static final double POINT_LIGHT_INTENSITY = 1.0;

    /** Цвет фона (неба) сцены. */
    public static final Color BACKGROUND_COLOR = Color.CYAN;

    /** Яркость фона (0.0 - нет, 1.0 - максимум). */
    public static final double BACKGROUND_BRIGHTNESS = 0.6;

    /** Начальная позиция камеры в мировых координатах. */
    public static final Vector3D INITIAL_CAMERA_POSITION = Vector3D.zeroVector;

    /** Начальное вращение камеры (углы Эйлера в радианах). */
    public static final Vector3D INITIAL_CAMERA_ROTATION = Vector3D.zeroVector;

    /** Скорость перемещения камеры при управлении (единиц в секунду). */
    public static final double CAMERA_SPEED = 5.0;

    /** Вертикальный угол обзора камеры (FOV) в радианах. */
    public static final double CAMERA_FOV = Math.PI / 3; // (60°)
}
