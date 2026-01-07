package graphics.shadows;

import graphics.config.RenderingConfig;
import core.base.CameraBase;
import core.math.Vector3D;

/**
 * Специализированная камера для рендеринга теней.
 * <p>
 * Наследует от {@link CameraBase} и фиксирует угол обзора (FOV) в соответствии с настройками теней.
 * Используется для рендеринга отдельных граней cube shadow maps.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class ShadowCamera extends CameraBase {
    /**
     * Создает теневую камеру в указанной позиции и ориентации.
     * <p>
     * Угол обзора (FOV) устанавливается из {@link RenderingConfig#SHADOW_CAMERA_FOV}.
     *
     * @param position позиция источника света (позиция камеры)
     * @param rotation вращение камеры (должно смотреть вдоль одной из осей: ±X, ±Y, ±Z)
     */
    public ShadowCamera(Vector3D position, Vector3D rotation) {
        super(position, rotation);
        fov = RenderingConfig.SHADOW_CAMERA_FOV;
    }

    @Override
    public String toString() {
        return String.format(
                "ShadowCamera[position=%s, rotation=%s, fov=%.2frad]",
                getTransform().getPosition(),
                getTransform().getRotation(),
                fov
        );
    }
}
