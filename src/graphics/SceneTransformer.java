package graphics;

import geometry.Triangle;
import scene.Camera;
import geometry.Mesh;
import math.Vector3D;
import org.joml.Matrix4d;
import org.joml.Vector4d;
import java.util.ArrayList;
import java.util.List;

public class SceneTransformer {
    public static Matrix4d createMVPMatrix(Camera camera, Matrix4d modelMatrix, int screenWidth, int screenHeight) {
        Matrix4d viewMatrix = camera.getViewMatrix();
        Matrix4d projMatrix = camera.getProjectionMatrix(screenWidth, screenHeight);

        return projMatrix.mul(viewMatrix).mul(modelMatrix);
    }

    public static Mesh transformMesh(Mesh mesh, Matrix4d transformMatrix, int screenWidth, int screenHeight) {
        List<Triangle> originalTriangles = mesh.triangles();
        ArrayList<Triangle> transformedTriangles = new ArrayList<>();

        for (Triangle triangle : originalTriangles) {
            ArrayList<Vector3D> points = triangle.getPoints();
            Vector3D[] transformedPoints = new Vector3D[3];

            for (int i = 0; i < 3; i++) {
                transformedPoints[i] = applyMatrix(transformMatrix, points.get(i), screenWidth, screenHeight);
            }

            Vector3D[] originalPoints = triangle.hasOriginalPoints() ?
                    triangle.getOriginalPoints() :
                    new Vector3D[]{points.get(0), points.get(1), points.get(2)};

            Triangle transformedTriangle = new Triangle(
                    transformedPoints[0],
                    transformedPoints[1],
                    transformedPoints[2],
                    triangle.getMaterial(),
                    originalPoints,
                    triangle.getUV1(),
                    triangle.getUV2(),
                    triangle.getUV3()
            );

            transformedTriangles.add(transformedTriangle);
        }

        return new Mesh(transformedTriangles);
    }

    private static Vector3D applyMatrix(Matrix4d mvp, Vector3D point, int screenWidth, int screenHeight) {
        Vector4d vec = new Vector4d(point.getX(), point.getY(), point.getZ(), 1.0f);
        vec = mvp.transform(vec);

        if (vec.w <= 0.0) {
            return new Vector3D(Double.NaN, Double.NaN, Double.NaN);
        }

        vec.x /= vec.w;
        vec.y /= vec.w;
        vec.z /= vec.w;

        double screenX = (vec.x + 1.0) * screenWidth / 2.0;
        double screenY = (1.0 - vec.y) * screenHeight / 2.0;

        return new Vector3D(screenX, screenY, vec.z);
    }
}