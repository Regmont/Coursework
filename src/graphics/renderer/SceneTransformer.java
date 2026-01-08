package graphics.renderer;

import core.base.CameraBase;
import core.math.Vector3D;

import graphics.config.RenderingConfig;
import scene.SceneSystem;
import org.joml.Matrix4d;
import org.joml.Vector4d;
import scene.*;
import scene.gameObjects.Camera;
import scene.gameObjects.PointLight;
import scene.gameObjects.SimpleObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Трансформатор сцены: преобразует объекты из мировых в экранные координаты.
 * <p>
 * Применяет цепочку преобразований (Model-View-Projection) ко всем объектам сцены,
 * включая визуальные представления источников света.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class SceneTransformer {
    /**
     * Преобразует все объекты сцены в экранные координаты.
     * <p>
     * Обрабатывает:
     * <ul>
     *   <li>Все обычные объекты сцены</li>
     *   <li>Визуальные представления источников света (если есть)</li>
     * </ul>
     * Для каждого объекта применяет MVP-преобразование с текущей камерой.
     *
     * @param sceneSystem   система сцены
     * @param screenWidth   ширина экрана в пикселях
     * @param screenHeight  высота экрана в пикселях
     * @return список мешей с вершинами в экранных координатах
     */
    public static List<Mesh> getTransformedMeshes(SceneSystem sceneSystem, int screenWidth, int screenHeight) {
        List<Mesh> meshes = new ArrayList<>();

        for (SimpleObject object : sceneSystem.getObjects()) {
            Matrix4d modelMatrix = object.getTransform().getModelMatrix();

            Mesh originalMesh = object.getMesh();
            Mesh transformedMesh = SceneTransformer.transformMesh(
                    originalMesh, modelMatrix, sceneSystem.getCamera(), screenWidth, screenHeight
            );
            meshes.add(transformedMesh);
        }

        for (PointLight light : sceneSystem.getPointLights()) {
            if (light.hasObject()) {
                SimpleObject object = light.getObject();
                Matrix4d modelMatrix = object.getTransform().getModelMatrix();
                Mesh transformedMesh = SceneTransformer.transformMesh(
                        object.getMesh(),
                        modelMatrix,
                        sceneSystem.getCamera(),
                        screenWidth,
                        screenHeight
                );
                meshes.add(transformedMesh);
            }
        }

        return meshes;
    }

    /**
     * Преобразует меш объекта из мировых в экранные координаты.
     * <p>
     * Выполняет:
     * <ol>
     *   <li>MVP-преобразование: Model → View → Projection (P * V * M)</li>
     *   <li>Perspective divide (деление на w)</li>
     *   <li>Преобразование NDC [-1,1] в экранные координаты [0, screenSize]</li>
     *   <li>Сохранение invW (1/w) для перспективно-корректной интерполяции</li>
     *   <li>Преобразование оригинальных вершин в мировые координаты (через Model)</li>
     * </ol>
     *
     * @param mesh          исходный меш с вершинами в локальных координатах объекта
     * @param modelMatrix   матрица модели объекта
     * @param camera        камера для view/projection преобразований
     * @param screenWidth   ширина экрана
     * @param screenHeight  высота экрана
     * @return меш с вершинами в экранных координатах и мировыми координатами
     */
    public static Mesh transformMesh(Mesh mesh, Matrix4d modelMatrix, Camera camera,
                                     int screenWidth, int screenHeight) {
        List<RenderableTriangle> originalTriangles = mesh.triangles();
        ArrayList<RenderableTriangle> transformedTriangles = new ArrayList<>();

        Matrix4d viewMatrix = CameraBase.getViewMatrix(camera);
        Matrix4d projMatrix = CameraBase.getProjectionMatrix(camera, screenWidth, screenHeight,
                RenderingConfig.MAIN_CAMERA_NEAR, RenderingConfig.MAIN_CAMERA_FAR);
        Matrix4d mvpMatrix = projMatrix.mul(viewMatrix).mul(modelMatrix);

        for (RenderableTriangle triangle : originalTriangles) {
            List<Vector3D> points = triangle.getCurrentPoints();
            List<Vector3D> transformedPoints = new ArrayList<>();
            List<Double> invW = new ArrayList<>();

            for (int i = 0; i < 3; i++) {
                Vector4d vec = new Vector4d(points.get(i).x(), points.get(i).y(), points.get(i).z(), 1.0);
                vec = mvpMatrix.transform(vec);

                if (vec.w <= 0.0) {
                    transformedPoints.add(new Vector3D(Double.NaN, Double.NaN, Double.NaN));
                    invW.add(0d);
                } else {
                    invW.add(1.0 / vec.w);

                    vec.x /= vec.w;
                    vec.y /= vec.w;
                    vec.z /= vec.w;

                    double screenX = (vec.x + 1.0) * screenWidth / 2.0;
                    double screenY = (1.0 - vec.y) * screenHeight / 2.0;

                    transformedPoints.add(new Vector3D(screenX, screenY, vec.z));
                }
            }

            List<Vector3D> worldPoints = new ArrayList<>();

            for (int i = 0; i < 3; i++) {
                worldPoints.add(applyWorldMatrix(modelMatrix, triangle.getOriginalPoints().get(i)));
            }

            RenderableTriangle transformedTriangle = new RenderableTriangle(
                    worldPoints,
                    transformedPoints,
                    triangle.getMaterial(),
                    triangle.getUVs(),
                    invW
            );

            transformedTriangles.add(transformedTriangle);
        }

        return new Mesh(transformedTriangles);
    }

    /**
     * Применяет матрицу модели к точке.
     *
     * @param modelMatrix   матрица модели
     * @param point         точка в локальных координатах объекта
     * @return точка в мировых координатах
     */
    private static Vector3D applyWorldMatrix(Matrix4d modelMatrix, Vector3D point) {
        Vector4d vec = new Vector4d(point.x(), point.y(), point.z(), 1.0);
        vec = modelMatrix.transform(vec);

        return new Vector3D(vec.x, vec.y, vec.z);
    }
}
