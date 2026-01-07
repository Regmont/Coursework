package graphics.utils;

import scene.RenderableTriangle;
import scene.gameObjects.PointLight;
import core.math.Vector3D;

import java.awt.*;

/**
 * Утилиты для вычисления освещения в рендерере.
 * <p>
 * Содержит методы для расчета вклада точечных источников света
 * и определения граней cube shadow map.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class LightUtils {
    /**
     * Вычисляет вклад точечного источника света в точку поверхности.
     * <p>
     * Использует модель Ламберта (Lambertian/diffuse освещение) с затуханием:
     * <pre>
     *   contribution = max(0, normal·lightDir) * intensity * attenuation
     *   attenuation = 1 / (1 + 0.1*d + 0.01*d²)
     * </pre>
     * Где:
     * <ul>
     *   <li>normal — нормаль поверхности в точке</li>
     *   <li>lightDir — направление от точки к источнику света</li>
     *   <li>d — расстояние от точки до источника света</li>
     * </ul>
     *
     * @param pointLight    точечный источник света
     * @param triangle      треугольник, содержащий точку
     * @param point         мировая позиция точки на поверхности
     * @return цвет вклада света (может быть {@link Color#BLACK} если нет освещения)
     */
    public static Color calculateLightContribution(PointLight pointLight, RenderableTriangle triangle, Vector3D point) {
        Vector3D normal = triangle.getWorldNormal();
        Color noLightContribution = Color.BLACK;

        if (normal == null) {
            return noLightContribution;
        }

        Vector3D lightDir = pointLight.getTransform().getPosition().subtract(point).normalize();
        double lambert = Math.max(0.0, normal.dot(lightDir));

        if (lambert <= 0.0) {
            return noLightContribution;
        }

        double distance = point.subtract(pointLight.getTransform().getPosition()).length();
        double attenuation = 1.0 / (1.0 + 0.1 * distance + 0.01 * distance * distance);

        double lightAmount = lambert * pointLight.getIntensity() * attenuation;

        Color fullLight = Color.WHITE;

        return ColorUtils.applyBrightnessWithColoredLight(fullLight, pointLight.getColor(), lightAmount);
    }

    /**
     * Определяет индекс грани cube shadow map для точки относительно источника света.
     * <p>
     * Выбирает грань, в направлении которой находится точка.
     * Определяется по максимальной по модулю компоненте вектора направления:
     * <ul>
     *   <li>0: +X грань</li>
     *   <li>1: -X грань</li>
     *   <li>2: +Y грань</li>
     *   <li>3: -Y грань</li>
     *   <li>4: +Z грань</li>
     *   <li>5: -Z грань</li>
     * </ul>
     *
     * @param pointLight точечный источник света
     * @param worldPoint мировая позиция точки
     * @return индекс грани cube map (0-5)
     */
    public static int getCubeFaceIndex(PointLight pointLight, Vector3D worldPoint) {
        Vector3D dir = worldPoint.subtract(pointLight.getTransform().getPosition()).normalize();

        double x = dir.x();
        double y = dir.y();
        double z = dir.z();

        double absX = Math.abs(x);
        double absY = Math.abs(y);
        double absZ = Math.abs(z);

        if (absX >= absY && absX >= absZ) {
            return x > 0 ? 0 : 1;
        } else if (absY >= absZ) {
            return y > 0 ? 2 : 3;
        } else {
            return z > 0 ? 4 : 5;
        }
    }
}
