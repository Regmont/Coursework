package game;

import game.configuration.GameConfig;
import game.input.InputSystem;
import game.input.Keyboard;
import game.input.Mouse;
import core.math.Vector3D;
import graphics.RenderingSystem;
import graphics.SceneSystem;
import scene.gameObjects.Camera;

public class GameController {
    private final double FRAME_TIME_MS = 1000.0 / GameConfig.TARGET_FPS;
    private final RenderingSystem renderingSystem;
    private final Camera camera;
    private final InputSystem inputSystem;
    private volatile boolean running;
    private Thread gameThread;
    private boolean cameraMoved = false;

    public GameController(SceneSystem sceneSystem, RenderingSystem renderingSystem) {
        this.renderingSystem = renderingSystem;
        this.camera = sceneSystem.getCamera();
        this.inputSystem = new InputSystem(renderingSystem.getWindow());
    }

    public void startGameLoop() {
        running = true;

        gameThread = new Thread(this::gameLoop, "Game-Thread");
        gameThread.start();
    }

    public void stopGameLoop() {
        running = false;

        if (gameThread != null) {
            try {
                gameThread.join();
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
    }

    private void gameLoop() {
        long lastTime = System.nanoTime();
        double accumulator = 0;

        while (running) {
            long currentTime = System.nanoTime();
            double elapsedMs = (currentTime - lastTime) / 1_000_000.0;
            lastTime = currentTime;

            if (elapsedMs > GameConfig.MAX_FRAME_TIME_MS) {
                elapsedMs = GameConfig.MAX_FRAME_TIME_MS;
            }

            accumulator += elapsedMs;

            while (accumulator >= FRAME_TIME_MS) {
                update(FRAME_TIME_MS / 1000.0);
                accumulator -= FRAME_TIME_MS;
            }

            if (cameraMoved) {
                renderingSystem.requestRepaint();
                cameraMoved = false;
            }

            long sleepTimeMs = (long)(FRAME_TIME_MS - (System.nanoTime() - lastTime) / 1_000_000.0);

            if (sleepTimeMs > 0) {
                try {
                    Thread.sleep(sleepTimeMs);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    running = false;
                }
            }
        }
    }

    private void update(double deltaTime) {
        Keyboard keyboard = inputSystem.getKeyboard();
        Mouse mouse = inputSystem.getMouse();

        if (keyboard.isEscapePressed()) {
            if (mouse.isCaptured()) {
                mouse.release();
            }

            keyboard.resetEscape();
        }

        if (mouse.isCaptured()) {
            int deltaX = mouse.getDeltaX();
            int deltaY = mouse.getDeltaY();

            if (deltaX != 0 || deltaY != 0) {
                double deltaYaw = -deltaX * GameConfig.MOUSE_SENSITIVITY;
                double deltaPitch = deltaY * GameConfig.MOUSE_SENSITIVITY;

                double newPitch = camera.getTransform().getRotation().getX() + deltaPitch;
                double newYaw = camera.getTransform().getRotation().getY() + deltaYaw;

                newPitch = Math.max(GameConfig.MIN_PITCH, Math.min(GameConfig.MAX_PITCH, newPitch));

                camera.getTransform().setRotation(new Vector3D(newPitch, newYaw,
                        camera.getTransform().getRotation().getZ()));
                cameraMoved = true;
            }
        }

        if (keyboard.isAnyMovementPressed()) {
            updateCameraPosition(deltaTime);
            cameraMoved = true;
        }

        inputSystem.update();
    }

    private void updateCameraPosition(double deltaTime) {
        Keyboard keyboard = inputSystem.getKeyboard();

        double forward = 0;
        double right = 0;

        if (keyboard.isWPressed()) forward += 1;
        if (keyboard.isSPressed()) forward -= 1;
        if (keyboard.isAPressed()) right += 1;
        if (keyboard.isDPressed()) right -= 1;

        double cameraSpeedDiagonalFactor = Math.sqrt(2) / 2;

        if (forward != 0 && right != 0) {
            forward *= cameraSpeedDiagonalFactor;
            right *= cameraSpeedDiagonalFactor;
        }

        forward *= camera.getSpeed() * deltaTime;
        right *= camera.getSpeed() * deltaTime;

        moveCamera(forward, right);
    }

    private void moveCamera(double forward, double right) {
        Vector3D cameraPosition = camera.getTransform().getPosition();
        double yaw = camera.getTransform().getRotation().getY();

        double x = cameraPosition.getX() +
                forward * Math.sin(yaw) +
                right * Math.sin(yaw + Math.PI / 2);

        double z = cameraPosition.getZ() +
                forward * Math.cos(yaw) +
                right * Math.cos(yaw + Math.PI / 2);

        camera.getTransform().setPosition(new Vector3D(x, cameraPosition.getY(), z));
    }
}
