package game;

import geometricObjects.OBJParser;
import geometricObjects.Triangle;
import math.Point3D;
import org.joml.Matrix4d;
import rendering.Material;
import sceneObjects.Camera;
import geometricObjects.Mesh;
import rendering.SceneTransformer;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.TimerTask;

public class GameController {
    private static final float CAMERA_SPEED = 0.1f;
    private static final float MOUSE_SENSITIVITY = 0.003f;
    private static final float DIAGONAL_FACTOR = 0.70710678118f;

    private final Camera camera;
    private final ArrayList<Mesh> objects;
    private final MainWindow window;
    private final InputHandler inputHandler;
    private final MouseHandler mouseHandler;
    private final int frameTimeMs;
    private Mesh loadedMesh;

    private boolean mouseCaptured = false;

    public GameController(Camera camera, ArrayList<Mesh> objects, MainWindow window, int targetFps) {
        this.camera = camera;
        this.objects = objects;
        this.window = window;
        this.inputHandler = new InputHandler();
        this.mouseHandler = new MouseHandler();
        frameTimeMs = 1000 / targetFps;

        try {
            BufferedImage texture = ImageIO.read(new File("textures/Sphere.png"));
            Material material = new Material(texture);

            loadedMesh = OBJParser.parseOBJ("models/Cube.obj", 1.0f);

            for (Triangle tri : loadedMesh.getTriangles()) {
                Point2D uv1 = tri.getUV1();
                Point2D uv2 = tri.getUV2();
                Point2D uv3 = tri.getUV3();

                if (uv1 == null || uv2 == null || uv3 == null) {
                    uv1 = new Point2D.Double(0, 0);
                    uv2 = new Point2D.Double(1, 0);
                    uv3 = new Point2D.Double(0, 1);
                }

                ArrayList<Point3D> points = tri.getPoints();
                Point3D[] originalPoints = tri.hasOriginalPoints() ?
                        tri.getOriginalPoints() :
                        new Point3D[]{points.get(0), points.get(1), points.get(2)};

                Triangle newTri = new Triangle(
                        points.get(0), points.get(1), points.get(2),
                        material, originalPoints, uv1, uv2, uv3
                );

                loadedMesh.getTriangles().set(
                        loadedMesh.getTriangles().indexOf(tri), newTri
                );
            }
        } catch (IOException e) { e.printStackTrace(); }

        updateTransformedScene();
    }

    public void startGameLoop() {
        java.util.Timer gameTimer = new java.util.Timer(true);
        gameTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                EventQueue.invokeLater(() -> {
                    if (inputHandler.isAnyMovementKeyPressed()) {
                        updateCameraPosition();
                    }
                    window.repaint();
                });
            }
        }, 0, frameTimeMs);
    }

    private void updateCameraPosition() {
        double forward = 0;
        double right = 0;

        if (inputHandler.isWPressed()) forward += CAMERA_SPEED;
        if (inputHandler.isSPressed()) forward -= CAMERA_SPEED;
        if (inputHandler.isAPressed()) right += CAMERA_SPEED;
        if (inputHandler.isDPressed()) right -= CAMERA_SPEED;

        if (forward != 0 && right != 0) {
            forward *= DIAGONAL_FACTOR;
            right *= DIAGONAL_FACTOR;
        }

        moveCamera(forward, right);
        updateTransformedScene();
        window.updateObjects(objects);
    }

    private void moveCamera(double forward, double right) {
        double x = camera.getX();
        double y = camera.getY();
        double z = camera.getZ();
        double yaw = camera.getYaw();

        x += forward * Math.sin(yaw);
        z += forward * Math.cos(yaw);

        x += right * Math.sin(yaw + Math.PI/2);
        z += right * Math.cos(yaw + Math.PI/2);

        camera.setPosition(x, y, z);
    }

    private void updateTransformedScene() {
        objects.clear();

        Matrix4d modelMatrix = new Matrix4d()
                .translate(0, 0, -10);

        Mesh transformedMesh = SceneTransformer.transformMesh(
                loadedMesh,
                SceneTransformer.createMVPMatrix(camera, modelMatrix)
        );

        objects.add(transformedMesh);
    }

    public void handleMouseMove(java.awt.event.MouseEvent e) {
        if (!mouseCaptured) return;

        int centerX = window.getWidth() / 2;
        int centerY = window.getHeight() / 2;

        Point mousePos = e.getPoint();
        int deltaX = mousePos.x - centerX;
        int deltaY = mousePos.y - centerY;

        if (deltaX != 0 || deltaY != 0) {
            camera.addRotation(-deltaX * MOUSE_SENSITIVITY, -deltaY * MOUSE_SENSITIVITY);
            updateTransformedScene();
            centerMouse();
        }
    }

    public void captureMouse() {
        if (!mouseCaptured) {
            mouseCaptured = true;
            window.setCursor(window.getToolkit().createCustomCursor(
                    new java.awt.image.BufferedImage(1, 1, java.awt.image.BufferedImage.TYPE_INT_ARGB),
                    new Point(0, 0),
                    "null"
            ));
            centerMouse();
        }
    }

    public void releaseMouse() {
        if (mouseCaptured) {
            mouseCaptured = false;
            window.setCursor(Cursor.getDefaultCursor());
        }
    }

    private void centerMouse() {
        if (window.isVisible() && mouseCaptured) {
            try {
                Robot robot = new Robot();
                Point windowLoc = window.getLocationOnScreen();
                int centerX = windowLoc.x + window.getWidth() / 2;
                int centerY = windowLoc.y + window.getHeight() / 2;

                robot.mouseMove(centerX, centerY);
            } catch (AWTException e) {
                e.printStackTrace();
            }
        }
    }

    public boolean isMouseCaptured() {
        return mouseCaptured;
    }

    public void setupWindowListeners(MainWindow window) {
        inputHandler.setupKeyListeners(window, this);
        mouseHandler.setupMouseListeners(window, this);
    }
}