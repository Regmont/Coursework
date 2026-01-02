package game;

import graphics.SceneSystem;
import scene.Camera;

public class GameClient {
    public static void main(String[] args) {
        Camera camera = new Camera();
        SceneSystem sceneSystem = new SceneSystem(camera);

        MainWindow window = new MainWindow(sceneSystem);
        window.setVisible(true);

        GameController gameController = new GameController(camera, window);
        gameController.startGameLoop();
    }
}