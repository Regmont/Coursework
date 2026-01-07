package graphics.utils;

import scene.gameObjects.Sky;

import java.awt.*;

/**
 * Утилиты для работы с цветами в рендерере.
 * <p>
 * Предоставляет методы для преобразования цветов с учетом яркости и освещения.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class ColorUtils {
    /**
     * Возвращает цвет неба с учетом его яркости.
     *
     * @param sky объект неба, содержащий цвет и яркость
     * @return цвет неба, скорректированный по яркости
     */
    public static Color getSkyColor(Sky sky) {
        return applyBrightness(sky.getColor(), sky.getBrightness());
    }

    /**
     * Применяет яркость с учетом цветного источника света.
     * <p>
     * Каждый цветовой канал умножается на яркость:
     * <pre>
     *   originalChannel * brightness * (lightChannel / 255.0)
     * </pre>
     * Результат ограничивается диапазоном [0, 255].
     *
     * @param originalColor исходный цвет поверхности
     * @param lightColor    цвет источника света
     * @param brightness    яркость (0.0 - темнота, 1.0 - полная яркость)
     * @return результирующий цвет
     */
    public static Color applyBrightnessWithColoredLight(Color originalColor, Color lightColor, double brightness) {
        int r = (int)(originalColor.getRed() * brightness * lightColor.getRed() / 255.0);
        int g = (int)(originalColor.getGreen() * brightness * lightColor.getGreen() / 255.0);
        int b = (int)(originalColor.getBlue() * brightness * lightColor.getBlue() / 255.0);

        r = Math.min(255, Math.max(0, r));
        g = Math.min(255, Math.max(0, g));
        b = Math.min(255, Math.max(0, b));

        return new Color(r, g, b);
    }

    /**
     * Применяет яркость к цвету.
     * <p>
     * Каждый цветовой канал умножается на brightness.
     * Результат ограничивается диапазоном [0, 255].
     *
     * @param color исходный цвет
     * @param brightness яркость (0.0 - темнота, 1.0 - полная яркость)
     * @return затемненный/осветленный цвет
     */
    public static Color applyBrightness(Color color, double brightness) {
        int r = (int)(color.getRed() * brightness);
        int g = (int)(color.getGreen() * brightness);
        int b = (int)(color.getBlue() * brightness);

        r = Math.min(255, Math.max(0, r));
        g = Math.min(255, Math.max(0, g));
        b = Math.min(255, Math.max(0, b));

        return new Color(r, g, b);
    }
}
