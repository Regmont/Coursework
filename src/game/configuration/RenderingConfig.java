package game.configuration;

public class RenderingConfig {
    public static final int SHADOW_MAP_RESOLUTION = 512;
    public static final int SPATIAL_GRID_CELL_SIZE = 16; //Размер ячейки экранного разбиения мешей

    public static final double SHADOW_BIAS = 0.0005; //Погрешность глубины точки при расчёте теней

    //Вертикальный поворот главной камеры
    public static final double MAX_PITCH = Math.PI / 2.0 - 0.01; // 89.99°
    public static final double MIN_PITCH = -Math.PI / 2.0 + 0.01; // -89.99°

    public static final double SHADOW_CAMERA_FOV = Math.PI / 2; // Вертикальный угол обзора теневой камеры

    //Границы дальности отсечения видимой области (мешей)
    public static final double MAIN_CAMERA_NEAR = 0.1;
    public static final double MAIN_CAMERA_FAR = 100.0;
    public static final double SHADOW_CAMERA_NEAR = 0.1;
    public static final double SHADOW_CAMERA_FAR = 100.0;
}
