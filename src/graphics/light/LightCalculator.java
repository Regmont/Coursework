package graphics.light;

import game.configuration.RenderingConfig;
import geometry.Triangle;
import math.Vector3D;
import graphics.utils.ShadowUtils;

import java.awt.Color;
import java.util.List;

public class LightCalculator {
    private static final AmbienceLight ambienceLight = new AmbienceLight();

    public static Color calculatePixelColor(Color baseColor, Triangle triangle,
                                            Vector3D worldPos, List<PointLight> lights) {
        Color color = ambienceLight.applyAmbienceLightToTriangles(baseColor);

        boolean inAnyShadow = false;

        for (PointLight light : lights) {
            boolean isBackFacingToLight = isTriangleBackFacingToLight(triangle, light);

            if (isBackFacingToLight) {
                inAnyShadow = true;
                continue;
            }

            if (ShadowUtils.isPointInShadow(worldPos, light)) {
                inAnyShadow = true;
            } else {
                color = addLightContribution(color, light, triangle, worldPos);
            }
        }

        if (inAnyShadow) {
            color = darkenColor(color);
        }

        return color;
    }

    private static boolean isTriangleBackFacingToLight(Triangle triangle, PointLight light) {
        Vector3D triangleCenter = triangle.getCenter();
        Vector3D toLight = light.getWorldPosition().subtract(triangleCenter).normalize();
        Vector3D normal = triangle.getWorldNormal();

        return normal.dot(toLight) <= 0.0;
    }

    private static Color addLightContribution(Color currentColor, PointLight light,
                                              Triangle triangle, Vector3D worldPos) {
        Color lightContribution = light.calculateLightContribution(triangle, worldPos);
        
        return blendAdditive(currentColor, lightContribution);
    }

    private static Color blendAdditive(Color firstColor, Color secondColor) {
        int r = Math.min(255, firstColor.getRed() + secondColor.getRed());
        int g = Math.min(255, firstColor.getGreen() + secondColor.getGreen());
        int b = Math.min(255, firstColor.getBlue() + secondColor.getBlue());

        return new Color(r, g, b);
    }

    private static Color darkenColor(Color color) {
        double factor = 1 - RenderingConfig.SHADOW_DARKNESS_FACTOR;

        int r = (int)(color.getRed() * factor);
        int g = (int)(color.getGreen() * factor);
        int b = (int)(color.getBlue() * factor);

        return new Color(r, g, b);
    }
}