package geometry;

import java.util.*;

public record Mesh(List<Triangle> triangles) {

    @Override
    public String toString() {
        StringBuilder resultString = new StringBuilder();

        for (Triangle triangle : triangles) {
            resultString.append(triangle).append("\n");
        }

        return resultString.toString();
    }
}
