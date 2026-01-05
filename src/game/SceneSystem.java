package game;

import scene.*;

import java.util.List;

public class SceneSystem {
    private final List<SimpleObject> objects;
    private final List<PointLight> lights;
    private Sky sky;
    private AmbienceLight ambienceLight;
    private Camera camera;

    public SceneSystem() {
        objects = SceneCreator.createDefaultScene();
        lights = SceneCreator.createDefaultLight();

        sky = Sky.DEFAULT_SKY;
        ambienceLight = AmbienceLight.DEFAULT_AMBIENCE_LIGHT;
        camera = Camera.DEFAULT_CAMERA;
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