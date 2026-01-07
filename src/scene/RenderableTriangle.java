package scene;

import core.math.Triangle;
import core.math.Vector3D;

import java.awt.geom.Point2D;
import java.util.List;

public class RenderableTriangle {
    private final Triangle<Vector3D> originalPoints;
    private final Triangle<Vector3D> currentPoints;
    private Material material;
    private final Triangle<Point2D> uvs;
    private final Triangle<Double> invWs;
    private Vector3D cameraNormal;
    private Vector3D worldNormal;

    public RenderableTriangle(List<Vector3D> originalPoints, List<Vector3D> currentPoints, Material material,
                              List<Point2D> uvs, List<Double> invWs) {
        this.originalPoints = new Triangle<>(originalPoints);
        this.currentPoints = new Triangle<>(currentPoints);
        this.material = material;
        this.uvs = new Triangle<>(uvs);
        this.invWs = new Triangle<>(invWs);
        this.cameraNormal = null;
        this.worldNormal = calculateWorldNormal();
    }

    public RenderableTriangle(List<Vector3D> originalPoints, List<Point2D> uvs) {
        this.originalPoints = new Triangle<>(originalPoints);
        this.currentPoints = new Triangle<>(originalPoints);
        this.material = Material.DEFAULT_MATERIAL;
        this.uvs = new Triangle<>(uvs);
        this.invWs = new Triangle<>(1d, 1d, 1d);
    }

    public List<Vector3D> getOriginalPoints() {
        return originalPoints.getPoints();
    }

    public List<Vector3D> getCurrentPoints() {
        return currentPoints.getPoints();
    }

    public Triangle<Vector3D> getOriginalTriangle() {
        return originalPoints;
    }

    public Triangle<Vector3D> getCurrentTriangle() {
        return currentPoints;
    }

    public boolean hasUV() {
        return uvs.getPoints().size() == 3;
    }

    public List<Point2D> getUVs() {
        return uvs.getPoints();
    }

    public List<Double> getInvWs() {
        return invWs.getPoints();
    }

    public Material getMaterial() {
        return material;
    }

    public void setMaterial(Material material) {
        this.material = material;
    }

    public boolean isVisibleFromCameraCenter() {
        return calculateCameraNormal().getZ() < 0;
    }

    public Vector3D getWorldNormal() {
        return new Vector3D(worldNormal);
    }

    private Vector3D calculateWorldNormal() {
        List<Vector3D> points = originalPoints.getPoints();

        return Triangle.calculateNormal(points.get(0), points.get(1), points.get(2));
    }

    private Vector3D calculateCameraNormal() {
        if (cameraNormal != null) {
            return new Vector3D(cameraNormal);
        }

        List<Vector3D> points = currentPoints.getPoints();
        cameraNormal = Triangle.calculateNormal(points.get(0), points.get(1), points.get(2));

        return new Vector3D(cameraNormal);
    }
}
