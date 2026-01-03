package graphics.shadows;

import game.configuration.RenderingConfig;
import math.Vector3D;
import org.joml.Matrix4d;

public class ShadowCamera {
    private final Vector3D position;
    private final Vector3D direction;

    public ShadowCamera(Vector3D position, Vector3D direction) {
        this.position = position;
        this.direction = direction;
    }

    public Matrix4d getViewMatrix() {
        Vector3D lookAt = position.add(direction);
        Vector3D up = calculateUpVector(direction);

        return new Matrix4d().lookAt(
                position.getX(), position.getY(), position.getZ(),
                lookAt.getX(), lookAt.getY(), lookAt.getZ(),
                up.getX(), up.getY(), up.getZ()
        );
    }

    public Matrix4d getProjectionMatrix() {
        return new Matrix4d().perspective(RenderingConfig.FOV, RenderingConfig.SHADOW_MAP_ASPECT_RATIO,
                RenderingConfig.SHADOW_CAMERA_NEAR, RenderingConfig.SHADOW_CAMERA_FAR);
    }

    public Vector3D getPosition() {
        return position;
    }

    private Vector3D calculateUpVector(Vector3D dir) {
        double x = Math.abs(dir.getX());
        double y = Math.abs(dir.getY());
        double z = Math.abs(dir.getZ());

        if (x > y && x > z) {
            return new Vector3D(0, 1, 0);
        } else if (y > z) {
            return new Vector3D(0, 0, dir.getY() > 0 ? -1 : 1);
        } else {
            return new Vector3D(0, 1, 0);
        }
    }
}