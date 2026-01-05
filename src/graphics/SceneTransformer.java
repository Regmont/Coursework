package graphics;

import game.SceneSystem;
import core.base.CameraBase;
import scene.Camera;
import core.math.Vector3D;
import org.joml.Matrix4d;
import org.joml.Vector4d;
import scene.PointLight;
import scene.SimpleObject;

import java.util.ArrayList;
import java.util.List;

public class SceneTransformer {
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
                Vector4d vec = new Vector4d(points.get(i).getX(), points.get(i).getY(), points.get(i).getZ(), 1.0);
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

            transformedTriangle.getWorldNormal();
            transformedTriangles.add(transformedTriangle);
        }

        return new Mesh(transformedTriangles);
    }

    private static Vector3D applyWorldMatrix(Matrix4d modelMatrix, Vector3D point) {
        Vector4d vec = new Vector4d(point.getX(), point.getY(), point.getZ(), 1.0);
        vec = modelMatrix.transform(vec);

        return new Vector3D(vec.x, vec.y, vec.z);
    }
}