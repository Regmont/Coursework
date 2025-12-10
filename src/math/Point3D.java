package math;

public class Point3D {
    private final double x;
    private final double y;
    private final double z;

    public Point3D (double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Point3D (Point3D other) {
        this.x = other.x;
        this.y = other.y;
        this.z = other.z;
    }

    public double getX() {
        return x;
    }

    public double getY() {
        return y;
    }

    public double getZ() {
        return z;
    }

    @Override
    public String toString() {
        return "(x = " + x + ", y = " + y + ", z = " + z + ")";
    }
}
