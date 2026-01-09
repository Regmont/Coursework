package graphics.config;

import java.awt.*;

/**
 * Конфигурация рендеринга (качество, алгоритмы, параметры).
 * <p>
 * Содержит настройки качества графики, параметры алгоритмов рендеринга
 * и визуальные константы.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class RenderingConfig {
    /** Разрешение shadow map (ширина и высота в пикселях). */
    public static final int SHADOW_MAP_RESOLUTION = 512;

    /** Размер ячейки пространственной сетки для ускорения растеризации. */
    public static final int SPATIAL_GRID_CELL_SIZE = 16;

    /** Смещение глубины для предотвращения shadow acne (самозатенения). */
    public static final double SHADOW_BIAS = 0.0005;

    /** Цвет для визуализации теней (debug режим). */
    public static final Color SHADOW_COLOR = Color.BLUE;

    /** Угол обзора теневых камер (π/2 = 90° для cube map граней). */
    public static final double SHADOW_CAMERA_FOV = Math.PI / 2;

    /** Ближняя плоскость отсечения главной камеры. */
    public static final double MAIN_CAMERA_NEAR = 0.1;

    /** Дальняя плоскость отсечения главной камеры. */
    public static final double MAIN_CAMERA_FAR = 100.0;

    /** Ближняя плоскость отсечения теневых камер. */
    public static final double SHADOW_CAMERA_NEAR = 0.1;

    /** Дальняя плоскость отсечения теневых камер. */
    public static final double SHADOW_CAMERA_FAR = 100.0;

    /** Цвет для обозначения численных ошибок (например, NaN вершин). */
    public static final Color NUMERICAL_ERROR_COLOR = Color.RED;
}
