package geometricObjects;

import java.util.*;

public class Mesh {
    private final float scale;
    private List<Triangle> triangles = new ArrayList<>();

    public Mesh(float scale, Triangle... triangles) {
        this.scale = scale;
        Collections.addAll(this.triangles, triangles);
    }

    public float getScale() {
        return scale;
    }

    public List<Triangle> getTriangles() {
        return triangles;
    }

    public void setTriangles(List<Triangle> triangles) {
        this.triangles = triangles;
    }

    @Override
    public String toString() {
        StringBuilder resultString = new StringBuilder();

        for (Triangle triangle : triangles) {
            resultString.append(triangle).append("\n");
        }

        return resultString.toString();
    }
}
