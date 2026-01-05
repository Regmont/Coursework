package scene;

import game.configuration.GameConfig;

import java.awt.*;

public class Sky {
    public static final Sky DEFAULT_SKY = new Sky(GameConfig.BACKGROUND_COLOR, GameConfig.BACKGROUND_BRIGHTNESS);

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
