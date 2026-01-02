package graphics.light;

import game.configuration.GameConfig;
import graphics.utils.ColorUtils;

import java.awt.Color;

public class AmbienceLight {
    private final double intensity;
    private final Color color;

    public AmbienceLight() {
        this.color = GameConfig.AMBIENCE_COLOR;
        this.intensity = GameConfig.AMBIENCE_INTENSITY;
    }

    public Color applyAmbienceLightToTriangles(Color originalColor) {
        return ColorUtils.applyBrightnessWithColoredLight(originalColor, color, intensity);
    }
}