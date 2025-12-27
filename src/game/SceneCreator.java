package game;

import graphics.light.PointLight;
import graphics.renderer.Material;
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

//        ObjectInstance objectInstance = new ObjectInstance(sphere, "Sphere 1",
//                new Vector3D(0, -3, 0), Vector3D.zeroVector, Vector3D.unitVector);
//
//        objectInstance.getObject().getMesh().setMaterial(new Material(Color.GRAY));
//
//        instances.add(objectInstance);
//
//        instances.add(new ObjectInstance(cube, "Cube 1",
//                new Vector3D(2, 0, 0), Vector3D.zeroVector, Vector3D.unitVector));

        instances.add(new ObjectInstance(terrain, "Terrain",
                new Vector3D(0, -5, 0), Vector3D.zeroVector, Vector3D.unitVector));

        return instances;
    }

    public static List<PointLight> createDefaultLight() {
        PointLight pointLight = new PointLight(new Vector3D(0, 0, 0), Color.RED, 0.5);

        List<PointLight> pointLights = new ArrayList<>();

        pointLights.add(pointLight);

        return pointLights;
    }
}