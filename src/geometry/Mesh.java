package geometry;

import graphics.renderer.Material;

import java.util.*;

public record Mesh(List<Triangle> triangles) {

    public void setMaterial(Material material) {
        for (Triangle triangle : triangles) {
            triangle.setMaterial(material);
        }
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
