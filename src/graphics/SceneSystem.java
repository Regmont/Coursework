package graphics;

import game.SceneCreator;
import geometry.Mesh;
import scene.*;
import scene.PointLight;
import org.joml.Matrix4d;

import java.util.ArrayList;
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

    public List<Mesh> getTransformedMeshes(int screenWidth, int screenHeight) {
        List<Mesh> meshes = new ArrayList<>();

        for (SimpleObject object : objects) {
            Matrix4d modelMatrix = object.getTransform().getModelMatrix();

            Mesh originalMesh = object.getMesh();
            Mesh transformedMesh = SceneTransformer.transformMesh(
                    originalMesh, modelMatrix, camera, screenWidth, screenHeight
            );
            meshes.add(transformedMesh);
        }

        for (PointLight light : lights) {
            if (light.hasObject()) {
                SimpleObject object = light.getObject();
                Matrix4d modelMatrix = object.getTransform().getModelMatrix();
                Mesh transformedMesh = SceneTransformer.transformMesh(
                        object.getMesh(),
                        modelMatrix,
                        camera,
                        screenWidth,
                        screenHeight
                );
                meshes.add(transformedMesh);
            }
        }

        return meshes;
    }

    public List<PointLight> getPointLights() {
        return lights;
    }

    public List<SimpleObject> getInstances() {
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