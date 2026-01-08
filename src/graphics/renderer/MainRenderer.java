package graphics.renderer;

import graphics.shadows.ShadowRenderer;
import scene.SceneSystem;
import graphics.shadows.ShadowLightSystem;
import scene.Mesh;
import scene.gameObjects.PointLight;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

/**
 * Главный рендерер, координирующий процесс отрисовки всей сцены.
 * <p>
 * Управляет пайплайном рендеринга: очистка буферов, преобразование сцены, построение теней и отрисовка треугольников.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class MainRenderer {
    /**
     * Рендерит всю сцену в указанные буферы.
     * <p>
     * Процесс рендеринга:
     * <ol>
     *   <li>Очистка цветового и глубинного буферов</li>
     *   <li>Преобразование объектов сцены в экранные координаты</li>
     *   <li>Построение карт теней для источников света</li>
     *   <li>Отрисовка всех треугольников с учетом освещения и теней</li>
     * </ol>
     *
     * @param colorBuffer       цветовой буфер размером [width][height]
     * @param depthBuffer       глубинный буфер (z-buffer) размером [width][height]
     * @param backgroundColor   цвет фона для очистки
     * @param sceneSystem       система сцены с объектами, освещением и камерой
     */
    public static void renderScene(Color[][] colorBuffer, double[][] depthBuffer,
                                   Color backgroundColor, SceneSystem sceneSystem) {
        clearBuffers(colorBuffer, depthBuffer, backgroundColor);

        int width = colorBuffer.length;
        int height = colorBuffer[0].length;

        List<Mesh> meshes = SceneTransformer.getTransformedMeshes(sceneSystem, width, height);
        List<PointLight> pointLights = sceneSystem.getPointLights();

        ShadowLightSystem shadowLightSystem = new ShadowLightSystem(pointLights);

        ShadowRenderer.renderShadowMaps(shadowLightSystem, sceneSystem.getObjects());
        TriangleRenderer.renderTriangles(meshes, colorBuffer, depthBuffer, width, height, shadowLightSystem,
                sceneSystem.getAmbienceLight());
    }

    /**
     * Очищает цветовой и глубинный буферы.
     * <p>
     * Глубинный буфер заполняется значением {@link Double#POSITIVE_INFINITY}.
     * Цветовой буфер заполняется указанным цветом фона.
     *
     * @param colorBuffer цветовой буфер для очистки
     * @param depthBuffer глубинный буфер для очистки
     * @param backgroundColor цвет для заполнения цветового буфера
     */
    private static void clearBuffers(Color[][] colorBuffer, double[][] depthBuffer, Color backgroundColor) {
        for (double[] row : depthBuffer) {
            Arrays.fill(row, Double.POSITIVE_INFINITY);
        }

        for (Color[] colors : colorBuffer) {
            Arrays.fill(colors, backgroundColor);
        }
    }
}
