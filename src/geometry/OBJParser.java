package geometry;

import math.Vector3D;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class OBJParser {
    public static Mesh parseOBJ(String filePath) throws IOException {
        List<Vector3D> vertices = new ArrayList<>();
        List<Point2D> texCoords = new ArrayList<>();
        List<Triangle> triangles = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;

            while ((line = br.readLine()) != null) {
                line = line.trim();

                String[] parts = line.split("\\s+");

                switch (parts[0]) {
                    case "v" -> {
                        double x = Double.parseDouble(parts[1]);
                        double y = Double.parseDouble(parts[2]);
                        double z = Double.parseDouble(parts[3]);

                        vertices.add(new Vector3D(x, y, z));
                    }
                    case "vt" -> {
                        double u = Double.parseDouble(parts[1]);
                        double v = Double.parseDouble(parts[2]);

                        texCoords.add(new Point2D.Double(u, v));
                    }
                    case "f" -> {
                        String[] indices1 = parts[1].split("/");
                        String[] indices2 = parts[2].split("/");
                        String[] indices3 = parts[3].split("/");

                        int vIdx1 = Integer.parseInt(indices1[0]) - 1;
                        int vIdx2 = Integer.parseInt(indices2[0]) - 1;
                        int vIdx3 = Integer.parseInt(indices3[0]) - 1;

                        Vector3D p1 = vertices.get(vIdx1);
                        Vector3D p2 = vertices.get(vIdx2);
                        Vector3D p3 = vertices.get(vIdx3);

                        int uvIdx1 = Integer.parseInt(indices1[1]) - 1;
                        int uvIdx2 = Integer.parseInt(indices2[1]) - 1;
                        int uvIdx3 = Integer.parseInt(indices3[1]) - 1;

                        Point2D uv1 = texCoords.get(uvIdx1);
                        Point2D uv2 = texCoords.get(uvIdx2);
                        Point2D uv3 = texCoords.get(uvIdx3);

                        Triangle triangle = new Triangle(p1, p2, p3, uv1, uv2, uv3);
                        triangles.add(triangle);
                    }
                }
            }
        }

        return new Mesh(triangles);
    }
}