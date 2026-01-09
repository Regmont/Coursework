package scene;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * Класс для представления 3D объекта, загружаемого из внешних файлов.
 * <p>
 * Автоматически загружает геометрию из OBJ файла и текстуру из PNG файла.
 * Объединяет их в готовый для рендеринга {@link Mesh} с {@link Material}.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class Object3D {
    private Mesh mesh;

    /**
     * Создает 3D объект, загружая модель и текстуру с одинаковыми именами.
     * <p>
     * Ищет файлы:
     * <ul>
     *   <li>models/{modelName}.obj - геометрия</li>
     *   <li>textures/{modelName}.png - текстура (если существует)</li>
     * </ul>
     *
     * @param modelName имя модели (без расширения)
     */
    public Object3D(String modelName) {
        parseMeshFromFiles(modelName, modelName);
    }

    /**
     * Создает 3D объект с указанными именами модели и текстуры.
     * <p>
     * Ищет файлы:
     * <ul>
     *   <li>models/{modelName}.obj - геометрия</li>
     *   <li>textures/{textureName}.png - текстура (если существует)</li>
     * </ul>
     *
     * @param modelName     имя файла модели (без расширения)
     * @param textureName   имя файла текстуры (без расширения)
     */
    public Object3D(String modelName, String textureName) {
        parseMeshFromFiles(modelName, textureName);
    }

    public Mesh getMesh() {
        return mesh;
    }

    /**
     * Загружает модель из OBJ файла и текстуру из PNG файла.
     * <p>
     * Создает материал с текстурой (если PNG найден) или без (если не найден).
     * Материал применяется ко всем треугольникам модели.
     *
     * @param modelName     имя файла модели (без .obj)
     * @param textureName   имя файла текстуры (без .png)
     * @throws RuntimeException если не удалось загрузить OBJ файл
     */
    private void parseMeshFromFiles(String modelName, String textureName) {
        try {
            String modelPath = modelName + ".obj";
            mesh = OBJParser.parseOBJ(modelPath);

            String texturePath = textureName + ".png";
            Material material = loadTextureResource(texturePath);

            mesh.setMaterial(material);

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Model loading error: " + modelName, e);
        }
    }

    /**
     * Загружает текстуру из ресурсов или файловой системы.
     * Работает как в IDE, так и в JAR-файле.
     *
     * @param texturePath путь к файлу текстуры
     * @return материал с текстурой или null-текстурой, если не найден
     * @throws IOException если произошла ошибка при чтении файла
     */
    private Material loadTextureResource(String texturePath) throws IOException {
        InputStream resourceStream = getClass().getResourceAsStream("/" + texturePath);

        if (resourceStream != null) {
            try (InputStream is = resourceStream) {
                BufferedImage texture = ImageIO.read(is);
                if (texture != null) {
                    return new Material(texture);
                }
            }
        }

        File textureFile = new File(texturePath);
        if (textureFile.exists() && textureFile.isFile()) {
            BufferedImage texture = ImageIO.read(textureFile);
            if (texture != null) {
                return new Material(texture);
            }
        }

        System.err.println("Texture not found: " + texturePath);

        return new Material(null);
    }
}
