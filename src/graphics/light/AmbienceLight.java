package graphics.light;

import graphics.utils.ColorUtils;

import java.awt.Color;

public class AmbienceLight {
    private final double intensity;
    private final Color color;

    public AmbienceLight(double intensity, Color color) {
        this.intensity = intensity;
        this.color = color;
    }

    public Color applyAmbienceLightToTriangles(Color originalColor) {
        return ColorUtils.applyBrightnessWithColoredLight(originalColor, color, intensity);
    }
}