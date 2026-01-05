package graphics.renderer;

import graphics.shadows.ShadowCube;
import core.math.Vector3D;
import scene.gameObjects.PointLight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShadowLightSystem {
    private final Map<PointLight, ShadowCube> lightToShadowCube = new HashMap<>();

    public ShadowLightSystem(List<PointLight> lights) {
        for (PointLight light : lights) {
            ShadowCube shadowCube = new ShadowCube(light.getTransform().getPosition());
            lightToShadowCube.put(light, shadowCube);
        }
    }

    public Map<PointLight, ShadowCube> getLightToShadowCube() {
        updateShadowCube();

        return lightToShadowCube;
    }

    private void updateShadowCube() {
        for (Map.Entry<PointLight, ShadowCube> entry : lightToShadowCube.entrySet()) {
            PointLight light = entry.getKey();
            ShadowCube cube = entry.getValue();

            Vector3D lightPos = light.getTransform().getPosition();
            cube.updatePosition(lightPos);
        }
    }
}
