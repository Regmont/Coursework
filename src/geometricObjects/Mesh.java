package geometricObjects;

import math.Point3D;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class Mesh {
    private final float scale;
    private ArrayList<Triangle> triangles = new ArrayList<>();
    private final ArrayList<Edge> edges = new ArrayList<>();
    private boolean edgesCalculated = false;

    public Mesh(float scale, Triangle... triangles) {
        this.scale = scale;
        Collections.addAll(this.triangles, triangles);
    }

    public float getScale() {
        return scale;
    }

    public ArrayList<Triangle> getTriangles() {
        return triangles;
    }

    public void setTriangles(ArrayList<Triangle> triangles) {
        this.triangles = triangles;
        edgesCalculated = false;
    }

    public ArrayList<Edge> getEdges() {
        if (edgesCalculated && !edges.isEmpty()) {
            return edges;
        }

        edges.clear();
        Map<String, Edge> edgeMap = new HashMap<>();

        for (Triangle triangle : triangles) {
            ArrayList<Point3D> points = triangle.getPoints();

            Edge[] triangleEdges = new Edge[3];
            triangleEdges[0] = new Edge(points.get(0), points.get(1), triangle);
            triangleEdges[1] = new Edge(points.get(1), points.get(2), triangle);
            triangleEdges[2] = new Edge(points.get(2), points.get(0), triangle);

            for (Edge edge : triangleEdges) {
                String key = createEdgeKey(edge.getP1(), edge.getP2());

                if (edgeMap.containsKey(key)) {
                    Edge existingEdge = edgeMap.get(key);
                    existingEdge.setTriangle2(triangle);
                } else {
                    edgeMap.put(key, edge);
                    edges.add(edge);
                }
            }
        }

        edgesCalculated = true;

        return edges;
    }

    private String createEdgeKey(Point3D p1, Point3D p2) {
        String key1 = p1.getX() + "," + p1.getY() + "," + p1.getZ() + "|" +
                p2.getX() + "," + p2.getY() + "," + p2.getZ();
        String key2 = p2.getX() + "," + p2.getY() + "," + p2.getZ() + "|" +
                p1.getX() + "," + p1.getY() + "," + p1.getZ();

        return key1.compareTo(key2) < 0 ? key1 : key2;
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
