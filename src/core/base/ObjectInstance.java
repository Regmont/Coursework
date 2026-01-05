package core.base;

import core.math.Vector3D;

public abstract class ObjectInstance {
    private final Transform transform;

    public ObjectInstance(Vector3D position, Vector3D rotation, Vector3D scale) {
        transform = new Transform(position, rotation, scale);
    }

    public Transform getTransform() {
        return transform;
    }
}
