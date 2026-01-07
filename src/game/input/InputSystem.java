package game.input;

import java.awt.*;

/**
 * Центральная система ввода, объединяющая клавиатуру и мышь.
 * <p>
 * Предоставляет единый интерфейс для доступа к устройствам ввода
 * и управления их обновлением.
 *
 * @author Дунин Михаил Сергеевич
 * @version 1.0
 */
public class InputSystem {
    private final Keyboard keyboard;
    private final Mouse mouse;

    /**
     * Создает систему ввода для указанного окна.
     *
     * @param window компонент окна для привязки слушателей ввода
     */
    public InputSystem(Component window) {
        keyboard = new Keyboard(window);
        mouse = new Mouse(window);
    }

    public Keyboard getKeyboard() { return keyboard; }
    public Mouse getMouse() { return mouse; }

    /**
     * Обновляет состояние устройств ввода. Должен вызываться каждый кадр.
     * <p>
     * В настоящее время обновляет только мышь (центрирование курсора,
     * сглаживание движения).
     */
    public void update() {
        mouse.update();
    }

    @Override
    public String toString() {
        return String.format(
                "InputSystem[keyboard=%s, mouse=%s]",
                keyboard,
                mouse
        );
    }
}
