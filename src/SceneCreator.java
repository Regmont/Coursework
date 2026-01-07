import core.math.Vector3D;
import scene.gameObjects.PointLight;
import scene.Object3D;
import scene.gameObjects.SimpleObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Фабрика для создания и настройки объектов сцены.
 * <p>
 * Предоставляет методы для генерации тестовых сцен с объектами и освещением.
 * Используется в демонстрационных целях и для тестирования рендеринга.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class SceneCreator {
    /**
     * Создает набор объектов для тестовой сцены.
     * <p>
     * Генерирует коллекцию базовых объектов с различными трансформациями
     * для демонстрации возможностей рендеринга.
     *
     * @return список созданных объектов сцены
     */
    public static List<SimpleObject> createObjects() {
        Object3D sphere = new Object3D("Sphere");
        Object3D cube = new Object3D("Cube");
        Object3D terrain = new Object3D("Terrain", "Terrain");

        List<SimpleObject> objects = new ArrayList<>();

        objects.add(new SimpleObject(sphere,
                new Vector3D(2, 0, 0), Vector3D.zeroVector, Vector3D.oneVector));

        objects.add(new SimpleObject(cube,
                new Vector3D(0, -2, 0), Vector3D.zeroVector, Vector3D.oneVector));

        objects.add(new SimpleObject(terrain,
                new Vector3D(0, -4, 0), Vector3D.zeroVector, Vector3D.oneVector));

        return objects;
    }

    /**
     * Создает стандартное освещение для тестовой сцены.
     * <p>
     * Генерирует точечные источники света с визуальными представлениями
     * (объектами-индикаторами) для демонстрации работы системы освещения.
     *
     * @return список точечных источников света
     */
    public static List<PointLight> createDefaultLight() {
        Object3D smallSphere = new Object3D("SmallSphere");

        SimpleObject light1 = new SimpleObject(smallSphere,
                Vector3D.zeroVector, Vector3D.zeroVector, Vector3D.oneVector);
        SimpleObject light2 = new SimpleObject(smallSphere,
                Vector3D.zeroVector, Vector3D.zeroVector, Vector3D.oneVector);

        PointLight pointLight = new PointLight(new Vector3D(3, 7, 0));
        pointLight.setObject(light1);
        pointLight.setColor(Color.RED);
        PointLight pointLight2 = new PointLight(new Vector3D(-3, 7, 0));
        pointLight2.setObject(light2);

        List<PointLight> pointLights = new ArrayList<>();
        pointLights.add(pointLight);
        pointLights.add(pointLight2);

        return pointLights;
    }
}
