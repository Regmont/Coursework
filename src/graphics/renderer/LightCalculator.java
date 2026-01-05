package graphics.renderer;

import core.math.Triangle;
import core.math.Vector3D;
import scene.Material;
import scene.RenderableTriangle;
import scene.gameObjects.AmbienceLight;
import scene.gameObjects.PointLight;
import graphics.utils.ColorUtils;
import graphics.utils.LightUtils;
import graphics.utils.ShadowUtils;
import graphics.config.RenderingConfig;

import java.awt.Color;

public class LightCalculator {
    public static Color calculatePixelColor(Color baseColor, RenderableTriangle triangle,
                                            Vector3D worldPos, ShadowLightSystem shadowLightSystem,
                                            AmbienceLight ambienceLight) {
        Material material = triangle.getMaterial();

        if (material.isTransparentForLight()) {
            return material.getColor();
        }

        Color color = ColorUtils.applyBrightnessWithColoredLight(baseColor,
                ambienceLight.getColor(), ambienceLight.getIntensity());

        boolean inAnyShadow = false;

        for (PointLight light : shadowLightSystem.getLightToShadowCube().keySet()) {
            boolean isBackFacingToLight = isTriangleBackFacingToLight(triangle, light);

            if (isBackFacingToLight) {
                inAnyShadow = true;

                continue;
            }

            if (ShadowUtils.isPointInShadow(worldPos, light, shadowLightSystem.getLightToShadowCube().get(light))) {
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

    private static boolean isTriangleBackFacingToLight(RenderableTriangle triangle, PointLight light) {
        Vector3D triangleCenter = Triangle.getTriangleCenter(triangle.getOriginalTriangle());
        Vector3D toLight = light.getTransform().getPosition().subtract(triangleCenter).normalize();
        Vector3D normal = triangle.getWorldNormal();

        return normal.dot(toLight) <= 0.0;
    }

    private static Color addLightContribution(Color currentColor, PointLight light,
                                              RenderableTriangle triangle, Vector3D worldPos) {
        Color lightContribution = LightUtils.calculateLightContribution(light, triangle, worldPos);
        
        return blendAdditive(currentColor, lightContribution);
    }

    private static Color blendAdditive(Color firstColor, Color secondColor) {
        int r = Math.min(255, firstColor.getRed() + secondColor.getRed());
        int g = Math.min(255, firstColor.getGreen() + secondColor.getGreen());
        int b = Math.min(255, firstColor.getBlue() + secondColor.getBlue());

        return new Color(r, g, b);
    }

    private static Color darkenColor(Color color) {
        Color shadowColor = RenderingConfig.SHADOW_COLOR;
        double darkness = RenderingConfig.SHADOW_DARKNESS_FACTOR;
        double colorMix = RenderingConfig.SHADOW_COLOR_MIX;

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