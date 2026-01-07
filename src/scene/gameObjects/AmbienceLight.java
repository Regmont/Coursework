package scene.gameObjects;

import scene.config.SceneConfig;

import java.awt.Color;

public class AmbienceLight {
    public static final AmbienceLight DEFAULT_AMBIENCE_LIGHT =
            new AmbienceLight(SceneConfig.AMBIENCE_COLOR, SceneConfig.AMBIENCE_INTENSITY);

    private Color color;
    private double intensity;

    public AmbienceLight(Color color, double intensity) {
        this.color = color;
        this.intensity = intensity;
    }

    public Color getColor() {
        return color;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }
}