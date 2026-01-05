package graphics.shadows;

import graphics.config.RenderingConfig;

import java.util.Arrays;

public class ShadowCubeFace {
    private final ShadowCamera camera;
    private final double[][] depthBuffer;

    public ShadowCubeFace(ShadowCamera camera) {
        this.camera = camera;
        this.depthBuffer = new double[RenderingConfig.SHADOW_MAP_RESOLUTION][RenderingConfig.SHADOW_MAP_RESOLUTION];
    }

    public ShadowCamera getCamera() {
        return camera;
    }

    public double[][] getDepthBuffer() {
        return depthBuffer;
    }

    public void clearDepthBuffer() {
        for (double[] doubles : depthBuffer) {
            Arrays.fill(doubles, Double.POSITIVE_INFINITY);
        }
    }
}