package graphics.light;

import geometry.Triangle;
import graphics.utils.ColorUtils;
import math.Vector3D;

import java.awt.*;

public record PointLight(Vector3D worldPosition, Color color, double intensity) {

    public Color applyLightToTriangle(Color currentColor, Triangle triangle) {
        Vector3D normal = triangle.getWorldNormal();
        if (normal == null) {
            return currentColor;
        }

        Vector3D[] worldPoints = triangle.getOriginalPoints();
        if (worldPoints == null) {
            return currentColor;
        }

        Vector3D triangleCenter = new Vector3D(
                (worldPoints[0].getX() + worldPoints[1].getX() + worldPoints[2].getX()) / 3.0,
                (worldPoints[0].getY() + worldPoints[1].getY() + worldPoints[2].getY()) / 3.0,
                (worldPoints[0].getZ() + worldPoints[1].getZ() + worldPoints[2].getZ()) / 3.0
        );

        Vector3D lightDir = worldPosition.subtract(triangleCenter).normalize();

        double lambert = Math.max(0.0, normal.dot(lightDir));

        if (lambert <= 0.0) {
            return currentColor;
        }

        double distance = triangleCenter.subtract(worldPosition).length();
        double attenuation = 1.0 / (1.0 + 0.1 * distance + 0.01 * distance * distance);

        double lightAmount = lambert * intensity * attenuation;

        Color lightColor = ColorUtils.applyBrightnessWithColoredLight(Color.WHITE, color, lightAmount);

        return blendColors(currentColor, lightColor);
    }

    private Color blendColors(Color base, Color light) {
        int r = Math.min(255, base.getRed() + light.getRed());
        int g = Math.min(255, base.getGreen() + light.getGreen());
        int b = Math.min(255, base.getBlue() + light.getBlue());

        return new Color(r, g, b);
    }
}