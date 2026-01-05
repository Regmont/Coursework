package graphics;

import game.configuration.RenderingConfig;
import geometry.Triangle;
import scene.Camera;
import geometry.Mesh;
import math.Vector3D;
import org.joml.Matrix4d;
import org.joml.Vector4d;

import java.util.ArrayList;
import java.util.List;

public class SceneTransformer {
    public static Mesh transformMesh(Mesh mesh, Matrix4d modelMatrix, Camera camera,
                                     int screenWidth, int screenHeight) {
        List<Triangle> originalTriangles = mesh.triangles();
        ArrayList<Triangle> transformedTriangles = new ArrayList<>();

        Matrix4d viewMatrix = CameraBase.getViewMatrix(camera);
        Matrix4d projMatrix = CameraBase.getProjectionMatrix(camera, screenWidth, screenHeight,
                RenderingConfig.MAIN_CAMERA_NEAR, RenderingConfig.MAIN_CAMERA_FAR);
        Matrix4d mvpMatrix = projMatrix.mul(viewMatrix).mul(modelMatrix);

        for (Triangle triangle : originalTriangles) {
            ArrayList<Vector3D> points = triangle.getPoints();
            Vector3D[] transformedPoints = new Vector3D[3];
            double[] invW = new double[3];

            for (int i = 0; i < 3; i++) {
                Vector4d vec = new Vector4d(points.get(i).getX(), points.get(i).getY(), points.get(i).getZ(), 1.0);
                vec = mvpMatrix.transform(vec);

                if (vec.w <= 0.0) {
                    transformedPoints[i] = new Vector3D(Double.NaN, Double.NaN, Double.NaN);
                    invW[i] = 0;
                } else {
                    invW[i] = 1.0 / vec.w;

                    vec.x /= vec.w;
                    vec.y /= vec.w;
                    vec.z /= vec.w;

                    double screenX = (vec.x + 1.0) * screenWidth / 2.0;
                    double screenY = (1.0 - vec.y) * screenHeight / 2.0;

                    transformedPoints[i] = new Vector3D(screenX, screenY, vec.z);
                }
            }

            Vector3D[] worldPoints;

            if (triangle.hasOriginalPoints()) {
                worldPoints = new Vector3D[3];

                for (int i = 0; i < 3; i++) {
                    worldPoints[i] = applyWorldMatrix(modelMatrix, triangle.getOriginalPoints()[i]);
                }
            } else {
                worldPoints = new Vector3D[3];

                for (int i = 0; i < 3; i++) {
                    worldPoints[i] = applyWorldMatrix(modelMatrix, points.get(i));
                }
            }

            Triangle transformedTriangle = new Triangle(
                    transformedPoints[0],
                    transformedPoints[1],
                    transformedPoints[2],
                    triangle.getMaterial(),
                    worldPoints,
                    triangle.getUV1(),
                    triangle.getUV2(),
                    triangle.getUV3(),
                    invW[0],
                    invW[1],
                    invW[2]
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