package graphics.utils;

import geometry.Triangle;
import scene.PointLight;
import math.Vector3D;

import java.awt.*;

public class LightUtils {
    public static Color calculateLightContribution(PointLight pointLight, Triangle triangle, Vector3D point) {
        Vector3D normal = triangle.getWorldNormal();
        Color noLightContribution = Color.BLACK;

        if (normal == null) {
            return noLightContribution;
        }

        Vector3D lightDir = pointLight.getTransform().getPosition().subtract(point).normalize();
        double lambert = Math.max(0.0, normal.dot(lightDir));

        if (lambert <= 0.0) {
            return noLightContribution;
        }

        double distance = point.subtract(pointLight.getTransform().getPosition()).length();
        double attenuation = 1.0 / (1.0 + 0.1 * distance + 0.01 * distance * distance);

        double lightAmount = lambert * pointLight.getIntensity() * attenuation;

        Color fullLight = Color.WHITE;

        return ColorUtils.applyBrightnessWithColoredLight(fullLight, pointLight.getColor(), lightAmount);
    }

    public static int getCubeFaceIndex(PointLight pointLight, Vector3D worldPoint) {
        Vector3D dir = worldPoint.subtract(pointLight.getTransform().getPosition()).normalize();

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
}
