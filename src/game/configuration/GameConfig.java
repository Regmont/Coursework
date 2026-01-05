package game.configuration;

import core.math.Vector3D;

import java.awt.*;

public class GameConfig {
    public static final int TARGET_FPS = 60;

    public static final Vector3D INITIAL_CAMERA_POSITION = Vector3D.zeroVector;
    public static final Vector3D INITIAL_CAMERA_ROTATION = Vector3D.zeroVector;
    public static final double CAMERA_SPEED = 5.0;
    public static final double CAMERA_FOV = Math.PI / 3; // Вертикальный угол обзора камеры (60°)

    //Вертикальный поворот главной камеры
    public static final double MAX_PITCH = Math.PI / 2.0 - 0.01; // 89.99°
    public static final double MIN_PITCH = -Math.PI / 2.0 + 0.01; // -89.99°

    public static final double MOUSE_SENSITIVITY = 0.003;

    public static final long MAX_FRAME_TIME_MS = 250; //Ограничение длительности кадра

    public static final Color BACKGROUND_COLOR = Color.BLUE;
    public static final double BACKGROUND_BRIGHTNESS = 0.8;

    public static final Color AMBIENCE_COLOR = Color.WHITE;
    public static final double AMBIENCE_INTENSITY = 0.5;

    public static final Color POINT_LIGHT_COLOR = Color.YELLOW;
    public static final double POINT_LIGHT_INTENSITY = 1.0;

    public static final Color SHADOW_COLOR = Color.BLUE;
    public static final double SHADOW_DARKNESS_FACTOR = 0.3;
    public static final double SHADOW_COLOR_MIX = 0.1;
}
