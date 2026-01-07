package scene.gameObjects;

import scene.config.SceneConfig;

import java.awt.*;

/**
 * Класс, представляющий небо/фон сцены.
 * <p>
 * Определяет цвет фона для рендеринга.
 * Яркость используется для настройки визуального восприятия.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class Sky {
    /** Небо по умолчанию из настроек сцены. */
    public static final Sky DEFAULT_SKY = new Sky(SceneConfig.BACKGROUND_COLOR, SceneConfig.BACKGROUND_BRIGHTNESS);

    private Color color;
    private double brightness;

    /**
     * Создает небо с указанным цветом и яркостью.
     *
     * @param color         цвет неба (используется как цвет фона рендера)
     * @param brightness    коэффициент яркости для визуальной настройки
     *                      (0.0 - отсутствие яркости, 1.0 - максимальная яркость)
     */
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

    @Override
    public String toString() {
        String hexColor = String.format("#%06X", color.getRGB() & 0xFFFFFF);
        return String.format(
                "Sky[color=%s, brightness=%.2f]",
                hexColor,
                brightness
        );
    }
}
