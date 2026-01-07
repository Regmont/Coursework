package graphics.shadows;

import graphics.config.RenderingConfig;

import java.util.Arrays;

/**
 * Одна грань cube shadow map.
 * <p>
 * Хранит глубинный буфер (shadow map) для конкретного направления из cube map точечного источника света.
 * Каждая грань имеет свою камеру ({@link ShadowCamera}), направленную вдоль одной из осей: ±X, ±Y, ±Z.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class ShadowCubeFace {
    private final ShadowCamera camera;
    private final double[][] depthBuffer;

    /**
     * Создает грань cube shadow map с указанной камерой.
     * <p>
     * Инициализирует глубинный буфер размером {@link RenderingConfig#SHADOW_MAP_RESOLUTION}.
     *
     * @param camera камера для этой грани, направленная вдоль одной из осей
     */
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

    /**
     * Очищает глубинный буфер, заполняя его значением {@link Double#POSITIVE_INFINITY}.
     * <p>
     * Должен вызываться перед рендерингом теней для каждого кадра.
     */
    public void clearDepthBuffer() {
        for (double[] doubles : depthBuffer) {
            Arrays.fill(doubles, Double.POSITIVE_INFINITY);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "ShadowCubeFace[direction=%s, resolution=%dx%d]",
                camera.getTransform().getForward(),
                depthBuffer.length,
                depthBuffer[0].length
        );
    }
}
