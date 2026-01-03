package graphics.shadows;

import math.Vector3D;

public class ShadowCube {
    private final ShadowCubeFace[] faces = new ShadowCubeFace[6];

    public ShadowCube(Vector3D position) {
        for (int i = 0; i < 6; i++) {
            ShadowCamera camera = createCameraForFace(position, i);
            faces[i] = new ShadowCubeFace(camera);
        }
    }

    private ShadowCamera createCameraForFace(Vector3D position, int faceIndex) {
        Vector3D direction = getDirectionForFace(faceIndex);
        return new ShadowCamera(position, direction);
    }

    private Vector3D getDirectionForFace(int faceIndex) {
        return switch (faceIndex) {
            case 0 -> new Vector3D(1, 0, 0);   // +X
            case 1 -> new Vector3D(-1, 0, 0);  // -X
            case 2 -> new Vector3D(0, 1, 0);   // +Y
            case 3 -> new Vector3D(0, -1, 0);  // -Y
            case 4 -> new Vector3D(0, 0, 1);   // +Z
            case 5 -> new Vector3D(0, 0, -1);  // -Z
            default -> Vector3D.zeroVector;
        };
    }

    public ShadowCubeFace getFace(int index) {
        return faces[index];
    }

    public ShadowCubeFace[] getFaces() {
        return faces;
    }
}
