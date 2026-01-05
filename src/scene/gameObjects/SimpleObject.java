package scene.gameObjects;

import core.base.ObjectInstance;
import core.math.Vector3D;
import scene.Mesh;
import scene.Object3D;

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
