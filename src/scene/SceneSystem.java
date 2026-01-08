package scene;

import scene.gameObjects.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Центральная система управления 3D сценой.
 * <p>
 * Содержит и управляет всеми объектами, источниками света, окружением и камерой.
 * Является контейнером для данных сцены, которые используются отрисовщиком (рендерером).
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class SceneSystem {
    private final List<SimpleObject> objects = new ArrayList<>();
    private final List<PointLight> lights = new ArrayList<>();
    private Sky sky;
    private AmbienceLight ambienceLight;
    private Camera camera;

    /**
     * Создает систему сцены с настройками по умолчанию.
     * <p>
     * Инициализирует:
     * - Небо ({@link Sky#DEFAULT_SKY})
     * - Окружающее освещение ({@link AmbienceLight#DEFAULT_AMBIENCE_LIGHT})
     * - Камеру ({@link Camera#DEFAULT_CAMERA})
     */
    public SceneSystem() {
        sky = Sky.DEFAULT_SKY;
        ambienceLight = AmbienceLight.DEFAULT_AMBIENCE_LIGHT;
        camera = Camera.DEFAULT_CAMERA;
    }

    /**
     * Добавляет список объектов в сцену.
     *
     * @param simpleObject список объектов для добавления
     */
    public void addObjectsToScene(List<SimpleObject> simpleObject) {
        objects.addAll(simpleObject);
    }

    /**
     * Добавляет список точечных источников света в сцену.
     *
     * @param pointLights список источников света
     */
    public void addPointLights(List<PointLight> pointLights) {
        lights.addAll(pointLights);
    }

    public List<PointLight> getPointLights() {
        return lights;
    }
    public List<SimpleObject> getObjects() {
        return objects;
    }
    public Sky getSky() {
        return sky;
    }
    public AmbienceLight getAmbienceLight() {
        return ambienceLight;
    }
    public Camera getCamera() {
        return camera;
    }

    public void setSky(Sky sky) {
        this.sky = sky;
    }
    public void setAmbienceLight(AmbienceLight ambienceLight) {
        this.ambienceLight = ambienceLight;
    }
    public void setCamera(Camera camera) {
        this.camera = camera;
    }

    @Override
    public String toString() {
        return String.format(
                "SceneSystem[objects=%d, lights=%d, sky=%s, ambience=%s, camera=%s]",
                objects.size(),
                lights.size(),
                sky,
                ambienceLight,
                camera
        );
    }
}
