package graphics;

import math.Vector3D;
import org.joml.Matrix4d;
import scene.ObjectInstance;

public abstract class CameraBase extends ObjectInstance {
    protected double fov;

    public CameraBase(Vector3D position, Vector3D rotation) {
        super(position, rotation, Vector3D.unitVector);
    }

    public double getFov() {
        return fov;
    }

    public void setFov(double fov) {
        this.fov = fov;
    }

    public static Matrix4d getViewMatrix(CameraBase camera) {
        Vector3D position = camera.getTransform().getPosition();;
        Vector3D lookAt = position.add(camera.getTransform().getForward());
        Vector3D up = camera.getTransform().getUp();

        return new Matrix4d().lookAt(
                position.getX(), position.getY(), position.getZ(),
                lookAt.getX(), lookAt.getY(), lookAt.getZ(),
                up.getX(), up.getY(), up.getZ());
    }

    public static Matrix4d getProjectionMatrix(CameraBase camera, int width, int height,
                                               double nearPlane, double farPlane) {
        double aspectRatio = (double) width / height;

        return new Matrix4d().perspective(camera.getFov(), aspectRatio, nearPlane, farPlane);
    }
}
