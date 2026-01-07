package scene;

import core.math.Vector3D;

import java.awt.geom.Point2D;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Парсер файлов формата Wavefront OBJ.
 * <p>
 * Поддерживает загрузку 3D моделей с текстурами из OBJ файлов.
 * Формат OBJ должен содержать вершины (v), текстурные координаты (vt) и грани (f).
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class OBJParser {
    /**
     * Парсит файл формата Wavefront OBJ и создает из него объект {@link Mesh}.
     * <p>
     * Ожидает файл с расширением .obj следующего формата:
     * <pre>
     * v 1.0 2.0 3.0        # Вершина (x, y, z)
     * vt 0.5 0.5           # UV-координата (u, v)
     * f 1/1 2/2 3/3        # Грань: vertex_index/texture_index
     * </pre>
     * <p>
     * Требования к файлу:
     * - Должен содержать хотя бы один треугольник
     * - UV-координаты должны быть заданы для всех вершин граней
     * - Грань должна содержать ровно 3 вершины (только треугольники)
     *
     * @param filePath путь к OBJ файлу
     * @return объект Mesh, содержащий все треугольники модели
     * @throws IOException если файл не найден или произошла ошибка чтения
     * @throws IllegalArgumentException если формат файла не соответствует ожиданиям
     */
    public static Mesh parseOBJ(String filePath) throws IOException {
        List<Vector3D> vertices = new ArrayList<>();
        List<Point2D> texCoords = new ArrayList<>();
        List<RenderableTriangle> triangles = new ArrayList<>();

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
                        RenderableTriangle triangle = getTriangle(parts, vertices, texCoords);
                        triangles.add(triangle);
                    }
                }
            }
        }

        return new Mesh(triangles);
    }

    /**
     * Создает треугольник из строки грани OBJ формата.
     * <p>
     * Формат строки грани: {@code f v1/vt1 v2/vt2 v3/vt3}
     * где v1, v2, v3 - индексы вершин, vt1, vt2, vt3 - индексы текстурных координат.
     * <p>
     * Пример: {@code f 1/2 3/4 5/6}
     *
     * @param parts разделенная строка грани
     * @param vertices список всех вершин модели
     * @param texCoords список всех текстурных координат
     * @return готовый RenderableTriangle
     * @throws NumberFormatException если индексы не являются числами
     * @throws IndexOutOfBoundsException если индексы выходят за пределы списков
     */
    private static RenderableTriangle getTriangle(String[] parts, List<Vector3D> vertices, List<Point2D> texCoords) {
        String[] indices1 = parts[1].split("/");
        String[] indices2 = parts[2].split("/");
        String[] indices3 = parts[3].split("/");

        int vIdx1 = Integer.parseInt(indices1[0]) - 1;
        int vIdx2 = Integer.parseInt(indices2[0]) - 1;
        int vIdx3 = Integer.parseInt(indices3[0]) - 1;

        Vector3D p1 = vertices.get(vIdx1);
        Vector3D p2 = vertices.get(vIdx2);
        Vector3D p3 = vertices.get(vIdx3);

        List<Vector3D> points = new ArrayList<>();
        points.add(p1);
        points.add(p2);
        points.add(p3);

        int uvIdx1 = Integer.parseInt(indices1[1]) - 1;
        int uvIdx2 = Integer.parseInt(indices2[1]) - 1;
        int uvIdx3 = Integer.parseInt(indices3[1]) - 1;

        Point2D uv1 = texCoords.get(uvIdx1);
        Point2D uv2 = texCoords.get(uvIdx2);
        Point2D uv3 = texCoords.get(uvIdx3);

        List<Point2D> uvs = new ArrayList<>();

        uvs.add(uv1);
        uvs.add(uv2);
        uvs.add(uv3);

        return new RenderableTriangle(points, uvs);
    }
}
