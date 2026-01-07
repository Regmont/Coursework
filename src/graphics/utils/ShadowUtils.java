package graphics.utils;

import core.base.CameraBase;
import core.math.Vector3D;
import scene.gameObjects.PointLight;
import graphics.config.RenderingConfig;
import graphics.shadows.ShadowCamera;
import graphics.shadows.ShadowCube;
import graphics.shadows.ShadowCubeFace;
import org.joml.Matrix4d;
import org.joml.Vector4d;

/**
 * Утилиты для работы с тенями (shadow mapping).
 * <p>
 * Содержит методы для проверки, находится ли точка в тени, и преобразования мировых координат в координаты shadow map.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class ShadowUtils {
    /**
     * Проверяет, находится ли точка в тени от указанного источника света.
     * <p>
     * Алгоритм:
     * <ol>
     *   <li>Определяет грань cube shadow map, в направлении которой находится точка</li>
     *   <li>Преобразует мировые координаты точки в координаты shadow map</li>
     *   <li>Сравнивает глубину точки с глубиной, сохраненной в shadow map</li>
     *   <li>Если глубина точки больше (дальше) + bias → точка в тени</li>
     * </ol>
     *
     * @param worldPoint    точка в мировых координатах
     * @param light         источник света
     * @param shadowCube    cube shadow map источника света
     * @return {@code true} если точка находится в тени
     */
    public static boolean isPointInShadow(Vector3D worldPoint, PointLight light, ShadowCube shadowCube) {
        int faceIndex = LightUtils.getCubeFaceIndex(light, worldPoint);
        ShadowCubeFace face = shadowCube.getFaces()[faceIndex];

        if (face == null) {
            return false;
        }

        double[] shadowCoords = worldToShadowMap(worldPoint, face.getCamera(),
                face.getDepthBuffer().length,
                face.getDepthBuffer()[0].length);

        if (shadowCoords[0] < 0) {
            return false;
        }

        int x = (int) shadowCoords[0];
        int y = (int) shadowCoords[1];

        double pointDepth = shadowCoords[2];
        double shadowDepth = face.getDepthBuffer()[x][y];

        return pointDepth > shadowDepth + RenderingConfig.SHADOW_BIAS;
    }

    /**
     * Преобразует мировые координаты в координаты shadow map.
     * <p>
     * Выполняет преобразование: world → view → projection → NDC → texture coordinates.
     * Точки за камерой (w ≤ 0) возвращают [-1, -1, ∞].
     *
     * @param worldPoint    точка в мировых координатах
     * @param shadowCamera  теневая камера (грань cube map)
     * @param shadowWidth   ширина shadow map в пикселях
     * @param shadowHeight  высота shadow map в пикселях
     * @return массив [x, y, depth] где:
     *         x, y - координаты в shadow map,
     *         depth - глубина в пространстве камеры (0..1 после perspective divide)
     */
    private static double[] worldToShadowMap(Vector3D worldPoint, ShadowCamera shadowCamera,
                                             int shadowWidth, int shadowHeight) {
        Matrix4d viewMatrix = CameraBase.getViewMatrix(shadowCamera);
        Matrix4d projMatrix = CameraBase.getProjectionMatrix(shadowCamera, RenderingConfig.SHADOW_MAP_RESOLUTION,
                RenderingConfig.SHADOW_MAP_RESOLUTION, RenderingConfig.SHADOW_CAMERA_NEAR,
                RenderingConfig.SHADOW_CAMERA_FAR);
        Matrix4d viewProjMatrix = projMatrix.mul(viewMatrix);

        Vector4d vec = new Vector4d(worldPoint.x(), worldPoint.y(), worldPoint.z(), 1.0);
        vec = viewProjMatrix.transform(vec);

        if (vec.w <= 0.0) {
            return new double[]{-1, -1, Double.POSITIVE_INFINITY};
        }

        vec.x /= vec.w;
        vec.y /= vec.w;
        vec.z /= vec.w;

        double u = (vec.x + 1.0) / 2.0;
        double v = (1.0 - vec.y) / 2.0;

        double x = u * shadowWidth;
        double y = v * shadowHeight;

        int pixelX = (int) Math.floor(x);
        int pixelY = (int) Math.floor(y);

        pixelX = Math.max(0, Math.min(shadowWidth - 1, pixelX));
        pixelY = Math.max(0, Math.min(shadowHeight - 1, pixelY));

        return new double[]{pixelX, pixelY, vec.z};
    }
}
