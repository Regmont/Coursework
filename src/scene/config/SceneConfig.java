package scene.config;

import core.math.Vector3D;

import java.awt.*;

public class SceneConfig {
    public static final Color AMBIENCE_COLOR = Color.WHITE;
    public static final double AMBIENCE_INTENSITY = 0.5;

    public static final Color POINT_LIGHT_COLOR = Color.YELLOW;
    public static final double POINT_LIGHT_INTENSITY = 1.0;

    public static final Color BACKGROUND_COLOR = Color.CYAN;
    public static final double BACKGROUND_BRIGHTNESS = 0.6;

    public static final Vector3D INITIAL_CAMERA_POSITION = Vector3D.zeroVector;
    public static final Vector3D INITIAL_CAMERA_ROTATION = Vector3D.zeroVector;
    public static final double CAMERA_SPEED = 5.0;
    public static final double CAMERA_FOV = Math.PI / 3; // Вертикальный угол обзора камеры (60°)
}
