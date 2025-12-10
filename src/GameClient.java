import game.*;
import sceneObjects.Camera;
import geometricObjects.Mesh;

import java.awt.*;
import java.util.ArrayList;

public class GameClient {
    private static final double INITIAL_X = 0;
    private static final double INITIAL_Y = 2;
    private static final double INITIAL_Z = 5;
    private static final String TITLE = "3D Game";
    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final Color BACKGROUND_COLOR = Color.BLACK;
    private static final int TARGET_FPS = 60;

    public static void main(String[] args) {
        Camera camera = new Camera(INITIAL_X, INITIAL_Y, INITIAL_Z);
        ArrayList<Mesh> objects = new ArrayList<>();

        MainWindow window = new MainWindow(objects, TITLE, WINDOW_WIDTH, WINDOW_HEIGHT, BACKGROUND_COLOR);
        window.setVisible(true);

        GameController gameController = new GameController(camera, objects, window, TARGET_FPS);

        gameController.setupWindowListeners(window);

        EventQueue.invokeLater(gameController::captureMouse);

        gameController.startGameLoop();

        window.setFocusable(true);
        window.requestFocus();
    }
}