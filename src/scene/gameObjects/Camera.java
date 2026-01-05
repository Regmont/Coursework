package scene.gameObjects;

import core.base.CameraBase;
import core.math.Vector3D;
import scene.config.SceneConfig;

public class Camera extends CameraBase {
    public static final Camera DEFAULT_CAMERA = new Camera(SceneConfig.INITIAL_CAMERA_POSITION,
            SceneConfig.INITIAL_CAMERA_ROTATION);

    private double speed;

    public Camera(Vector3D position, Vector3D rotation) {
        super(position, rotation);
        this.speed = SceneConfig.CAMERA_SPEED;
        fov = SceneConfig.CAMERA_FOV;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}