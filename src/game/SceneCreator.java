package game;

import scene.PointLight;
import scene.Object3D;
import math.Vector3D;
import scene.SimpleObject;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class SceneCreator {

    public static List<SimpleObject> createDefaultScene() {
        Object3D sphere = new Object3D("Sphere");
        Object3D cube = new Object3D("Cube");
        Object3D terrain = new Object3D("Terrain", "Terrain");

        List<SimpleObject> objects = new ArrayList<>();

        objects.add(new SimpleObject(sphere,
                new Vector3D(2, 0, 0), Vector3D.zeroVector, Vector3D.unitVector));

        objects.add(new SimpleObject(cube,
                new Vector3D(0, -2, 0), Vector3D.zeroVector, Vector3D.unitVector));

        objects.add(new SimpleObject(terrain,
                new Vector3D(0, -4, 0), Vector3D.zeroVector, Vector3D.unitVector));

        return objects;
    }

    public static List<PointLight> createDefaultLight() {
        Object3D smallSphere = new Object3D("SmallSphere");

        SimpleObject light1 = new SimpleObject(smallSphere,
                Vector3D.zeroVector, Vector3D.zeroVector, Vector3D.unitVector);
        SimpleObject light2 = new SimpleObject(smallSphere,
                Vector3D.zeroVector, Vector3D.zeroVector, Vector3D.unitVector);

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