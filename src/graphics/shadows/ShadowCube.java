package graphics.shadows;

import math.Vector3D;

public class ShadowCube {
    private final ShadowCubeFace[] faces;

    public ShadowCube(Vector3D position) {
        faces = new ShadowCubeFace[6];

        for (int i = 0; i < 6; i++) {
            Vector3D rotation = getRotationForFace(i);
            ShadowCamera camera = new ShadowCamera(position, rotation);
            faces[i] = new ShadowCubeFace(camera);
        }
    }

    public void updatePosition(Vector3D newPosition) {
        for (ShadowCubeFace face : faces) {
            face.getCamera().getTransform().setPosition(newPosition);
        }
    }

    public ShadowCubeFace[] getFaces() {
        return faces;
    }

    private Vector3D getRotationForFace(int faceIndex) {
        return switch (faceIndex) {
            case 0 -> new Vector3D(0, -Math.PI/2, 0);    // +X: yaw=-90°, pitch=0
            case 1 -> new Vector3D(0, Math.PI/2, 0);     // -X: yaw=+90°, pitch=0
            case 2 -> new Vector3D(-Math.PI/2, 0, 0);     // +Y: pitch=+90°, yaw=0
            case 3 -> new Vector3D(Math.PI/2, 0, 0);    // -Y: pitch=-90°, yaw=0
            case 4 -> new Vector3D(0, 0, 0);             // +Z: pitch=0, yaw=0
            case 5 -> new Vector3D(0, Math.PI, 0);       // -Z: yaw=180°, pitch=0
            default -> Vector3D.zeroVector;
        };
    }
}
