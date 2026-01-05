package scene;

import geometry.Mesh;
import geometry.OBJParser;
import geometry.Material;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Object3D {
    private Mesh mesh;

    public Object3D(String modelName) {
        parseMeshFromFiles(modelName, modelName);
    }

    public Object3D(String modelName, String textureName) {
        parseMeshFromFiles(modelName, textureName);
    }

    public Mesh getMesh() {
        return mesh;
    }

    private void parseMeshFromFiles(String modelName, String textureName) {
        try {
            String modelPath = "models/" + modelName + ".obj";

            mesh = OBJParser.parseOBJ(modelPath);

            String texturePath = "textures/" + textureName + ".png";
            File textureFile = new File(texturePath);

            Material material;

            if (textureFile.exists()) {
                BufferedImage texture = ImageIO.read(textureFile);
                material = new Material(texture);
            } else {
                material = new Material(null);
            }

            mesh.setMaterial(material);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}