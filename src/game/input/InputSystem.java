package game.input;

import java.awt.*;

public class InputSystem {
    private final Keyboard keyboard;
    private final Mouse mouse;

    public InputSystem(Component window) {
        keyboard = new Keyboard(window);
        mouse = new Mouse(window);
    }

    public Keyboard getKeyboard() { return keyboard; }
    public Mouse getMouse() { return mouse; }

    public void update() {
        mouse.update();
    }
}
