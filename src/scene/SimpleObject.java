package scene;

import geometry.Mesh;
import math.Vector3D;

public class SimpleObject extends ObjectInstance {
    private final Mesh mesh;

    public SimpleObject(Object3D object, Vector3D position, Vector3D rotation, Vector3D scale) {
        super(position, rotation, scale);
        mesh = object.getMesh().getCopy();
    }

    public Mesh getMesh() {
        return mesh;
    }
}
