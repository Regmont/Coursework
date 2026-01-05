package graphics;

import graphics.config.AppConfig;

public class RenderingSystem {
    private final MainWindow window;

    public RenderingSystem(SceneSystem sceneSystem) {
        window = new MainWindow(sceneSystem, AppConfig.WINDOW_TITLE,
                AppConfig.WINDOW_WIDTH, AppConfig.WINDOW_HEIGHT);
        window.setVisible(true);
    }

    public RenderingSystem(SceneSystem sceneSystem, String windowTitle, int windowWidth, int windowHeight) {
        window = new MainWindow(sceneSystem, windowTitle, windowWidth, windowHeight);
        window.setVisible(true);
    }

    public MainWindow getWindow() {
        return window;
    }

    public void requestRepaint() {
        java.awt.EventQueue.invokeLater(window::repaint);
    }
}
