package graphics;

public record TriangleBoundingBox(int minX, int maxX, int minY, int maxY) {
    public static TriangleBoundingBox clampToScreen(TriangleBoundingBox triangleBoundingBox, int width, int height) {
        int minX = Math.max(0, triangleBoundingBox.minX());
        int maxX = Math.min(width - 1, triangleBoundingBox.maxX());
        int minY = Math.max(0, triangleBoundingBox.minY());
        int maxY = Math.min(height - 1, triangleBoundingBox.maxY());

        return new TriangleBoundingBox(minX, maxX, minY, maxY);
    }
}