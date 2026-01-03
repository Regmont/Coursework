package scene;

import geometry.Mesh;
import math.Vector3D;
import org.joml.Matrix4d;

public class ObjectInstance {
    private final Object3D object;
    private final String name;
    private final Mesh mesh;
    private Vector3D position;
    private Vector3D rotation;
    private Vector3D scale;

    public ObjectInstance(Object3D object, String name, Vector3D position, Vector3D rotation, Vector3D scale) {
        this.object = object;
        this.name = name;
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;

        mesh = object.getMesh().getCopy();
    }

    public Mesh getMesh() {
        return mesh;
    }

    public Vector3D getPosition() {
        return position;
    }

    public Vector3D getRotation() {
        return rotation;
    }

    public Vector3D getScale() {
        return scale;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public void setRotation(Vector3D rotation) {
        this.rotation = rotation;
    }

    public void setScale(Vector3D scale) {
        this.scale = scale;
    }

    public Matrix4d getModelMatrix() {
        return new Matrix4d()
                .translate(position.getX(), position.getY(), position.getZ())
                .rotateXYZ(rotation.getX(), rotation.getY(), rotation.getZ())
                .scale(scale.getX(), scale.getY(), scale.getZ());
    }
}
