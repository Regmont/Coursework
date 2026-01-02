package graphics.light;

import game.configuration.GameConfig;
import graphics.utils.ColorUtils;

import java.awt.*;

public class SkyLight {
    private Color skyColor;
    private double skyBrightness;

    public SkyLight() {
        this.skyColor = GameConfig.BACKGROUND_COLOR;
        this.skyBrightness = GameConfig.BACKGROUND_BRIGHTNESS;
    }

    public Color getSkyColor() {
        return ColorUtils.applyBrightness(skyColor, skyBrightness);
    }

    public void setSkyColor(Color color) {
        this.skyColor = color;
    }

    public void setSkyBrightness(double brightness) {
        this.skyBrightness = brightness;
    }
}
