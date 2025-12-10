package rendering.utils;

import java.awt.Color;

public class ColorUtils {

    public Color blendColors(Color oldColor, Color triColor, double alpha) {
        double inverseAlpha = 1.0 - alpha;

        double r = oldColor.getRed() * inverseAlpha + triColor.getRed() * alpha;
        double g = oldColor.getGreen() * inverseAlpha + triColor.getGreen() * alpha;
        double b = oldColor.getBlue() * inverseAlpha + triColor.getBlue() * alpha;

        int red = (int) Math.round(r);
        int green = (int) Math.round(g);
        int blue = (int) Math.round(b);

        red = Math.max(0, Math.min(255, red));
        green = Math.max(0, Math.min(255, green));
        blue = Math.max(0, Math.min(255, blue));

        return new Color(red, green, blue);
    }
}