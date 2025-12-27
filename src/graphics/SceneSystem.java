package graphics;

import game.SceneCreator;
import geometry.Mesh;
import graphics.light.PointLight;
import scene.Camera;
import scene.ObjectInstance;
import org.joml.Matrix4d;

import java.util.ArrayList;
import java.util.List;

public class SceneSystem {
    private final List<ObjectInstance> instances;
    private final List<PointLight> lights;
    private final Camera camera;

    public SceneSystem(Camera camera) {
        this.camera = camera;
        instances = SceneCreator.createDefaultScene();
        lights = SceneCreator.createDefaultLight();
    }

    public List<Mesh> getTransformedMeshes(int screenWidth, int screenHeight) {
        List<Mesh> meshes = new ArrayList<>();

        for (ObjectInstance instance : instances) {
            Matrix4d modelMatrix = instance.getModelMatrix();

            Mesh originalMesh = instance.getObject().getMesh();
            Mesh transformedMesh = SceneTransformer.transformMesh(
                    originalMesh, modelMatrix, camera, screenWidth, screenHeight
            );
            meshes.add(transformedMesh);
        }

        return meshes;
    }

    public List<PointLight> getPointLights() {
        return lights;
    }
}