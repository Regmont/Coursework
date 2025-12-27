package graphics.renderer;

import geometry.*;

import java.util.*;

public class SpatialGrid {
    private final int cellSize;
    private final ArrayList[][] grid;
    private final int gridCols;
    private final int gridRows;

    public SpatialGrid(int screenWidth, int screenHeight, int cellSize) {
        this.cellSize = cellSize;
        this.gridCols = (screenWidth + cellSize - 1) / cellSize;
        this.gridRows = (screenHeight + cellSize - 1) / cellSize;

        grid = new ArrayList[gridCols][gridRows];
        for (int i = 0; i < gridCols; i++) {
            for (int j = 0; j < gridRows; j++) {
                grid[i][j] = new ArrayList<>();
            }
        }
    }

    public void clear() {
        for (int i = 0; i < gridCols; i++) {
            for (int j = 0; j < gridRows; j++) {
                grid[i][j].clear();
            }
        }
    }

    public void addTriangle(Triangle triangle) {
        if (!triangle.isVisibleFromCameraCenter()) {
            return;
        }

        BoundingBox boundingBox = triangle.getBoundingBox();

        int minCellX = Math.max(0, boundingBox.minX() / cellSize);
        int maxCellX = Math.min(gridCols - 1, boundingBox.maxX() / cellSize);
        int minCellY = Math.max(0, boundingBox.minY() / cellSize);
        int maxCellY = Math.min(gridRows - 1, boundingBox.maxY() / cellSize);

        for (int cx = minCellX; cx <= maxCellX; cx++) {
            for (int cy = minCellY; cy <= maxCellY; cy++) {
                grid[cx][cy].add(triangle);
            }
        }
    }

    public List<Triangle> getTriangles(int x, int y) {
        int cellX = x / cellSize;
        int cellY = y / cellSize;

        if (cellX >= 0 && cellX < gridCols && cellY >= 0 && cellY < gridRows) {
            return grid[cellX][cellY];
        }

        return Collections.emptyList();
    }
}