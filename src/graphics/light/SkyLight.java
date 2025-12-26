package graphics.light;

import graphics.utils.ColorUtils;

import java.awt.*;

public class SkyLight {
    private Color skyColor;
    private double skyBrightness;

    public SkyLight(Color color, double brightness) {
        this.skyColor = color;
        this.skyBrightness = brightness;
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
