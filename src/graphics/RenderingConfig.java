package graphics;

import java.awt.*;

public class RenderingConfig {
    public static final int SHADOW_MAP_RESOLUTION = 512;
    public static final int SPATIAL_GRID_CELL_SIZE = 16; //Размер ячейки экранного разбиения мешей

    public static final double SHADOW_BIAS = 0.0005; //Погрешность глубины точки при расчёте теней



    public static final double SHADOW_CAMERA_FOV = Math.PI / 2; // Вертикальный угол обзора теневой камеры

    //Границы дальности отсечения видимой области (мешей)
    public static final double MAIN_CAMERA_NEAR = 0.1;
    public static final double MAIN_CAMERA_FAR = 100.0;
    public static final double SHADOW_CAMERA_NEAR = 0.1;
    public static final double SHADOW_CAMERA_FAR = 100.0;

    public static final Color NUMERICAL_ERROR_COLOR = Color.RED;
}
