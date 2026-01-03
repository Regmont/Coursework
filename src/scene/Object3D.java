package scene;

import geometry.Mesh;
import geometry.OBJParser;
import geometry.Triangle;
import graphics.renderer.Material;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Object3D {
    private Mesh mesh;
    private final String modelName;

    public Object3D(String modelName) {
        this.modelName = modelName;

        try {
            String modelPath = "models/" + modelName + ".obj";

            mesh = OBJParser.parseOBJ(modelPath);

            String texturePath = "textures/" + modelName + ".png";
            File textureFile = new File(texturePath);

            Material material;

            if (textureFile.exists()) {
                BufferedImage texture = ImageIO.read(textureFile);
                material = new Material(texture);
            } else {
                material = new Material(game.configuration.ColorConfiguration.BROKEN_MODEL_COLOR, false);
            }

            for (Triangle triangle : mesh.triangles()) {
                triangle.setMaterial(material);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public Mesh getMesh() {
        return mesh;
    }
}