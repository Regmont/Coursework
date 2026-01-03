package game;

import graphics.light.PointLight;
import scene.Object3D;
import scene.ObjectInstance;
import math.Vector3D;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SceneCreator {

    public static List<ObjectInstance> createDefaultScene() {
        Object3D sphere = new Object3D("Sphere");
        Object3D cube = new Object3D("Cube");
        Object3D terrain = new Object3D("Terrain");

        List<ObjectInstance> instances = new ArrayList<>();

        instances.add(new ObjectInstance(sphere, "Sphere 1",
                new Vector3D(2, 0, 1), Vector3D.zeroVector, Vector3D.unitVector));

        instances.add(new ObjectInstance(cube, "Cube 1",
                new Vector3D(0, -2, 0), Vector3D.zeroVector, Vector3D.unitVector));

        instances.add(new ObjectInstance(terrain, "Terrain",
                new Vector3D(0, -4, 0), Vector3D.zeroVector, Vector3D.unitVector));

        return instances;
    }

    public static List<PointLight> createDefaultLight() {
        Object3D smallSphere = new Object3D("SmallSphere");

        ObjectInstance light1 = new ObjectInstance(smallSphere, "Light 1",
                Vector3D.zeroVector, Vector3D.zeroVector, Vector3D.unitVector);
        ObjectInstance light2 = new ObjectInstance(smallSphere, "Light 2",
                Vector3D.zeroVector, Vector3D.zeroVector, Vector3D.unitVector);

        PointLight pointLight = new PointLight(new Vector3D(5, 5, 0), Color.RED, 1, light1);
        PointLight pointLight2 = new PointLight(new Vector3D(0, 7, 3), Color.YELLOW, 1, light2);

        List<PointLight> pointLights = new ArrayList<>();
        pointLights.add(pointLight);
        pointLights.add(pointLight2);

        return pointLights;
    }
}