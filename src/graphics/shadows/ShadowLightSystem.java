package graphics.shadows;

import core.math.Vector3D;
import scene.gameObjects.PointLight;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Система управления тенями для точечных источников света.
 * <p>
 * Связывает каждый источник света с его cube shadow map ({@link ShadowCube}).
 * Автоматически обновляет позиции теневых карт при перемещении источников света.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class ShadowLightSystem {
    private final Map<PointLight, ShadowCube> lightToShadowCube = new HashMap<>();

    /**
     * Создает систему теней для указанных источников света.
     * <p>
     * Для каждого источника света создает cube shadow map в его текущей позиции.
     *
     * @param lights список источников света, которые должны отбрасывать тени
     */
    public ShadowLightSystem(List<PointLight> lights) {
        for (PointLight light : lights) {
            ShadowCube shadowCube = new ShadowCube(light.getTransform().getPosition());
            lightToShadowCube.put(light, shadowCube);
        }
    }

    /**
     * Возвращает связь источников света с их тенями.
     * <p>
     * Перед возвратом обновляет позиции всех cube shadow maps в соответствии
     * с текущими позициями источников света.
     *
     * @return неизменяемое отображение "источник света → его shadow cube"
     */
    public Map<PointLight, ShadowCube> getLightToShadowCube() {
        updateShadowCube();

        return lightToShadowCube;
    }

    /**
     * Обновляет позиции всех cube shadow maps.
     * <p>
     * Синхронизирует позиции теневых карт с позициями источников света.
     * Должен вызываться каждый кадр перед рендерингом теней.
     */
    private void updateShadowCube() {
        for (Map.Entry<PointLight, ShadowCube> entry : lightToShadowCube.entrySet()) {
            PointLight light = entry.getKey();
            ShadowCube cube = entry.getValue();

            Vector3D lightPos = light.getTransform().getPosition();
            cube.updatePosition(lightPos);
        }
    }

    @Override
    public String toString() {
        return String.format(
                "ShadowLightSystem[lights=%d]",
                lightToShadowCube.size()
        );
    }
}
