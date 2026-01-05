package graphics.utils;

import scene.Sky;

import java.awt.*;

public class ColorUtils {
    public static Color getSkyColor(Sky sky) {
        return applyBrightness(sky.getColor(), sky.getBrightness());
    }

    public static Color applyBrightnessWithColoredLight(Color originalColor, Color lightColor, double brightness) {
        int r = (int)(originalColor.getRed() * brightness * lightColor.getRed() / 255.0);
        int g = (int)(originalColor.getGreen() * brightness * lightColor.getGreen() / 255.0);
        int b = (int)(originalColor.getBlue() * brightness * lightColor.getBlue() / 255.0);

        r = Math.min(255, Math.max(0, r));
        g = Math.min(255, Math.max(0, g));
        b = Math.min(255, Math.max(0, b));

        return new Color(r, g, b);
    }

    public static Color applyBrightness(Color color, double brightness) {
        int r = (int)(color.getRed() * brightness);
        int g = (int)(color.getGreen() * brightness);
        int b = (int)(color.getBlue() * brightness);

        r = Math.min(255, Math.max(0, r));
        g = Math.min(255, Math.max(0, g));
        b = Math.min(255, Math.max(0, b));

        return new Color(r, g, b);
    }
}
