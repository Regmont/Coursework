package game;

import core.math.Vector3D;
import scene.Camera;

public class GameClient {
    public static void main(String[] args) {
        Camera camera = new Camera(new Vector3D(0, 2, -10), Vector3D.zeroVector);

        SceneSystem sceneSystem = new SceneSystem();
        sceneSystem.setCamera(camera);

        MainWindow window = new MainWindow(sceneSystem);
        window.setVisible(true);

        GameController gameController = new GameController(sceneSystem, window);
        gameController.startGameLoop();
    }
}