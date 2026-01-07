package graphics.shadows;

import core.math.Vector3D;

/**
 * Cube shadow map для точечного источника света.
 * <p>
 * Состоит из 6 отдельных shadow maps (граней), направленных по осям +X, -X, +Y, -Y, +Z, -Z.
 * Используется для создания теней от точечных (omnidirectional) источников света.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class ShadowCube {
    private final ShadowCubeFace[] faces;

    /**
     * Создает cube shadow map в указанной позиции.
     * <p>
     * Инициализирует 6 граней, каждая со своей камерой, направленной
     * вдоль соответствующей оси мировой системы координат.
     *
     * @param position позиция источника света (центр cube map)
     */
    public ShadowCube(Vector3D position) {
        faces = new ShadowCubeFace[6];

        for (int i = 0; i < 6; i++) {
            Vector3D rotation = getRotationForFace(i);
            ShadowCamera camera = new ShadowCamera(position, rotation);
            faces[i] = new ShadowCubeFace(camera);
        }
    }

    /**
     * Обновляет позицию всех граней cube map.
     * <p>
     * Используется при перемещении источника света для синхронизации
     * позиций теневых камер с позицией света.
     *
     * @param newPosition новая позиция источника света
     */
    public void updatePosition(Vector3D newPosition) {
        for (ShadowCubeFace face : faces) {
            face.getCamera().getTransform().setPosition(newPosition);
        }
    }

    /**
     * Возвращает массив граней cube map.
     *
     * @return массив из 6 граней в порядке: +X, -X, +Y, -Y, +Z, -Z
     */
    public ShadowCubeFace[] getFaces() {
        return faces;
    }

    /**
     * Возвращает вращение камеры для указанной грани cube map.
     * <p>
     * Система координат камеры:
     * - X положительное → ВЛЕВО (right = (-1, 0, 0))
     * - Y положительное → ВВЕРХ (up = (0, 1, 0))
     * - Z положительное → ВПЕРЁД (forward = (0, 0, 1))
     *
     * @param faceIndex индекс грани (0-5)
     * @return углы Эйлера (pitch, yaw, roll) в радианах для камеры
     */
    private Vector3D getRotationForFace(int faceIndex) {
        return switch (faceIndex) {
            case 0 -> new Vector3D(0, -Math.PI/2, 0);
            case 1 -> new Vector3D(0, Math.PI/2, 0);
            case 2 -> new Vector3D(-Math.PI/2, 0, 0);
            case 3 -> new Vector3D(Math.PI/2, 0, 0);
            case 4 -> new Vector3D(0, 0, 0);
            case 5 -> new Vector3D(0, Math.PI, 0);
            default -> Vector3D.zeroVector;
        };
    }

    @Override
    public String toString() {
        return String.format(
                "ShadowCube[faces=%d, position=%s]",
                faces.length,
                faces[0].getCamera().getTransform().getPosition()
        );
    }
}
