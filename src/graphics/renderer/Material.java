package graphics.renderer;

import java.awt.*;
import java.awt.image.BufferedImage;

public class Material {
    private static final Material defaultMaterial = new Material(Color.WHITE);
    private final Color color;
    private final BufferedImage texture;

    public Material(Color color) {
        this.color = color;
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

    public static Material getDefaultMaterial() {
        return defaultMaterial;
    }
}