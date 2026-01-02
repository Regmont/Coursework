package scene;

import game.configuration.GameConfig;
import game.configuration.RenderingConfig;
import math.Vector3D;
import org.joml.Matrix4d;

public class Camera {
    private Vector3D position;
    private Vector3D rotation;
    private final double speed;

    public Camera() {
        this.position = GameConfig.INITIAL_CAMERA_POSITION;
        this.rotation = GameConfig.INITIAL_CAMERA_ROTATION;
        this.speed = GameConfig.CAMERA_SPEED;
    }

    public Camera(Vector3D position, Vector3D rotation) {
        this.position = position;
        this.rotation = rotation;
        this.speed = 0;
    }

    public Vector3D getPosition() {
        return position;
    }

    public Vector3D getRotation() {
        return rotation;
    }

    public void setPosition(Vector3D position) {
        this.position = position;
    }

    public void addRotation(double deltaYaw, double deltaPitch) {
        double newYaw = rotation.getX() + deltaYaw;
        double newPitch = rotation.getY() + deltaPitch;

        newPitch = Math.max(RenderingConfig.MIN_PITCH, Math.min(RenderingConfig.MAX_PITCH, newPitch));

        rotation = new Vector3D(newYaw, newPitch, rotation.getZ());
    }

    public Matrix4d getViewMatrix() {
        Vector3D lookAt = getLookAtPoint();

        return new Matrix4d().lookAt(
                        position.getX(), position.getY(), position.getZ(),
                        lookAt.getX(), lookAt.getY(), lookAt.getZ(),
                        0, 1, 0);
    }

    public Matrix4d getProjectionMatrix(int screenWidth, int screenHeight) {
        double aspectRatio = (double) screenWidth / screenHeight;

        return new Matrix4d().perspective(RenderingConfig.FOV, aspectRatio, RenderingConfig.NEAR_PLANE,
                RenderingConfig.FAR_PLANE);
    }

    public double getSpeed() {
        return speed;
    }

    private Vector3D getLookAtPoint() {
        double yaw = rotation.getX();
        double pitch = rotation.getY();

        double lookX = position.getX() + Math.cos(pitch) * Math.sin(yaw);
        double lookY = position.getY() + Math.sin(pitch);
        double lookZ = position.getZ() + Math.cos(pitch) * Math.cos(yaw);

        return new Vector3D(lookX, lookY, lookZ);
    }
}