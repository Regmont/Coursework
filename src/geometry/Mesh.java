package geometry;

import java.util.*;

public record Mesh(List<Triangle> triangles) {

    public void setMaterial(Material material) {
        for (Triangle triangle : triangles) {
            triangle.setMaterial(material);
        }
    }

    public Mesh getCopy() {
        List<Triangle> newTriangles = new ArrayList<>();

        for (Triangle triangle : triangles) {
            Triangle newTriangle = new Triangle(
                    triangle.getPoints().get(0),
                    triangle.getPoints().get(1),
                    triangle.getPoints().get(2),
                    triangle.getUV1(),
                    triangle.getUV2(),
                    triangle.getUV3()
            );

            newTriangle.setMaterial(triangle.getMaterial());

            newTriangles.add(newTriangle);
        }

        return new Mesh(newTriangles);
    }
}
