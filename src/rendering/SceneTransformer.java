package rendering;

import geometricObjects.Triangle;
import sceneObjects.Camera;
import geometricObjects.Mesh;
import math.Point3D;
import org.joml.Matrix4d;
import org.joml.Vector4d;
import java.util.ArrayList;

public class SceneTransformer {
    private static int screenWidth = 400;
    private static int screenHeight = 400;
    public static final double FOV = Math.PI/3;
    public static final double NEAR_PLANE = 0.1;
    public static final double FAR_PLANE = 100.0;

    public static void setScreenSize(int width, int height) {
        screenWidth = width;
        screenHeight = height;
    }

    public static Matrix4d createViewMatrix(Camera camera) {
        Point3D lookAt = camera.getLookAtPoint();
        return new Matrix4d()
                .lookAt(camera.getX(), camera.getY(), camera.getZ(),
                        lookAt.getX(), lookAt.getY(), lookAt.getZ(),
                        0, 1, 0);
    }

    public static Matrix4d createProjectionMatrix() {
        double aspectRatio = (double) screenWidth / screenHeight;
        return new Matrix4d()
                .perspective(FOV, (float)aspectRatio, NEAR_PLANE, FAR_PLANE);
    }

    public static Matrix4d createMVPMatrix(Camera camera, Matrix4d modelMatrix) {
        Matrix4d viewMatrix = createViewMatrix(camera);
        Matrix4d projMatrix = createProjectionMatrix();
        return projMatrix.mul(viewMatrix).mul(modelMatrix);
    }

    public static Mesh transformMesh(Mesh mesh, Matrix4d transformMatrix) {
        ArrayList<Triangle> originalTriangles = mesh.getTriangles();
        ArrayList<Triangle> transformedTriangles = new ArrayList<>();

        for (Triangle triangle : originalTriangles) {
            ArrayList<Point3D> points = triangle.getPoints();
            Point3D[] transformedPoints = new Point3D[3];

            for (int i = 0; i < 3; i++) {
                transformedPoints[i] = applyMatrix(transformMatrix, points.get(i));
            }

            Point3D[] originalPoints = triangle.hasOriginalPoints() ?
                    triangle.getOriginalPoints() :
                    new Point3D[]{points.get(0), points.get(1), points.get(2)};

            Triangle transformedTriangle = new Triangle(
                    transformedPoints[0],
                    transformedPoints[1],
                    transformedPoints[2],
                    triangle.getColor(),
                    originalPoints
            );

            transformedTriangles.add(transformedTriangle);
        }

        Mesh transformedMesh = new Mesh(mesh.getScale(),
                transformedTriangles.toArray(new Triangle[0]));
        transformedMesh.setTriangles(transformedTriangles);

        return transformedMesh;
    }

    private static Point3D applyMatrix(Matrix4d mvp, Point3D point) {
        Vector4d vec = new Vector4d(point.getX(), point.getY(), point.getZ(), 1.0f);
        vec = mvp.transform(vec);

        if (vec.w <= 0.0) {
            return new Point3D(Double.NaN, Double.NaN, Double.NaN);
        }

        vec.x /= vec.w;
        vec.y /= vec.w;
        vec.z /= vec.w;

        double screenX = (vec.x + 1.0) * screenWidth / 2.0;
        double screenY = (1.0 - vec.y) * screenHeight / 2.0;

        return new Point3D(screenX, screenY, vec.z);
    }
}