package graphics.renderer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Material {
    public static final Material DEFAULT_MATERIAL = new Material(Color.WHITE, false);

    private final Color color;
    private final BufferedImage texture;
    private boolean transparentForLight;

    public Material(Color color, boolean transparentForLight) {
        this.color = color;
        this.transparentForLight = transparentForLight;
        this.texture = null;
    }

    public Material(BufferedImage texture) {
        this.color = null;
        this.texture = texture;
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