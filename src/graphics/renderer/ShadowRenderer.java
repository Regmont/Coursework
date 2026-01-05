package graphics.renderer;

import game.configuration.RenderingConfig;
import geometry.Mesh;
import geometry.Triangle;
import graphics.CameraBase;
import graphics.light.ShadowLightSystem;
import graphics.shadows.ShadowCamera;
import graphics.shadows.ShadowCube;
import graphics.shadows.ShadowCubeFace;
import math.Vector3D;
import graphics.TriangleBoundingBox;
import graphics.utils.GeometryUtils;
import org.joml.Vector4d;
import org.joml.Matrix4d;
import scene.SimpleObject;

import java.util.ArrayList;
import java.util.List;

public class ShadowRenderer {

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
                            .anyMatch(t -> t.getMaterial().isTransparentForLight());

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

    private static Mesh transformMeshForShadow(Mesh originalMesh, Matrix4d modelMatrix,
                                               ShadowCamera shadowCamera, int width, int height) {
        Matrix4d viewMatrix = CameraBase.getViewMatrix(shadowCamera);
        Matrix4d projMatrix = CameraBase.getProjectionMatrix(shadowCamera, RenderingConfig.SHADOW_MAP_RESOLUTION,
                RenderingConfig.SHADOW_MAP_RESOLUTION, RenderingConfig.SHADOW_CAMERA_NEAR,
                RenderingConfig.SHADOW_CAMERA_FAR);
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

            TriangleBoundingBox triangleBoundingBox = TriangleBoundingBox.clampToScreen(triangle.getBoundingBox(), width, height);

            for (int y = triangleBoundingBox.minY(); y <= triangleBoundingBox.maxY(); y++) {
                for (int x = triangleBoundingBox.minX(); x <= triangleBoundingBox.maxX(); x++) {
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
