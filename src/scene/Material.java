package scene;

import scene.config.ColorConfig;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Material {
    public static final Material DEFAULT_MATERIAL = new Material(Color.WHITE, false);

    private final Color color;
    private BufferedImage texture;
    private boolean transparentForLight = false;

    public Material(Color color, boolean transparentForLight) {
        this.color = color;
        this.transparentForLight = transparentForLight;
        texture = null;
    }

    public Material(BufferedImage texture) {
        if (texture == null) {
            color = ColorConfig.BROKEN_MODEL_COLOR;
        }
        else {
            color = null;
            this.texture = texture;
        }
    }

    public Color getColor() {
        return color;
    }

    public BufferedImage getTexture() {
        return texture;
    }

    public boolean hasTexture() {
        return texture != null;
    }

    public boolean isTransparentForLight() {
        return transparentForLight;
    }
}