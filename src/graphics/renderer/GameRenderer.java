package graphics.renderer;

import graphics.SceneSystem;
import graphics.utils.ColorUtils;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferInt;

/**
 * Основной рендерер игры, управляющий буферами и отрисовкой кадров.
 * <p>
 * Создает и управляет цветовым и глубинным буферами, преобразует результат
 * в {@link BufferedImage} для отображения в окне.
 * Автоматически пересоздает буферы при изменении размеров окна.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class GameRenderer {
    private Color[][] colorBuffer;
    private double[][] depthBuffer;
    private BufferedImage frame;
    private final SceneSystem sceneSystem;

    /**
     * Создает рендерер для указанной системы сцены.
     *
     * @param sceneSystem система сцены, содержащая объекты для рендеринга
     */
    public GameRenderer(SceneSystem sceneSystem) {
        this.sceneSystem = sceneSystem;
    }

    /**
     * Возвращает следующий кадр для отображения.
     * <p>
     * Процесс:
     * <ol>
     *   <li>При необходимости пересоздает буферы под новый размер</li>
     *   <li>Рендерит сцену в цветовой и глубинный буферы</li>
     *   <li>Копирует цветовой буфер в BufferedImage для отображения</li>
     * </ol>
     *
     * @param width     требуемая ширина кадра в пикселях
     * @param height    требуемая высота кадра в пикселях
     * @return изображение кадра для отображения в окне
     */
    public BufferedImage getFrame(int width, int height) {
        resizeBuffersIfWindowSizeChanged(width, height);
        MainRenderer.renderScene(colorBuffer, depthBuffer, ColorUtils.getSkyColor(sceneSystem.getSky()), sceneSystem);
        copyColorBufferToImage(width, height);

        return frame;
    }

    /**
     * Пересоздает буферы при изменении размеров окна.
     * <p>
     * Цветовой буфер хранит объекты {@link Color} для каждой точки,
     * глубинный буфер — значения глубины типа {@code double},
     * {@link BufferedImage} — финальное изображение в формате RGB.
     *
     * @param width     новая ширина окна
     * @param height    новая высота окна
     */
    private void resizeBuffersIfWindowSizeChanged(int width, int height) {
        if (colorBuffer == null || colorBuffer.length != width || colorBuffer[0].length != height) {
            colorBuffer = new Color[width][height];
            depthBuffer = new double[width][height];
            frame = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        }
    }

    /**
     * Копирует цветовой буфер в {@link BufferedImage} для отображения.
     * <p>
     * Использует прямое обращение к DataBuffer для максимальной производительности.
     * Пиксели без цвета (null) заполняются цветом фона.
     *
     * @param width     ширина изображения
     * @param height    высота изображения
     */
    private void copyColorBufferToImage(int width, int height) {
        int[] pixels = ((DataBufferInt) frame.getRaster().getDataBuffer()).getData();
        int backgroundRGB = ColorUtils.getSkyColor(sceneSystem.getSky()).getRGB();

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                Color color = colorBuffer[x][y];
                pixels[y * width + x] = (color != null) ? color.getRGB() : backgroundRGB;
            }
        }
    }
}
