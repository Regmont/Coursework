package graphics;

import graphics.config.AppConfig;
import scene.SceneSystem;

/**
 * Основная система рендеринга, управляющая окном приложения.
 * <p>
 * Инициализирует и управляет главным окном отрисовки ({@link MainWindow}).
 * Обеспечивает связь между системой сцены ({@link SceneSystem}) и отображением.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class RenderingSystem {
    private final MainWindow window;

    /**
     * Создает систему рендеринга с параметрами по умолчанию.
     * <p>
     * Использует настройки из {@link AppConfig}:
     * - Заголовок окна
     * - Ширина окна
     * - Высота окна
     *
     * @param sceneSystem система сцены, содержащая объекты для отображения
     */
    public RenderingSystem(SceneSystem sceneSystem) {
        window = new MainWindow(sceneSystem, AppConfig.WINDOW_TITLE,
                AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);
        window.setVisible(true);
    }

    /**
     * Создает систему рендеринга с пользовательскими параметрами окна.
     *
     * @param sceneSystem   система сцены, содержащая объекты для отображения
     * @param windowTitle   заголовок окна приложения
     * @param windowWidth   ширина окна в пикселях
     * @param windowHeight  высота окна в пикселях
     */
    public RenderingSystem(SceneSystem sceneSystem, String windowTitle, int windowWidth, int windowHeight) {
        window = new MainWindow(sceneSystem, windowTitle, windowWidth, windowHeight);
        window.setVisible(true);
    }

    public MainWindow getWindow() {
        return window;
    }

    /**
     * Запрашивает перерисовку окна.
     * <p>
     * Вызов происходит в потоке обработки событий AWT (Event Dispatch Thread)
     * для корректной работы с графическим интерфейсом.
     */
    public void requestRepaint() {
        java.awt.EventQueue.invokeLater(window::repaint);
    }

    @Override
    public String toString() {
        return String.format(
                "RenderingSystem[window=%dx%d, title=%s]",
                window.getWidth(),
                window.getHeight(),
                window.getTitle()
        );
    }
}
