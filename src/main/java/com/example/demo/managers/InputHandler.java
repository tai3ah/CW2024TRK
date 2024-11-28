package com.example.demo.managers;

import com.example.demo.actors.UserPlane;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

public class InputHandler {

    private final UserPlane userPlane;
    private final ImageView background;

    public InputHandler(UserPlane userPlane, ImageView background) {
        this.userPlane = userPlane;
        this.background = background;
    }

    public void initializeInputHandlers() {
        background.setOnKeyPressed(this::handleKeyPressed);
        background.setOnKeyReleased(this::handleKeyReleased);
    }

    private void handleKeyPressed(KeyEvent e) {
        KeyCode kc = e.getCode();
        if (kc == KeyCode.UP) {
            userPlane.moveUp();
        }
        if (kc == KeyCode.DOWN) {
            userPlane.moveDown();
        }
        if (kc == KeyCode.SPACE) {
            // Let LevelParent handle firing, since it has access to the root Pane
            if (onFire != null) {
                onFire.run();
            }
        }
    }

    private void handleKeyReleased(KeyEvent e) {
        KeyCode kc = e.getCode();
        if (kc == KeyCode.UP || kc == KeyCode.DOWN) {
            userPlane.stop();
        }
    }

    // Callback for firing projectiles
    private Runnable onFire;

    public void setOnFire(Runnable onFire) {
        this.onFire = onFire;
    }
}
