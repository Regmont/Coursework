package scene;

import game.configuration.GameConfig;
import graphics.Material;
import core.base.ObjectInstance;
import core.math.Vector3D;

import java.awt.*;

public class PointLight extends ObjectInstance {
    private SimpleObject object;
    private Color color;
    private double intensity;

    public PointLight(Vector3D position) {
        super(position, Vector3D.zeroVector, Vector3D.unitVector);

        color = GameConfig.POINT_LIGHT_COLOR;
        intensity = GameConfig.POINT_LIGHT_INTENSITY;
    }

    public boolean hasObject() {
        return object != null;
    }


    public SimpleObject getObject() {
        return object;
    }

    public Color getColor() {
        return color;
    }

    public double getIntensity() {
        return intensity;
    }

    public void setObject(SimpleObject object) {
        this.object = object;
        this.object.getTransform().setPosition(getTransform().getPosition());
        this.object.getMesh().setMaterial(new Material(color, true));
    }

    public void setColor(Color color) {
        this.color = color;
        object.getMesh().setMaterial(new Material(color, true));
    }

    public void setIntensity(double intensity) {
        this.intensity = intensity;
    }
}