package graphics.shadows;

import game.configuration.RenderingConfig;
import graphics.CameraBase;
import math.Vector3D;

public class ShadowCamera extends CameraBase {
    public ShadowCamera(Vector3D position, Vector3D rotation) {
        super(position, rotation);
        fov = RenderingConfig.SHADOW_CAMERA_FOV;
    }
}