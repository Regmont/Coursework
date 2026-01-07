package scene;

import scene.config.ColorConfig;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Класс материала для 3D объектов.
 * <p>
 * Определяет визуальные свойства поверхности: цвет, текстуру и поведение при отбрасывании теней.
 * Материал может быть либо цветным (solid color), либо текстурным, но не одновременно.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class Material {
    /** Материал по умолчанию: белый, отбрасывает тени. */
    public static final Material DEFAULT_MATERIAL = new Material(Color.WHITE, true);

    private final Color color;
    private BufferedImage texture;
    private boolean castsShadows = true;

    /**
     * Создает цветной материал без текстуры.
     *
     * @param color         цвет материала
     * @param castsShadows  {@code true} если материал отбрасывает тени (участвует в shadow mapping)
     */
    public Material(Color color, boolean castsShadows) {
        this.color = color;
        this.castsShadows = castsShadows;
        texture = null;
    }

    /**
     * Создает материал с текстурой.
     * <p>
     * Если текстура равна {@code null}, используется цвет {@link ColorConfig#BROKEN_MODEL_COLOR}.
     * Текстурные материалы по умолчанию отбрасывают тени.
     *
     * @param texture изображение текстуры или {@code null}
     */
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

    /**
     * Проверяет, является ли материал текстурным.
     *
     * @return {@code true} если материал имеет текстуру
     */
    public boolean hasTexture() {
        return texture != null;
    }

    /**
     * Проверяет, отбрасывает ли материал тени.
     * <p>
     * Если {@code false}, материал игнорируется при построении карт теней (shadow maps).
     *
     * @return {@code true} если материал отбрасывает тени
     */
    public boolean castsShadows() {
        return castsShadows;
    }

    @Override
    public String toString() {
        if (hasTexture()) {
            return String.format(
                    "Material[texture=%dx%d, castsShadows=%b]",
                    texture.getWidth(),
                    texture.getHeight(),
                    castsShadows
            );
        } else {
            assert color != null;
            String colorHex = String.format("#%06X", color.getRGB() & 0xFFFFFF);
            return String.format(
                    "Material[color=%s, castsShadows=%b]",
                    colorHex,
                    castsShadows
            );
        }
    }
}
