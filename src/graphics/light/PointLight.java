package graphics.light;

import game.configuration.ColorConfiguration;
import geometry.Triangle;
import graphics.renderer.Material;
import graphics.shadows.ShadowCube;
import math.Vector3D;
import graphics.utils.ColorUtils;
import graphics.shadows.ShadowCubeFace;
import scene.ObjectInstance;

import java.awt.*;

public class PointLight {
    private final Vector3D worldPosition;
    private final Color color;
    private final double intensity;
    private final ShadowCube shadowCube;
    private ObjectInstance objectInstance;

    public PointLight(Vector3D worldPosition, Color color, double intensity, ObjectInstance objectInstance) {
        this.worldPosition = worldPosition;
        this.color = color;
        this.intensity = intensity;
        this.objectInstance = objectInstance;

        this.objectInstance.setPosition(worldPosition);
        this.objectInstance.getMesh().setMaterial(new Material(color, true));

        shadowCube = new ShadowCube(worldPosition);
    }

    public PointLight(Vector3D worldPosition, Color color, double intensity) {
        this.worldPosition = worldPosition;
        this.color = color;
        this.intensity = intensity;

        shadowCube = new ShadowCube(worldPosition);
    }

    public ShadowCubeFace getShadowFace(int index) {
        return shadowCube.getFace(index);
    }

    public Vector3D getWorldPosition() {
        return worldPosition;
    }

    public Color getColor() {
        return color;
    }

    public Color calculateLightContribution(Triangle triangle, Vector3D point) {
        Vector3D normal = triangle.getWorldNormal();

        if (normal == null) {
            return ColorConfiguration.NO_LIGHT_CONTRIBUTION;
        }

        Vector3D lightDir = worldPosition.subtract(point).normalize();
        double lambert = Math.max(0.0, normal.dot(lightDir));

        if (lambert <= 0.0) {
            return ColorConfiguration.NO_LIGHT_CONTRIBUTION;
        }

        double distance = point.subtract(worldPosition).length();
        double attenuation = 1.0 / (1.0 + 0.1 * distance + 0.01 * distance * distance);

        double lightAmount = lambert * intensity * attenuation;

        return ColorUtils.applyBrightnessWithColoredLight(ColorConfiguration.FULL_LIGHT, color, lightAmount);
    }

    public int getCubeFaceIndex(Vector3D worldPoint) {
        Vector3D dir = worldPoint.subtract(worldPosition).normalize();

        double x = dir.getX();
        double y = dir.getY();
        double z = dir.getZ();

        double absX = Math.abs(x);
        double absY = Math.abs(y);
        double absZ = Math.abs(z);

        if (absX >= absY && absX >= absZ) {
            return x > 0 ? 0 : 1;
        } else if (absY >= absZ) {
            return y > 0 ? 2 : 3;
        } else {
            return z > 0 ? 4 : 5;
        }
    }

    public ShadowCube getShadowCube() {
        return shadowCube;
    }

    public boolean hasObjectInstance() {
        return objectInstance != null;
    }

    public ObjectInstance getObjectInstance() {
        return objectInstance;
    }
}