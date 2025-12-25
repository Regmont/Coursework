package geometry;

import java.util.*;

public class Mesh {
    private List<Triangle> triangles = new ArrayList<>();

    public Mesh(List<Triangle> triangles) {
        this.triangles = triangles;
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
