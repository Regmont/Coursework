package graphics.renderer;

import graphics.RenderingConfig;
import graphics.Mesh;
import graphics.RenderableTriangle;
import core.base.CameraBase;
import graphics.light.ShadowLightSystem;
import graphics.shadows.ShadowCamera;
import graphics.shadows.ShadowCube;
import graphics.shadows.ShadowCubeFace;
import core.math.Vector3D;
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

        List<RenderableTriangle> originalTriangles = originalMesh.triangles();
        List<RenderableTriangle> transformedTriangles = new ArrayList<>();

        for (RenderableTriangle triangle : originalTriangles) {
            List<Vector3D> points = triangle.getCurrentPoints();
            List<Vector3D> transformedPoints = new ArrayList<>();
            List<Double> invWs = new ArrayList<>();

            for (Vector3D point : points) {
                Vector4d vec = new Vector4d(point.getX(), point.getY(), point.getZ(), 1.0);
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

    private static void rasterizeTrianglesToDepthBuffer(Mesh mesh, double[][] depthBuffer) {
        for (RenderableTriangle triangle : mesh.triangles()) {
            int width = depthBuffer.length;
            int height = depthBuffer[0].length;

            TriangleBoundingBox triangleBoundingBox = TriangleBoundingBox.clampToScreen(
                    GeometryUtils.getTriangleBoundingBox(triangle.getCurrentTriangle()), width, height);

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
