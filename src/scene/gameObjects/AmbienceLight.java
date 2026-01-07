package scene.gameObjects;

import scene.config.SceneConfig;

import java.awt.Color;

/**
 * Класс, представляющий окружающий (ambient) свет в сцене.
 * <p>
 * Окружающий свет равномерно освещает все объекты в сцене независимо от их положения.
 * Используется для базового освещения.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class AmbienceLight {
    /** Окружающий свет по умолчанию из настроек сцены. */
    public static final AmbienceLight DEFAULT_AMBIENCE_LIGHT =
            new AmbienceLight(SceneConfig.AMBIENCE_COLOR, SceneConfig.AMBIENCE_INTENSITY);

    private Color color;
    private double intensity;

    /**
     * Создает источник окружающего света.
     *
     * @param color цвет окружающего света
     * @param intensity интенсивность (0.0 - нет света, 1.0 - максимальная интенсивность)
     */
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

    @Override
    public String toString() {
        String hexColor = String.format("#%06X", color.getRGB() & 0xFFFFFF);
        return String.format(
                "AmbienceLight[color=%s, intensity=%.2f]",
                hexColor,
                intensity
        );
    }
}
