package game;

import scene.Object3D;
import scene.ObjectInstance;
import math.Vector3D;
import java.util.ArrayList;
import java.util.List;

public class SceneCreator {

    public static List<ObjectInstance> createDefaultScene() {
        Object3D sphere = new Object3D("Sphere");
        Object3D cube = new Object3D("Cube");

        List<ObjectInstance> instances = new ArrayList<>();

        instances.add(new ObjectInstance(sphere, "Sphere 1",
                new Vector3D(0, 0, -10), Vector3D.zeroVector, Vector3D.unitVector));

        instances.add(new ObjectInstance(cube, "Cube 1",
                new Vector3D(10, 0, -10), Vector3D.zeroVector, Vector3D.unitVector));

        instances.add(new ObjectInstance(cube, "Cube 2",
                new Vector3D(10, 10, -10), Vector3D.zeroVector, Vector3D.unitVector));

        return instances;
    }
}