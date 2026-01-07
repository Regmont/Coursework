package scene.gameObjects;

import core.base.ObjectInstance;
import core.math.Vector3D;
import scene.Material;
import scene.config.SceneConfig;

import java.awt.*;

/**
 * Класс, представляющий точечный источник света в сцене.
 * <p>
 * Точечный свет излучает во всех направлениях из своей позиции.
 * Может быть визуализирован как 3D объект (например, сфера) для отладки.
 * <p>
 * Наследует трансформацию от {@link ObjectInstance} для позиционирования в пространстве.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class PointLight extends ObjectInstance {
    private SimpleObject object;
    private Color color;
    private double intensity;

    /**
     * Создает точечный источник света в указанной позиции.
     * <p>
     * Использует цвет и интенсивность по умолчанию из {@link SceneConfig}.
     *
     * @param position позиция источника света в мировых координатах
     */
    public PointLight(Vector3D position) {
        super(position, Vector3D.zeroVector, Vector3D.oneVector);

        color = SceneConfig.POINT_LIGHT_COLOR;
        intensity = SceneConfig.POINT_LIGHT_INTENSITY;
    }

    /**
     * Проверяет, имеет ли свет визуальное представление (3D объект).
     *
     * @return {@code true} если свет имеет ассоциированный объект для отображения
     */
    public boolean hasObject() {
        return object != null;
    }

    public SimpleObject getObject() {
        return object;
    }
    public Color getColor() {
        return color;
    }
    public double getIntensity() {
        return intensity;
    }

    /**
     * Устанавливает визуальное представление для света.
     * <p>
     * Объект позиционируется в той же точке, что и свет, и получает материал
     * цвета света, который не отбрасывает тени.
     *
     * @param object 3D объект для визуализации света
     */
    public void setObject(SimpleObject object) {
        this.object = object;
        this.object.getTransform().setPosition(getTransform().getPosition());
        this.object.getMesh().setMaterial(new Material(color, false));
    }

    /**
     * Устанавливает новый цвет света.
     * <p>
     * Если у света есть визуальное представление, его материал также обновляется.
     *
     * @param color новый цвет света
     */
    public void setColor(Color color) {
        this.color = color;

        if (object != null) {
            object.getMesh().setMaterial(new Material(color, false));
        }
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }

    @Override
    public String toString() {
        Vector3D pos = getTransform().getPosition();
        String hexColor = String.format("#%06X", color.getRGB() & 0xFFFFFF);
        return String.format(
                "PointLight[position=%s, color=%s, intensity=%.2f, hasObject=%b]",
                pos,
                hexColor,
                intensity,
                hasObject()
        );
    }
}
