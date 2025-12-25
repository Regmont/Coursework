package game;

import game.input.InputSystem;
import game.input.Keyboard;
import game.input.Mouse;
import math.Vector3D;
import scene.Camera;

import java.awt.*;

public class GameController {
    private static final double DIAGONAL_FACTOR = Math.sqrt(2) / 2;

    private final Camera camera;
    private final MainWindow window;
    private final InputSystem inputSystem;
    private final double mouseSensitivity;
    private final double frameTimeMs;
    private volatile boolean running;
    private Thread gameThread;

    public GameController(Camera camera, MainWindow window, int targetFps, double mouseSensitivity) {
        this.camera = camera;
        this.window = window;
        this.inputSystem = new InputSystem(window);
        this.frameTimeMs = 1000.0 / targetFps;
        this.mouseSensitivity = mouseSensitivity;
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

            if (elapsedMs > 250) {
                elapsedMs = 250;
            }

            accumulator += elapsedMs;

            while (accumulator >= frameTimeMs) {
                update(frameTimeMs / 1000.0);
                accumulator -= frameTimeMs;
            }

            EventQueue.invokeLater(window::repaint);

            long sleepTimeMs = (long)(frameTimeMs - (System.nanoTime() - lastTime) / 1_000_000.0);
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
            } else {
                mouse.capture();
            }

            keyboard.resetEscape();
        }

        if (mouse.isCaptured()) {
            int deltaX = mouse.getDeltaX();
            int deltaY = mouse.getDeltaY();
            if (deltaX != 0 || deltaY != 0) {
                camera.addRotation(
                        -deltaX * mouseSensitivity,
                        -deltaY * mouseSensitivity
                );
            }
        }

        if (keyboard.isAnyMovementPressed()) {
            updateCameraPosition(deltaTime);
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

        if (forward != 0 && right != 0) {
            forward *= DIAGONAL_FACTOR;
            right *= DIAGONAL_FACTOR;
        }

        forward *= camera.getSpeed() * deltaTime;
        right *= camera.getSpeed() * deltaTime;

        moveCamera(forward, right);
    }

    private void moveCamera(double forward, double right) {
        Vector3D cameraPosition = camera.getPosition();
        double yaw = camera.getRotation().getX();

        double x = cameraPosition.getX() +
                forward * Math.sin(yaw) +
                right * Math.sin(yaw + Math.PI / 2);

        double z = cameraPosition.getZ() +
                forward * Math.cos(yaw) +
                right * Math.cos(yaw + Math.PI / 2);

        camera.setPosition(new Vector3D(x, cameraPosition.getY(), z));
    }
}