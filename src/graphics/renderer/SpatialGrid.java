package graphics.renderer;

import scene.RenderableTriangle;
import graphics.config.RenderingConfig;
import graphics.TriangleBoundingBox;

import java.util.*;

/**
 * Пространственная сетка для ускорения поиска треугольников при растеризации.
 * <p>
 * Делит экран на ячейки фиксированного размера и распределяет треугольники
 * по ячейкам, которые они пересекают. Позволяет при растеризации пикселя
 * проверять только треугольники из соответствующей ячейки.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class SpatialGrid {
    private final int cellSize;
    private final List<RenderableTriangle>[] cells;
    private final int cols;
    private final int rows;

    /**
     * Создает пространственную сетку для экрана указанного размера.
     * <p>
     * Размер ячейки берется из {@link RenderingConfig#SPATIAL_GRID_CELL_SIZE}.
     *
     * @param screenWidth ширина экрана в пикселях
     * @param screenHeight высота экрана в пикселях
     */
    @SuppressWarnings("unchecked")
    public SpatialGrid(int screenWidth, int screenHeight) {
        this.cellSize = RenderingConfig.SPATIAL_GRID_CELL_SIZE;
        this.cols = (screenWidth + cellSize - 1) / cellSize;
        this.rows = (screenHeight + cellSize - 1) / cellSize;

        cells = (List<RenderableTriangle>[]) new List[cols * rows];

        for (int i = 0; i < cells.length; i++) {
            cells[i] = new ArrayList<>();
        }
    }

    /**
     * Очищает все ячейки сетки.
     * <p>
     * Должен вызываться перед началом рендеринга каждого кадра.
     */
    public void clear() {
        for (List<RenderableTriangle> cell : cells) {
            cell.clear();
        }
    }

    /**
     * Добавляет треугольник в сетку.
     * <p>
     * Треугольник добавляется во все ячейки, которые пересекает его bounding box.
     * Невидимые треугольники (обращенные от камеры) игнорируются.
     *
     * @param triangle треугольник для добавления
     */
    public void addTriangle(RenderableTriangle triangle) {
        if (!triangle.isVisibleFromCameraCenter()) {
            return;
        }

        TriangleBoundingBox triangleBoundingBox = TriangleBoundingBox.getBoundingBox(triangle.getCurrentTriangle());

        int minCellX = Math.max(0, triangleBoundingBox.minX() / cellSize);
        int maxCellX = Math.min(cols - 1, triangleBoundingBox.maxX() / cellSize);
        int minCellY = Math.max(0, triangleBoundingBox.minY() / cellSize);
        int maxCellY = Math.min(rows - 1, triangleBoundingBox.maxY() / cellSize);

        for (int cy = minCellY; cy <= maxCellY; cy++) {
            int rowOffset = cy * cols;

            for (int cx = minCellX; cx <= maxCellX; cx++) {
                cells[rowOffset + cx].add(triangle);
            }
        }
    }

    /**
     * Возвращает все треугольники в ячейке, содержащей указанную точку.
     *
     * @param x координата X точки в экранных координатах
     * @param y координата Y точки в экранных координатах
     * @return список треугольников в ячейке или пустой список если точка вне сетки
     */
    public List<RenderableTriangle> getTriangles(int x, int y) {
        int cellX = x / cellSize;
        int cellY = y / cellSize;

        if (cellX >= 0 && cellX < cols && cellY >= 0 && cellY < rows) {
            return cells[cellY * cols + cellX];
        }

        return Collections.emptyList();
    }

    @Override
    public String toString() {
        int totalTriangles = 0;
        int maxTrianglesInCell = 0;

        for (List<RenderableTriangle> cell : cells) {
            totalTriangles += cell.size();
            if (cell.size() > maxTrianglesInCell) {
                maxTrianglesInCell = cell.size();
            }
        }

        return String.format(
                "SpatialGrid[%dx%d cells (%dx%d), cellSize=%d, triangles=%d (max/cell=%d)]",
                cols, rows, cols * cellSize, rows * cellSize, cellSize,
                totalTriangles, maxTrianglesInCell
        );
    }
}
