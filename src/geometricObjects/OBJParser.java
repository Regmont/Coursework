package geometricObjects;

import math.Point3D;
import rendering.Material;

import javax.imageio.ImageIO;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OBJParser {

    public static Mesh parseOBJ(String filePath, float scale) throws IOException {
        File objFile = new File(filePath);
        String baseDir = objFile.getParent();

        List<Point3D> vertices = new ArrayList<>();
        List<Point2D> texCoords = new ArrayList<>();
        List<Triangle> triangles = new ArrayList<>();

        Map<String, Material> materials = new HashMap<>();
        Material currentMaterial = new Material(Color.WHITE);
        String currentMtllib;

        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("\\s+");

                switch (parts[0]) {
                    case "mtllib" -> {
                        currentMtllib = line.substring(6).trim();
                        String mtlPath = baseDir + File.separator + currentMtllib;
                        materials.putAll(parseMTL(mtlPath, baseDir));
                    }
                    case "usemtl" -> {
                        String materialName = line.substring(6).trim();
                        currentMaterial = materials.getOrDefault(materialName, new Material(Color.WHITE));
                    }
                    case "v" -> {
                        double x = Double.parseDouble(parts[1]) * scale;
                        double y = Double.parseDouble(parts[2]) * scale;
                        double z = Double.parseDouble(parts[3]) * scale;
                        vertices.add(new Point3D(x, y, z));
                    }
                    case "vt" -> {
                        double u = Double.parseDouble(parts[1]);
                        double v = Double.parseDouble(parts[2]);
                        texCoords.add(new Point2D.Double(u, v));
                    }
                    case "f" -> {
                        if (parts.length < 4) continue;

                        String[] indices1 = parts[1].split("/");
                        String[] indices2 = parts[2].split("/");
                        String[] indices3 = parts[3].split("/");

                        int idx1 = Integer.parseInt(indices1[0]) - 1;
                        int idx2 = Integer.parseInt(indices2[0]) - 1;
                        int idx3 = Integer.parseInt(indices3[0]) - 1;

                        Point3D p1 = vertices.get(idx1);
                        Point3D p2 = vertices.get(idx2);
                        Point3D p3 = vertices.get(idx3);

                        Point2D uv1 = null, uv2 = null, uv3 = null;

                        if (indices1.length > 1 && !indices1[1].isEmpty()) {
                            int uvIdx = Integer.parseInt(indices1[1]) - 1;
                            if (uvIdx >= 0 && uvIdx < texCoords.size()) uv1 = texCoords.get(uvIdx);
                        }

                        if (indices2.length > 1 && !indices2[1].isEmpty()) {
                            int uvIdx = Integer.parseInt(indices2[1]) - 1;
                            if (uvIdx >= 0 && uvIdx < texCoords.size()) uv2 = texCoords.get(uvIdx);
                        }

                        if (indices3.length > 1 && !indices3[1].isEmpty()) {
                            int uvIdx = Integer.parseInt(indices3[1]) - 1;
                            if (uvIdx >= 0 && uvIdx < texCoords.size()) uv3 = texCoords.get(uvIdx);
                        }

                        if (uv1 == null || uv2 == null || uv3 == null) {
                            uv1 = new Point2D.Double(0, 0);
                            uv2 = new Point2D.Double(1, 0);
                            uv3 = new Point2D.Double(0, 1);
                        }

                        Point3D[] originalPoints = new Point3D[]{p1, p2, p3};
                        Triangle triangle = new Triangle(p1, p2, p3, currentMaterial,
                                originalPoints, uv1, uv2, uv3);
                        triangles.add(triangle);
                    }
                }
            }
        }

        Mesh mesh = new Mesh(scale);
        mesh.setTriangles(triangles);
        return mesh;
    }

    private static Map<String, Material> parseMTL(String mtlPath, String baseDir) throws IOException {
        Map<String, Material> materials = new HashMap<>();

        if (!new File(mtlPath).exists()) {
            return materials;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(mtlPath))) {
            String line;
            String currentName = null;
            Color diffuseColor = Color.WHITE;
            String texturePath = null;

            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) {
                    continue;
                }

                String[] parts = line.split("\\s+");

                if (parts[0].equals("newmtl")) {
                    if (currentName != null) {
                        materials.put(currentName, createMaterial(diffuseColor, texturePath, baseDir));
                    }
                    currentName = parts[1];
                    diffuseColor = Color.WHITE;
                    texturePath = null;
                }
                else if (parts[0].equals("Kd") && currentName != null) {
                    float r = Float.parseFloat(parts[1]);
                    float g = Float.parseFloat(parts[2]);
                    float b = Float.parseFloat(parts[3]);
                    diffuseColor = new Color(r, g, b);
                }
                else if (parts[0].equals("map_Kd") && currentName != null) {
                    texturePath = parts[1];
                }
            }

            if (currentName != null) {
                materials.put(currentName, createMaterial(diffuseColor, texturePath, baseDir));
            }
        }

        return materials;
    }

    private static Material createMaterial(Color diffuseColor, String texturePath, String baseDir) {
        if (texturePath != null && !texturePath.isEmpty()) {
            try {
                String fullPath = baseDir + File.separator + texturePath;
                BufferedImage texture = ImageIO.read(new File(fullPath));
                if (texture != null) {
                    return new Material(texture);
                }
            } catch (IOException e) {
                System.err.println("Failed to load texture: " + texturePath);
            }
        }
        return new Material(diffuseColor);
    }
}