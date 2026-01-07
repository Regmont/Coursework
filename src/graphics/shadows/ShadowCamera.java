package graphics.shadows;

import graphics.config.RenderingConfig;
import core.base.CameraBase;
import core.math.Vector3D;

public class ShadowCamera extends CameraBase {
    public ShadowCamera(Vector3D position, Vector3D rotation) {
        super(position, rotation);
        fov = RenderingConfig.SHADOW_CAMERA_FOV;
    }
}
