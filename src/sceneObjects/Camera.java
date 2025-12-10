package sceneObjects;

import math.Point3D;

public class Camera {
    private double x;
    private double y;
    private double z;
    private double yaw;
    private double pitch;

    public Camera(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = 0;
        this.pitch = 0;
    }

    public Camera(double x, double y, double z, double yaw, double pitch) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.yaw = yaw;
        this.pitch = pitch;
    }

    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    public double getYaw() { return yaw; }

    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public void addRotation(double deltaYaw, double deltaPitch) {
        this.yaw += deltaYaw;
        this.pitch += deltaPitch;

        double maxPitch = Math.PI / 2.0 - 0.01;
        double minPitch = -Math.PI / 2.0 + 0.01;
        this.pitch = Math.max(minPitch, Math.min(maxPitch, this.pitch));
    }

    public Point3D getLookAtPoint() {
        double lookX = x + Math.cos(pitch) * Math.sin(yaw);
        double lookY = y + Math.sin(pitch);
        double lookZ = z + Math.cos(pitch) * Math.cos(yaw);

        return new Point3D(lookX, lookY, lookZ);
    }
}