package graphics;

import java.util.*;

public record Mesh(List<RenderableTriangle> triangles) {

    public void setMaterial(Material material) {
        for (RenderableTriangle triangle : triangles) {
            triangle.setMaterial(material);
        }
    }

    public Mesh getCopy() {
        List<RenderableTriangle> newTriangles = new ArrayList<>();

        for (RenderableTriangle triangle : triangles) {
            RenderableTriangle newTriangle = new RenderableTriangle(
                    triangle.getOriginalPoints(),
                    triangle.getUVs()
            );

            newTriangle.setMaterial(triangle.getMaterial());

            newTriangles.add(newTriangle);
        }

        return new Mesh(newTriangles);
    }
}
