package game.configuration;

import math.Vector3D;

import java.awt.*;

public class GameConfig {
    public static final Vector3D INITIAL_CAMERA_POSITION = new Vector3D(0, 2, -10);
    public static final Vector3D INITIAL_CAMERA_ROTATION = new Vector3D(0, 0, 0);
    public static final double CAMERA_SPEED = 5.0;
    public static final double MOUSE_SENSITIVITY = 0.003;

    public static final long MAX_FRAME_TIME_MS = 250; //Ограничение длительности кадра

    public static final Color BACKGROUND_COLOR = Color.BLUE;
    public static final double BACKGROUND_BRIGHTNESS = 0.8;

    public static final Color AMBIENCE_COLOR = Color.WHITE;
    public static final double AMBIENCE_INTENSITY = 0.5;

    public static final Color SHADOW_COLOR = Color.BLUE;
    public static final double SHADOW_DARKNESS_FACTOR = 0.3;
    public static final double SHADOW_COLOR_MIX = 0.1;
}
