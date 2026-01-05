package graphics;

import scene.gameObjects.*;

import java.util.ArrayList;
import java.util.List;

public class SceneSystem {
    private final List<SimpleObject> objects = new ArrayList<>();
    private final List<PointLight> lights = new ArrayList<>();
    private Sky sky;
    private AmbienceLight ambienceLight;
    private Camera camera;

    public SceneSystem() {
        sky = Sky.DEFAULT_SKY;
        ambienceLight = AmbienceLight.DEFAULT_AMBIENCE_LIGHT;
        camera = Camera.DEFAULT_CAMERA;
    }

    public void addObjectsToScene(List<SimpleObject> simpleObject) {
        objects.addAll(simpleObject);
    }

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
}