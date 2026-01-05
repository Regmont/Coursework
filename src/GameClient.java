import core.math.Vector3D;
import game.GameController;
import graphics.RenderingSystem;
import graphics.SceneSystem;
import scene.gameObjects.Camera;

public class GameClient {
    public static void main(String[] args) {
        Camera camera = new Camera(new Vector3D(0, 2, -10), Vector3D.zeroVector);

        SceneSystem sceneSystem = new SceneSystem();
        sceneSystem.setCamera(camera);
        sceneSystem.addObjectsToScene(SceneCreator.createObjects());
        sceneSystem.addPointLights(SceneCreator.createDefaultLight());

        RenderingSystem renderingSystem = new RenderingSystem(sceneSystem);

        GameController gameController = new GameController(sceneSystem, renderingSystem);
        gameController.startGameLoop();
    }
}