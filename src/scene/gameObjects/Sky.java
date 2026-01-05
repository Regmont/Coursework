package scene.gameObjects;

import scene.config.SceneConfig;

import java.awt.*;

public class Sky {
    public static final Sky DEFAULT_SKY = new Sky(SceneConfig.BACKGROUND_COLOR, SceneConfig.BACKGROUND_BRIGHTNESS);

    private Color color;
    private double brightness;

    public Sky(Color color, double brightness) {
        this.color = color;
        this.brightness = brightness;
    }

    public Color getColor() {
        return color;
    }

    public double getBrightness() {
        return brightness;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setBrightness(double brightness) {
        this.brightness = brightness;
    }
}
