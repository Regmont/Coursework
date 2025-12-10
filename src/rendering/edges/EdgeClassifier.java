package rendering.edges;

import geometricObjects.*;
import java.util.*;

public class EdgeClassifier {
    public Map<Edge, EdgeType> collectEdgeTypes(List<Mesh> objects) {
        Map<Edge, EdgeType> edgeTypes = new HashMap<>();

        for (Mesh object : objects) {
            ArrayList<Edge> edges = object.getEdges();
            for (Edge edge : edges) {
                if (edge.isBoundaryEdge()) {
                    edgeTypes.put(edge, EdgeType.BOUNDARY);
                } else if (edge.hasTwoTriangles()) {
                    boolean triangle1Visible = edge.getTriangle1().isVisibleFromCameraCenter();
                    boolean triangle2Visible = edge.getTriangle2().isVisibleFromCameraCenter();

                    if (triangle1Visible && triangle2Visible) {
                        edgeTypes.put(edge, EdgeType.INTERIOR);
                    }
                }
            }
        }

        return edgeTypes;
    }
}