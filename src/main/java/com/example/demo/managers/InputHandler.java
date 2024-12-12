package com.example.demo.managers;

import com.example.demo.actors.UserPlane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyEvent;

/**
 * The InputHandler class manages user input for controlling the user plane.
 * It handles key presses and releases to move the plane and fire projectiles.
 */
public class InputHandler {
    private final UserPlane userPlane;
    private final ImageView background;
    private Runnable onFire;

    /**
     * Constructs an InputHandler with the specified user plane and background.
     *
     * @param userPlane the user plane controlled by the player
     * @param background the background image view
     */
    public InputHandler(UserPlane userPlane, ImageView background) {
        this.userPlane = userPlane;
        this.background = background;
        initializeInputHandlers();
    }

    /**
     * Initializes input handlers for key presses and releases.
     */
    public void initializeInputHandlers() {
        background.setFocusTraversable(true);
        background.setOnKeyPressed(this::handleKeyPressed);
        background.setOnKeyReleased(this::handleKeyReleased);
    }

    /**
     * Handles key press events to move the user plane or fire a projectile.
     *
     * @param e the key event
     */
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

    /**
     * Handles key release events to stop the user plane's movement.
     *
     * @param e the key event
     */
    private void handleKeyReleased(KeyEvent e) {
        switch (e.getCode()) {
            case UP, DOWN, LEFT, RIGHT -> userPlane.stop();
        }
    }

    /**
     * Sets the action to be performed when the fire key is pressed.
     *
     * @param onFire the action to be performed
     */
    public void setOnFire(Runnable onFire) {
        this.onFire = onFire;
    }
}