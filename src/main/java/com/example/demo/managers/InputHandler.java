
package com.example.demo.managers;

import com.example.demo.actors.UserPlane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

public class InputHandler {
    private final UserPlane userPlane;
    private final ImageView background;
    private Runnable onFire;

    public InputHandler(UserPlane userPlane, ImageView background) {
        this.userPlane = userPlane;
        this.background = background;
        initializeInputHandlers();
    }

    public void initializeInputHandlers() {
        background.setFocusTraversable(true);
        background.setOnKeyPressed(this::handleKeyPressed);
        background.setOnKeyReleased(this::handleKeyReleased);
    }

    private void handleKeyPressed(KeyEvent e) {
        switch (e.getCode()) {
            case UP -> userPlane.moveUp();
            case DOWN -> userPlane.moveDown();
            case LEFT -> userPlane.moveLeft();
            case RIGHT -> userPlane.moveRight();
            case SPACE -> {
                if (onFire != null) {
                    onFire.run();
                }
            }
        }
    }

    private void handleKeyReleased(KeyEvent e) {
        switch (e.getCode()) {
            case UP, DOWN, LEFT, RIGHT -> userPlane.stop();
        }
    }

    public void setOnFire(Runnable onFire) {
        this.onFire = onFire;
    }
}
