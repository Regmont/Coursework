package core.base;

import core.math.Vector3D;

import org.joml.Matrix4d;
import org.joml.Quaterniond;
import org.joml.Vector3d;

public class Transform {
    private Vector3D position;
    private Vector3D rotation;
    private Vector3D scale;

    public Transform(Vector3D position, Vector3D rotation, Vector3D scale) {
        this.position = position;
        this.rotation = rotation;
        this.scale = scale;
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

    public Vector3D getForward() {
        Quaterniond q = new Quaterniond()
                .rotateY(rotation.getY())
                .rotateX(rotation.getX())
                .rotateZ(rotation.getZ());

        Vector3d localForward = new Vector3d(0, 0, 1);
        q.transform(localForward);

        return new Vector3D(localForward.x, localForward.y, localForward.z).normalize();
    }

    public Vector3D getUp() {
        Quaterniond q = new Quaterniond()
                .rotateY(rotation.getY())
                .rotateX(rotation.getX())
                .rotateZ(rotation.getZ());

        Vector3d localUp = new Vector3d(0, 1, 0);
        q.transform(localUp);

        return new Vector3D(localUp.x, localUp.y, localUp.z).normalize();
    }

    public Vector3D getRight() {
        return getForward().cross(getUp()).normalize();
    }
}
