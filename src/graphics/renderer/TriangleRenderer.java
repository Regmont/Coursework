package graphics.renderer;

import core.math.Vector3D;
import graphics.shadows.ShadowLightSystem;
import scene.config.ColorConfig;
import scene.Material;
import scene.gameObjects.AmbienceLight;
import scene.Mesh;
import scene.RenderableTriangle;
import graphics.config.RenderingConfig;
import graphics.utils.GeometryUtils;

import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Основной растеризатор треугольников.
 * <p>
 * Реализует z-buffer алгоритм с пространственным разбиением (spatial grid)
 * для ускорения поиска треугольников. Поддерживает текстурирование,
 * освещение, тени и перспективно-корректную интерполяцию.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class TriangleRenderer {
    private static SpatialGrid spatialGrid;
    private static int lastWidth = -1;
    private static int lastHeight = -1;

    /**
     * Рендерит все треугольники в буферы.
     * <p>
     * Алгоритм:
     * <ol>
     *   <li>Создание/обновление пространственной сетки</li>
     *   <li>Распределение треугольников по ячейкам сетки</li>
     *   <li>Для каждого пикселя экрана:</li>
     *   <li>Получение треугольников из соответствующей ячейки</li>
     *   <li>Поиск ближайшего треугольника (z-buffer)</li>
     *   <li>Получение цвета (текстура или материал)</li>
     *   <li>Вычисление освещения с учетом теней</li>
     *   <li>Запись в цветовой и глубинный буферы</li>
     * </ol>
     *
     * @param meshes            список мешей для рендеринга
     * @param colorBuffer       цветовой буфер [width][height]
     * @param depthBuffer       глубинный буфер [width][height]
     * @param width             ширина области рендеринга
     * @param height            высота области рендеринга
     * @param shadowLightSystem система теней
     * @param ambienceLight     окружающее освещение
     */
    public static void renderTriangles(List<Mesh> meshes, Color[][] colorBuffer, double[][] depthBuffer,
                                       int width, int height, ShadowLightSystem shadowLightSystem,
                                       AmbienceLight ambienceLight) {
        updateSpatialGridIfSizeChanged(width, height);
        spatialGrid.clear();

        for (Mesh mesh : meshes) {
            for (RenderableTriangle triangle : mesh.triangles()) {
                spatialGrid.addTriangle(triangle);
            }
        }

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                List<RenderableTriangle> trianglesInCell = spatialGrid.getTriangles(x, y);

                if (trianglesInCell.isEmpty()) {
                    continue;
                }

                double centerX = x + 0.5;
                double centerY = y + 0.5;

                double closestDepth = Double.POSITIVE_INFINITY;
                Color closestColor = null;
                RenderableTriangle closestTriangle = null;

                for (RenderableTriangle triangle : trianglesInCell) {
                    if (!GeometryUtils.isPointIn3DTriangle(centerX, centerY, triangle.getCurrentTriangle())) {
                        continue;
                    }

                    double depth = GeometryUtils.calculateDepthAtPoint(centerX, centerY, triangle.getCurrentTriangle());

                    if (depth >= closestDepth) {
                        continue;
                    }

                    closestDepth = depth;
                    closestTriangle = triangle;

                    Material material = triangle.getMaterial();
                    closestColor = material.hasTexture() && triangle.hasUV() ?
                            getTextureColor(centerX, centerY, triangle) : material.getColor();
                }

                if (closestColor != null && closestDepth < depthBuffer[x][y]) {
                    Vector3D pixelWorldPos = GeometryUtils.getWorldPositionInTriangle(centerX, centerY,
                            closestTriangle);
                    Color finalColor = LightCalculator.calculatePixelColor(
                            closestColor, closestTriangle, pixelWorldPos, shadowLightSystem, ambienceLight
                    );

                    depthBuffer[x][y] = closestDepth;
                    colorBuffer[x][y] = finalColor;
                }
            }
        }
    }

    /**
     * Обновляет spatial grid при изменении размеров экрана.
     */
    private static void updateSpatialGridIfSizeChanged(int width, int height) {
        if (spatialGrid == null || width != lastWidth || height != lastHeight) {
            spatialGrid = new SpatialGrid(width, height);
            lastWidth = width;
            lastHeight = height;
        }
    }

    /**
     * Получает цвет текстуры в точке треугольника.
     * <p>
     * Использует перспективно-корректную интерполяцию UV-координат
     * с учетом invW (1/w) значений.
     *
     * @param x         экранная X-координата
     * @param y         экранная Y-координата
     * @param triangle  треугольник с UV-координатами
     * @return цвет текстуры или цвет ошибки
     */
    private static Color getTextureColor(double x, double y, RenderableTriangle triangle) {
        if (!triangle.hasUV()) {
            return ColorConfig.BROKEN_MODEL_COLOR;
        }

        List<Vector3D> points = triangle.getCurrentPoints();

        Vector3D A = points.get(0);
        Vector3D B = points.get(1);
        Vector3D C = points.get(2);

        double x1 = A.x(), y1 = A.y();
        double x2 = B.x(), y2 = B.y();
        double x3 = C.x(), y3 = C.y();

        double denom = (y2 - y3) * (x1 - x3) + (x3 - x2) * (y1 - y3);

        if (Math.abs(denom) < 1e-9) {
            return ColorConfig.BROKEN_MODEL_COLOR;
        }

        double alpha = ((y2 - y3) * (x - x3) + (x3 - x2) * (y - y3)) / denom;
        double beta = ((y3 - y1) * (x - x3) + (x1 - x3) * (y - y3)) / denom;
        double gamma = 1 - alpha - beta;

        List<Double> invWs = triangle.getInvWs();

        double invW1 = invWs.get(0);
        double invW2 = invWs.get(1);
        double invW3 = invWs.get(2);

        double interpolatedInvW = alpha * invW1 + beta * invW2 + gamma * invW3;

        if (Math.abs(interpolatedInvW) < 1e-9) {
            return RenderingConfig.NUMERICAL_ERROR_COLOR;
        }

        List<Point2D> uvs = triangle.getUVs();

        Point2D uv1 = uvs.get(0);
        Point2D uv2 = uvs.get(1);
        Point2D uv3 = uvs.get(2);

        double uOverW = alpha * (uv1.getX() * invW1) +
                beta * (uv2.getX() * invW2) +
                gamma * (uv3.getX() * invW3);

        double vOverW = alpha * (uv1.getY() * invW1) +
                beta * (uv2.getY() * invW2) +
                gamma * (uv3.getY() * invW3);

        double u = uOverW / interpolatedInvW;
        double v = vOverW / interpolatedInvW;

        u = Math.max(0, Math.min(1, u));
        v = Math.max(0, Math.min(1, v));

        return sampleTexture(triangle.getMaterial().getTexture(), u, v);
    }

    /**
     * Сэмплирует текстуру по UV-координатам.
     * <p>
     * Использует nearest-neighbor фильтрацию.
     *
     * @param texture   изображение текстуры
     * @param u         координата U [0, 1]
     * @param v         координата V [0, 1]
     * @return цвет текстуры в точке
     */
    private static Color sampleTexture(Image texture, double u, double v) {
        u = Math.max(0, Math.min(1, u));
        v = Math.max(0, Math.min(1, v));

        int texWidth = texture.getWidth(null);
        int texHeight = texture.getHeight(null);

        int texX = (int)(u * (texWidth - 1));
        int texY = (int)((1 - v) * (texHeight - 1));

        BufferedImage buffered = (java.awt.image.BufferedImage) texture;

        return new Color(buffered.getRGB(texX, texY));
    }
}
