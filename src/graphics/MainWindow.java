package graphics;

import graphics.renderer.GameRenderer;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;

/**
 * Главное окно приложения для отображения 3D графики.
 * <p>
 * Наследует от {@link Frame} и управляет циклом отрисовки.
 * Делегирует рендеринг {@link GameRenderer}, получая от него готовые кадры.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class MainWindow extends Frame {
    private final GameRenderer gameRenderer;

    /**
     * Создает главное окно для рендеринга 3D сцены.
     * <p>
     * Инициализирует окно с указанными параметрами и создает отрисовщик (рендерер)
     * для указанной системы сцены.
     *
     * @param sceneSystem   система сцены, содержащая объекты для отображения
     * @param title         заголовок окна
     * @param width         ширина окна в пикселях
     * @param height        высота окна в пикселях
     */
    public MainWindow(SceneSystem sceneSystem, String title, int width, int height) {
        gameRenderer = new GameRenderer(sceneSystem);

        setTitle(title);
        setSize(width, height);
        setLocationRelativeTo(null);

        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                System.exit(0);
            }
        });
    }

    /**
     * Отрисовывает текущий кадр в окне.
     * <p>
     * Получает готовое изображение кадра от {@link GameRenderer} и отображает его на всей площади окна.
     *
     * @param g графический контекст для рисования
     */
    @Override
    public void paint(Graphics g) {
        int width = getWidth();
        int height = getHeight();

        BufferedImage frame = gameRenderer.getFrame(width, height);

        g.drawImage(frame, 0, 0, null);
    }

    /**
     * Переопределяет стандартный update для устранения мерцания.
     * <p>
     * Вместо очистки фона и вызова update + paint, напрямую вызывает paint.
     *
     * @param g графический контекст
     */
    @Override
    public void update(Graphics g) {
        paint(g);
    }

    @Override
    public String toString() {
        return String.format(
                "MainWindow[title=%s, size=%dx%d, renderer=%s]",
                getTitle(),
                getWidth(),
                getHeight(),
                gameRenderer.getClass().getSimpleName()
        );
    }
}
