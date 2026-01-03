package game;

import graphics.SceneSystem;

public class GameClient {
    public static void main(String[] args) {
        SceneSystem sceneSystem = new SceneSystem();

        MainWindow window = new MainWindow(sceneSystem);
        window.setVisible(true);

        GameController gameController = new GameController(sceneSystem, window);
        gameController.startGameLoop();
    }
}