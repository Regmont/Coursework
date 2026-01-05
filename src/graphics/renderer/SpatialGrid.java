package graphics.renderer;

import scene.RenderableTriangle;
import graphics.config.RenderingConfig;
import graphics.TriangleBoundingBox;
import graphics.utils.GeometryUtils;

import java.util.*;

public class SpatialGrid {
    private final int cellSize;
    private final List<RenderableTriangle>[] cells;
    private final int cols;
    private final int rows;

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

    public void clear() {
        for (List<RenderableTriangle> cell : cells) {
            cell.clear();
        }
    }

    public void addTriangle(RenderableTriangle triangle) {
        if (!triangle.isVisibleFromCameraCenter()) {
            return;
        }

        TriangleBoundingBox triangleBoundingBox = GeometryUtils.getTriangleBoundingBox(triangle.getCurrentTriangle());

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

    public List<RenderableTriangle> getTriangles(int x, int y) {
        int cellX = x / cellSize;
        int cellY = y / cellSize;

        if (cellX >= 0 && cellX < cols && cellY >= 0 && cellY < rows) {
            return cells[cellY * cols + cellX];
        }

        return Collections.emptyList();
    }
}