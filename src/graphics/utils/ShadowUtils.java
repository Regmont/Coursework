package graphics.utils;

import game.configuration.RenderingConfig;
import graphics.light.PointLight;
import graphics.shadows.ShadowCamera;
import graphics.shadows.ShadowCubeFace;
import math.Vector3D;
import org.joml.Matrix4d;
import org.joml.Vector4d;

public class ShadowUtils {

    public static boolean isPointInShadow(Vector3D worldPoint, PointLight light) {
        int faceIndex = light.getCubeFaceIndex(worldPoint);
        ShadowCubeFace face = light.getShadowFace(faceIndex);

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

    private static double[] worldToShadowMap(Vector3D worldPoint, ShadowCamera shadowCamera,
                                             int shadowWidth, int shadowHeight) {
        Matrix4d viewMatrix = shadowCamera.getViewMatrix();
        Matrix4d projMatrix = shadowCamera.getProjectionMatrix();
        Matrix4d viewProjMatrix = projMatrix.mul(viewMatrix);

        Vector4d vec = new Vector4d(worldPoint.getX(), worldPoint.getY(), worldPoint.getZ(), 1.0);
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

        x = Math.max(0, Math.min(shadowWidth - 1, x));
        y = Math.max(0, Math.min(shadowHeight - 1, y));

        return new double[]{x, y, vec.z};
    }
}