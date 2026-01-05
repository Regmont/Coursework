package graphics.renderer;

import geometry.*;
import graphics.SceneSystem;
import scene.PointLight;
import graphics.light.ShadowLightSystem;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

public class MainRenderer {
    public static void renderScene(Color[][] colorBuffer, double[][] depthBuffer,
                                   Color backgroundColor, SceneSystem sceneSystem) {
        clearBuffers(colorBuffer, depthBuffer, backgroundColor);

        int width = colorBuffer.length;
        int height = colorBuffer[0].length;

        List<Mesh> meshes = sceneSystem.getTransformedMeshes(width, height);
        List<PointLight> pointLights = sceneSystem.getPointLights();

        ShadowLightSystem shadowLightSystem = new ShadowLightSystem(pointLights);

        ShadowRenderer.renderShadowMaps(shadowLightSystem, sceneSystem.getInstances());
        TriangleRenderer.renderTriangles(meshes, colorBuffer, depthBuffer, width, height, shadowLightSystem,
                sceneSystem.getAmbienceLight());
    }

    private static void clearBuffers(Color[][] colorBuffer, double[][] depthBuffer, Color backgroundColor) {
        for (double[] row : depthBuffer) {
            Arrays.fill(row, Double.POSITIVE_INFINITY);
        }

        for (Color[] colors : colorBuffer) {
            Arrays.fill(colors, backgroundColor);
        }
    }
}