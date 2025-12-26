package game;

import graphics.SceneSystem;
import math.Vector3D;
import scene.Camera;

import java.awt.*;

public class GameClient {
    private static final Vector3D INITIAL_CAMERA_POSITION = new Vector3D(0, 4, 5);
    private static final Vector3D INITIAL_CAMERA_ROTATION = new Vector3D(-135, 0, 0);
    private static final String TITLE = "3D Game";
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final Color BACKGROUND_COLOR = Color.GRAY;
    private static final double BACKGROUND_BRIGHTNESS = 0.8;
    private static final int TARGET_FPS = 60;
    private static final double MOUSE_SENSITIVITY = 0.003;
    private static final double CAMERA_SPEED = 5.0;

    public static void main(String[] args) {
        Camera camera = new Camera(INITIAL_CAMERA_POSITION, INITIAL_CAMERA_ROTATION, CAMERA_SPEED);

        SceneSystem sceneSystem = new SceneSystem(camera);

        MainWindow window = new MainWindow(sceneSystem, TITLE, WINDOW_WIDTH, WINDOW_HEIGHT,
                BACKGROUND_COLOR, BACKGROUND_BRIGHTNESS);
        window.setVisible(true);

        GameController gameController = new GameController(camera, window, TARGET_FPS, MOUSE_SENSITIVITY);
        gameController.startGameLoop();
    }
}