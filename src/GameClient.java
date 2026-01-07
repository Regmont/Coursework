import core.math.Vector3D;
import game.GameController;
import graphics.RenderingSystem;
import graphics.SceneSystem;
import scene.gameObjects.Camera;

/**
 * Основной клиентский класс для запуска игрового приложения.
 * <p>
 * Содержит точку входа ({@link #main}) и выполняет инициализацию
 * всех систем приложения в правильном порядке.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class GameClient {
    /**
     * Точка входа в игровое приложение.
     * <p>
     * Порядок инициализации:
     * <ol>
     *   <li>Создание камеры с начальной позицией</li>
     *   <li>Инициализация системы сцены</li>
     *   <li>Добавление объектов и освещения</li>
     *   <li>Создание системы рендеринга</li>
     *   <li>Запуск игрового контроллера</li>
     * </ol>
     *
     * @param args аргументы командной строки (не используются)
     */
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
