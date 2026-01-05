package scene;

import game.configuration.GameConfig;
import graphics.CameraBase;
import math.Vector3D;

public class Camera extends CameraBase {
    public static final Camera DEFAULT_CAMERA = new Camera(GameConfig.INITIAL_CAMERA_POSITION,
            GameConfig.INITIAL_CAMERA_ROTATION);

    private double speed;

    public Camera(Vector3D position, Vector3D rotation) {
        super(position, rotation);
        this.speed = GameConfig.CAMERA_SPEED;
        fov = GameConfig.CAMERA_FOV;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }
}