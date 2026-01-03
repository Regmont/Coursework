package graphics.light;

import game.configuration.GameConfig;
import geometry.Triangle;
import graphics.renderer.Material;
import math.Vector3D;
import graphics.utils.ShadowUtils;

import java.awt.Color;
import java.util.List;

public class LightCalculator {
    private static final AmbienceLight ambienceLight = new AmbienceLight();

    public static Color calculatePixelColor(Color baseColor, Triangle triangle,
                                            Vector3D worldPos, List<PointLight> lights) {
        Material material = triangle.getMaterial();

        if (material.isTransparentForLight()) {
            return material.getColor();
        }

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
        Color shadowColor = GameConfig.SHADOW_COLOR;
        double darkness = GameConfig.SHADOW_DARKNESS_FACTOR;
        double colorMix = GameConfig.SHADOW_COLOR_MIX;

        int r1 = (int)(color.getRed() * (1 - darkness));
        int g1 = (int)(color.getGreen() * (1 - darkness));
        int b1 = (int)(color.getBlue() * (1 - darkness));

        int r2 = (int)(shadowColor.getRed() * colorMix + r1 * (1 - colorMix));
        int g2 = (int)(shadowColor.getGreen() * colorMix + g1 * (1 - colorMix));
        int b2 = (int)(shadowColor.getBlue() * colorMix + b1 * (1 - colorMix));

        r2 = Math.min(255, Math.max(0, r2));
        g2 = Math.min(255, Math.max(0, g2));
        b2 = Math.min(255, Math.max(0, b2));

        return new Color(r2, g2, b2);
    }
}