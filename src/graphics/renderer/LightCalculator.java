package graphics.renderer;

import core.math.Triangle;
import core.math.Vector3D;
import graphics.shadows.ShadowLightSystem;
import scene.Material;
import scene.RenderableTriangle;
import scene.gameObjects.AmbienceLight;
import scene.gameObjects.PointLight;
import graphics.utils.ColorUtils;
import graphics.utils.LightUtils;
import graphics.utils.ShadowUtils;

import java.awt.Color;

/**
 * Калькулятор освещения для пикселей.
 * <p>
 * Вычисляет итоговый цвет пикселя с учетом:
 * <ul>
 *   <li>Базового цвета поверхности (текстура/материал)</li>
 *   <li>Окружающего (ambient) освещения</li>
 *   <li>Точечных источников света</li>
 *   <li>Теней (shadow mapping)</li>
 *   <li>Backface culling относительно источников света</li>
 * </ul>
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class LightCalculator {
    /**
     * Вычисляет итоговый цвет пикселя с учетом всего освещения.
     * <p>
     * Каждый источник света обрабатывается независимо:
     * <ol>
     *   <li>Применение окружающего освещения к базовому цвету</li>
     *   <li>Для каждого источника света:</li>
     *   <li>Проверка backface culling (если треугольник отвернут → пропуск)</li>
     *   <li>Проверка shadow map (если точка в тени от этого источника → пропуск)</li>
     *   <li>Добавление вклада света аддитивным смешиванием</li>
     * </ol>
     * Тени влияют только на конкретные источники: тень от одного источника
     * не блокирует свет от других источников.
     *
     * @param baseColor         базовый цвет поверхности (уже с текстурой)
     * @param triangle          треугольник, содержащий точку
     * @param worldPos          мировая позиция точки
     * @param shadowLightSystem система теней
     * @param ambienceLight     окружающее освещение
     * @return итоговый цвет пикселя
     */
    public static Color calculatePixelColor(Color baseColor, RenderableTriangle triangle,
                                            Vector3D worldPos, ShadowLightSystem shadowLightSystem,
                                            AmbienceLight ambienceLight) {
        Material material = triangle.getMaterial();

        if (!material.castsShadows()) {
            return material.getColor();
        }

        Color color = ColorUtils.applyBrightnessWithColoredLight(baseColor,
                ambienceLight.getColor(), ambienceLight.getIntensity());

        for (PointLight light : shadowLightSystem.getLightToShadowCube().keySet()) {
            boolean isBackFacingToLight = isTriangleBackFacingToLight(triangle, light);

            if (isBackFacingToLight) {
                continue;
            }

            if (ShadowUtils.isPointInShadow(worldPos, light, shadowLightSystem.getLightToShadowCube().get(light))) {
                continue;
            }

            color = addLightContribution(color, light, triangle, worldPos);
        }

        return color;
    }

    /**
     * Проверяет, отвернут ли треугольник от источника света.
     * <p>
     * Если нормаль треугольника направлена от света (dot ≤ 0),
     * треугольник считается неосвещенным с этой стороны.
     *
     * @param triangle  треугольник
     * @param light     источник света
     * @return {@code true} если треугольник отвернут от света
     */
    private static boolean isTriangleBackFacingToLight(RenderableTriangle triangle, PointLight light) {
        Vector3D triangleCenter = Triangle.getCenter(triangle.getOriginalTriangle());
        Vector3D toLight = light.getTransform().getPosition().subtract(triangleCenter).normalize();
        Vector3D normal = triangle.getWorldNormal();

        return normal.dot(toLight) <= 0.0;
    }

    /**
     * Добавляет вклад точечного источника света к текущему цвету.
     *
     * @param currentColor  текущий цвет (уже с ambient)
     * @param light         источник света
     * @param triangle      треугольник
     * @param worldPos      позиция точки
     * @return новый цвет с добавленным светом
     */
    private static Color addLightContribution(Color currentColor, PointLight light,
                                              RenderableTriangle triangle, Vector3D worldPos) {
        Color lightContribution = LightUtils.calculateLightContribution(light, triangle, worldPos);
        
        return blendAdditive(currentColor, lightContribution);
    }

    /**
     * Аддитивное смешивание цветов.
     * <p>
     * Каждый канал складывается, результат ограничивается [0, 255].
     * Эмулирует физическое сложение света.
     *
     * @param firstColor    первый цвет
     * @param secondColor   второй цвет
     * @return сумма цветов
     */
    private static Color blendAdditive(Color firstColor, Color secondColor) {
        int r = Math.min(255, firstColor.getRed() + secondColor.getRed());
        int g = Math.min(255, firstColor.getGreen() + secondColor.getGreen());
        int b = Math.min(255, firstColor.getBlue() + secondColor.getBlue());

        return new Color(r, g, b);
    }
}
