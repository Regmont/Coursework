package graphics.shadows;

import core.base.CameraBase;
import scene.Mesh;
import scene.RenderableTriangle;
import scene.gameObjects.SimpleObject;
import graphics.config.RenderingConfig;
import core.math.Vector3D;
import graphics.TriangleBoundingBox;
import graphics.utils.GeometryUtils;

import org.joml.Vector4d;
import org.joml.Matrix4d;

import java.util.ArrayList;
import java.util.List;

/**
 * Рендерер для построения shadow maps (карт теней).
 * <p>
 * Рендерит глубину сцены с точки зрения каждого источника света,
 * создавая shadow maps для последующего использования при освещении.
 * Поддерживает cube shadow maps для точечных источников света.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class ShadowRenderer {
    /**
     * Рендерит shadow maps для всех источников света в системе.
     * <p>
     * Процесс:
     * <ol>
     *   <li>Для каждого источника света и каждой грани его cube map:</li>
     *   <li>Очистка глубинного буфера грани</li>
     *   <li>Для каждого непрозрачного объекта сцены:</li>
     *   <li>Преобразование вершин в пространство теневой камеры (MVP матрица)</li>
     *   <li>Растеризация треугольников в глубинный буфер (z-buffer)</li>
     * </ol>
     * Объекты с материалами, не отбрасывающими тени (castsShadows=false), пропускаются.
     *
     * @param shadowLightSystem система управления тенями источников света
     * @param objects           все объекты сцены
     */
    public static void renderShadowMaps(ShadowLightSystem shadowLightSystem, List<SimpleObject> objects) {
        for (ShadowCube cube : shadowLightSystem.getLightToShadowCube().values()) {
            for (ShadowCubeFace face : cube.getFaces()) {
                face.clearDepthBuffer();

                ShadowCamera shadowCamera = face.getCamera();
                int width = face.getDepthBuffer().length;
                int height = face.getDepthBuffer()[0].length;

                for (SimpleObject object : objects) {
                    Mesh originalMesh = object.getMesh();

                    boolean hasTransparentMaterial = originalMesh.triangles().stream()
                            .anyMatch(t -> !t.getMaterial().castsShadows());

                    if (hasTransparentMaterial) {
                        continue;
                    }

                    Matrix4d modelMatrix = object.getTransform().getModelMatrix();

                    Mesh shadowMesh = transformMeshForShadow(originalMesh, modelMatrix,
                            shadowCamera, width, height);

                    rasterizeTrianglesToDepthBuffer(shadowMesh, face.getDepthBuffer());
                }
            }
        }
    }

    /**
     * Преобразует меш объекта в пространство теневой камеры.
     * <p>
     * Применяет цепочку преобразований: Model → View → Projection.
     * Выполняет перспективное деление и преобразование в экранные координаты.
     * Вершины за камерой (w ≤ 0) помечаются как NaN.
     *
     * @param originalMesh  исходный меш объекта
     * @param modelMatrix   матрица модели объекта
     * @param shadowCamera  теневая камера (для данной грани cube map)
     * @param width         ширина shadow map в пикселях
     * @param height        высота shadow map в пикселях
     * @return меш с вершинами в экранных координатах shadow map
     */
    private static Mesh transformMeshForShadow(Mesh originalMesh, Matrix4d modelMatrix,
                                               ShadowCamera shadowCamera, int width, int height) {
        Matrix4d viewMatrix = CameraBase.getViewMatrix(shadowCamera);
        Matrix4d projMatrix = CameraBase.getProjectionMatrix(shadowCamera, RenderingConfig.SHADOW_MAP_RESOLUTION,
                RenderingConfig.SHADOW_MAP_RESOLUTION, RenderingConfig.SHADOW_CAMERA_NEAR,
                RenderingConfig.SHADOW_CAMERA_FAR);
        Matrix4d mvpMatrix = projMatrix.mul(viewMatrix).mul(modelMatrix);

        List<RenderableTriangle> originalTriangles = originalMesh.triangles();
        List<RenderableTriangle> transformedTriangles = new ArrayList<>();

        for (RenderableTriangle triangle : originalTriangles) {
            List<Vector3D> points = triangle.getCurrentPoints();
            List<Vector3D> transformedPoints = new ArrayList<>();
            List<Double> invWs = new ArrayList<>();

            for (Vector3D point : points) {
                Vector4d vec = new Vector4d(point.x(), point.y(), point.z(), 1.0);
                vec = mvpMatrix.transform(vec);

                if (vec.w <= 0.0) {
                    transformedPoints.add(new Vector3D(Double.NaN, Double.NaN, Double.NaN));
                    invWs.add(0d);
                } else {
                    invWs.add(1.0 / vec.w);

                    vec.x /= vec.w;
                    vec.y /= vec.w;
                    vec.z /= vec.w;

                    double screenX = (vec.x + 1.0) * width / 2.0;
                    double screenY = (1.0 - vec.y) * height / 2.0;

                    transformedPoints.add(new Vector3D(screenX, screenY, vec.z));
                }
            }

            RenderableTriangle transformedTriangle = new RenderableTriangle(
                    triangle.getOriginalPoints(),
                    transformedPoints,
                    triangle.getMaterial(),
                    triangle.getUVs(),
                    invWs
            );

            transformedTriangles.add(transformedTriangle);
        }

        return new Mesh(transformedTriangles);
    }

    /**
     * Растеризует треугольники в глубинный буфер.
     * <p>
     * Для каждого треугольника:
     * <ol>
     *   <li>Вычисляет bounding box в экранных координатах</li>
     *   <li>Ограничивает bounding box размерами буфера</li>
     *   <li>Для каждого пикселя в bounding box проверяет принадлежность треугольнику</li>
     *   <li>Вычисляет глубину в точке и записывает, если она ближе текущей</li>
     * </ol>
     * Использует алгоритм z-buffer для определения видимости.
     *
     * @param mesh          меш с треугольниками в экранных координатах
     * @param depthBuffer   целевой глубинный буфер (shadow map)
     */
    private static void rasterizeTrianglesToDepthBuffer(Mesh mesh, double[][] depthBuffer) {
        for (RenderableTriangle triangle : mesh.triangles()) {
            int width = depthBuffer.length;
            int height = depthBuffer[0].length;

            TriangleBoundingBox triangleBoundingBox = TriangleBoundingBox.clampToScreen(
                    TriangleBoundingBox.getBoundingBox(triangle.getCurrentTriangle()), width, height);

            for (int y = triangleBoundingBox.minY(); y <= triangleBoundingBox.maxY(); y++) {
                for (int x = triangleBoundingBox.minX(); x <= triangleBoundingBox.maxX(); x++) {
                    double centerX = x + 0.5;
                    double centerY = y + 0.5;

                    if (!GeometryUtils.isPointIn3DTriangle(centerX, centerY, triangle.getCurrentTriangle())) {
                        continue;
                    }

                    double depth = GeometryUtils.calculateDepthAtPoint(centerX, centerY, triangle.getCurrentTriangle());

                    if (depth < depthBuffer[x][y]) {
                        depthBuffer[x][y] = depth;
                    }
                }
            }
        }
    }
}
