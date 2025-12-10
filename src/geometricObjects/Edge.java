package geometricObjects;

import math.Point3D;

public class Edge {
    private final Point3D p1;
    private final Point3D p2;
    private final Triangle triangle1;
    private Triangle triangle2;

    public Edge(Point3D p1, Point3D p2, Triangle triangle) {
        this.p1 = p1;
        this.p2 = p2;
        this.triangle1 = triangle;
        this.triangle2 = null;
    }

    public Point3D getP1() {
        return p1;
    }

    public Point3D getP2() {
        return p2;
    }

    public Triangle getTriangle1() {
        return triangle1;
    }

    public Triangle getTriangle2() {
        return triangle2;
    }

    public void setTriangle2(Triangle triangle2) {
        this.triangle2 = triangle2;
    }

    public boolean hasTwoTriangles() {
        return triangle2 != null;
    }

    public boolean isBoundaryEdge() {
        if (triangle2 == null) {
            return true;
        }

        boolean triangle1Visible = triangle1.isVisibleFromCameraCenter();
        boolean triangle2Visible = triangle2.isVisibleFromCameraCenter();

        return triangle1Visible != triangle2Visible;
    }

    public Triangle getVisibleTriangle() {
        if (triangle1.isVisibleFromCameraCenter()) {
            return triangle1;
        } else if (triangle2 != null && triangle2.isVisibleFromCameraCenter()) {
            return triangle2;
        }

        return null;
    }

    public boolean equalsIgnoreOrder(Edge other) {
        return (this.p1.equals(other.p1) && this.p2.equals(other.p2)) ||
                (this.p1.equals(other.p2) && this.p2.equals(other.p1));
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Edge other = (Edge) obj;

        return this.equalsIgnoreOrder(other) && this.triangle1 == other.triangle1;
    }

    @Override
    public int hashCode() {
        long h = Double.doubleToLongBits(p1.getX() + p2.getX()) ^
                Double.doubleToLongBits(p1.getY() + p2.getY()) ^
                Double.doubleToLongBits(p1.getZ() + p2.getZ());

        return Long.hashCode(h);
    }

    @Override
    public String toString() {
        return "Edge[" + p1 + " - " + p2 + "]";
    }
}
