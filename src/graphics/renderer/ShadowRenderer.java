package graphics.renderer;

import geometry.Mesh;
import geometry.Triangle;
import graphics.light.PointLight;
import graphics.shadows.ShadowCamera;
import graphics.shadows.ShadowCubeFace;
import math.Vector3D;
import geometry.BoundingBox;
import graphics.utils.GeometryUtils;
import org.joml.Vector4d;
import org.joml.Matrix4d;
import scene.ObjectInstance;

import java.util.ArrayList;
import java.util.List;

public class ShadowRenderer {

    public static void renderShadowMaps(List<PointLight> lights, List<ObjectInstance> instances) {
        for (PointLight light : lights) {
            for (ShadowCubeFace face : light.getShadowCube().getFaces()) {
                face.clearDepthBuffer();

                ShadowCamera shadowCamera = face.getCamera();
                int width = face.getDepthBuffer().length;
                int height = face.getDepthBuffer()[0].length;

                for (ObjectInstance instance : instances) {
                    Mesh originalMesh = instance.getMesh();

                    boolean hasTransparentMaterial = originalMesh.triangles().stream()
                            .anyMatch(t -> t.getMaterial().isTransparentForLight());

                    if (hasTransparentMaterial) {
                        continue;
                    }

                    Matrix4d modelMatrix = instance.getModelMatrix();

                    Mesh shadowMesh = transformMeshForShadow(originalMesh, modelMatrix,
                            shadowCamera, width, height);

                    rasterizeTrianglesToDepthBuffer(shadowMesh, face.getDepthBuffer());
                }
            }
        }
    }

    private static Mesh transformMeshForShadow(Mesh originalMesh, Matrix4d modelMatrix,
                                               ShadowCamera shadowCamera, int width, int height) {
        Matrix4d viewMatrix = shadowCamera.getViewMatrix();
        Matrix4d projMatrix = shadowCamera.getProjectionMatrix();
        Matrix4d mvpMatrix = projMatrix.mul(viewMatrix).mul(modelMatrix);

        List<Triangle> originalTriangles = originalMesh.triangles();
        List<Triangle> transformedTriangles = new ArrayList<>();

        for (Triangle triangle : originalTriangles) {
            List<Vector3D> points = triangle.getPoints();
            Vector3D[] transformedPoints = new Vector3D[points.size()];
            double[] invW = new double[points.size()];

            for (int i = 0; i < points.size(); i++) {
                Vector3D point = points.get(i);
                Vector4d vec = new Vector4d(point.getX(), point.getY(), point.getZ(), 1.0);
                vec = mvpMatrix.transform(vec);

                if (vec.w <= 0.0) {
                    transformedPoints[i] = new Vector3D(Double.NaN, Double.NaN, Double.NaN);
                    invW[i] = 0;
                } else {
                    invW[i] = 1.0 / vec.w;

                    vec.x /= vec.w;
                    vec.y /= vec.w;
                    vec.z /= vec.w;

                    double screenX = (vec.x + 1.0) * width / 2.0;
                    double screenY = (1.0 - vec.y) * height / 2.0;

                    transformedPoints[i] = new Vector3D(screenX, screenY, vec.z);
                }
            }

            Triangle transformedTriangle = new Triangle(
                    transformedPoints[0], transformedPoints[1], transformedPoints[2],
                    triangle.getMaterial(),
                    triangle.getOriginalPoints(),
                    triangle.getUV1(), triangle.getUV2(), triangle.getUV3(),
                    invW[0], invW[1], invW[2]
            );

            transformedTriangles.add(transformedTriangle);
        }

        return new Mesh(transformedTriangles);
    }

    private static void rasterizeTrianglesToDepthBuffer(Mesh mesh, double[][] depthBuffer) {
        for (Triangle triangle : mesh.triangles()) {
            int width = depthBuffer.length;
            int height = depthBuffer[0].length;

            BoundingBox boundingBox = triangle.getBoundingBox();
            int minX = Math.max(0, boundingBox.minX());
            int maxX = Math.min(width - 1, boundingBox.maxX());
            int minY = Math.max(0, boundingBox.minY());
            int maxY = Math.min(height - 1, boundingBox.maxY());

            for (int y = minY; y <= maxY; y++) {
                for (int x = minX; x <= maxX; x++) {
                    double centerX = x + 0.5;
                    double centerY = y + 0.5;

                    if (!GeometryUtils.isPointInTriangle(centerX, centerY, triangle)) {
                        continue;
                    }

                    double depth = GeometryUtils.calculateDepthAtPoint(centerX, centerY, triangle);

                    if (depth < depthBuffer[x][y]) {
                        depthBuffer[x][y] = depth;
                    }
                }
            }
        }
    }
}
