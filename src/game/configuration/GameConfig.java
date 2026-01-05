package game.configuration;

public class GameConfig {
    public static final int TARGET_FPS = 60;

    //Вертикальный поворот главной камеры
    public static final double MAX_PITCH = Math.PI / 2.0 - 0.01; // 89.99°
    public static final double MIN_PITCH = -Math.PI / 2.0 + 0.01; // -89.99°

    public static final double MOUSE_SENSITIVITY = 0.003;

    public static final long MAX_FRAME_TIME_MS = 250; //Ограничение длительности кадра
}
