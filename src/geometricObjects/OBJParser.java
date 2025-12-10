package geometricObjects;

import math.Point3D;

import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;
import java.util.List;

public class OBJParser {
    public static Mesh parseOBJ(String filePath, float scale, Color color) throws IOException {
        List<Point3D> vertices = new ArrayList<>();
        ArrayList<Triangle> triangles = new ArrayList<>();

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();

                if (line.startsWith("v ")) {
                    String[] parts = line.split("\\s+");
                    double x = Double.parseDouble(parts[1]) * scale;
                    double y = Double.parseDouble(parts[2]) * scale;
                    double z = Double.parseDouble(parts[3]) * scale;
                    vertices.add(new Point3D(x, y, z));

                } else if (line.startsWith("f ")) {
                    String[] parts = line.split("\\s+");

                    int idx1 = Integer.parseInt(parts[1].split("/")[0]) - 1;
                    int idx2 = Integer.parseInt(parts[2].split("/")[0]) - 1;
                    int idx3 = Integer.parseInt(parts[3].split("/")[0]) - 1;

                    Point3D p1 = vertices.get(idx1);
                    Point3D p2 = vertices.get(idx2);
                    Point3D p3 = vertices.get(idx3);

                    Triangle triangle = new Triangle(p1, p2, p3, color);
                    triangles.add(triangle);
                }
            }
        }

        Mesh mesh = new Mesh(scale);
        mesh.setTriangles(triangles);
        return mesh;
    }
}