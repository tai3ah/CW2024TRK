package com.example.demo.ui;

import com.example.demo.ui.ShieldImage;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ShieldImageTest {

    private ShieldImage shieldImage;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        Platform.runLater(() -> {
            shieldImage = new ShieldImage(100, 100);
        });
        waitForRunLater();
    }

    @Test
    void testShieldImageInitialization() {
        Platform.runLater(() -> {
            assertNotNull(shieldImage, "ShieldImage should be initialized.");
            assertEquals(100, shieldImage.getLayoutX(), "Shield X position should be 100.");
            assertEquals(100, shieldImage.getLayoutY(), "Shield Y position should be 100.");
            assertEquals(400, shieldImage.getFitWidth(), "Shield width should be 400.");
            assertEquals(400, shieldImage.getFitHeight(), "Shield height should be 400.");
            assertFalse(shieldImage.isVisible(), "Shield should be hidden initially.");
        });
        waitForRunLater();
    }

    @Test
    void testShowShield() {
        Platform.runLater(() -> {
            shieldImage.showShield();
            assertTrue(shieldImage.isVisible(), "Shield should be visible after activation.");
        });
        waitForRunLater();
    }

    @Test
    void testHideShield() {
        Platform.runLater(() -> {
            shieldImage.showShield();
            shieldImage.hideShield();
            assertFalse(shieldImage.isVisible(), "Shield should be hidden after deactivation.");
        });
        waitForRunLater();
    }

    @Test
    void testUpdatePosition() {
        Platform.runLater(() -> {
            shieldImage.updatePosition(200, 300);
            assertEquals(200, shieldImage.getTranslateX(), "Shield X translation should be 200.");
            assertEquals(300, shieldImage.getTranslateY(), "Shield Y translation should be 300.");
        });
        waitForRunLater();
    }

    private void waitForRunLater() {
        try {
            Platform.runLater(() -> {});  // Dummy task to sync JavaFX thread
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
