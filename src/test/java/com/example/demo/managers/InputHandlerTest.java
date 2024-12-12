package com.example.demo.managers;

import com.example.demo.actors.UserPlane;
import javafx.application.Platform;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

class InputHandlerTest {

    // Correct mock class extending UserPlane
    static class TestPlane extends UserPlane {

        boolean movedUp, movedDown, movedLeft, movedRight, stopped;

        public TestPlane() {
            super(5);  // Initialize UserPlane with a dummy health value
        }

        @Override
        public void moveUp() { movedUp = true; }
        @Override
        public void moveDown() { movedDown = true; }
        @Override
        public void moveLeft() { movedLeft = true; }
        @Override
        public void moveRight() { movedRight = true; }
        @Override
        public void stop() { stopped = true; }
    }

    private TestPlane testPlane;
    private ImageView mockBackground;
    private InputHandler inputHandler;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        testPlane = new TestPlane();  // Correctly initialize TestPlane
        mockBackground = new ImageView();
        inputHandler = new InputHandler(testPlane, mockBackground);
    }

    @Test
    void testKeyPressUp() {
        fireEvent(mockBackground, new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.UP, false, false, false, false));
        assertTrue(testPlane.movedUp);
    }

    @Test
    void testKeyPressDown() {
        fireEvent(mockBackground, new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.DOWN, false, false, false, false));
        assertTrue(testPlane.movedDown);
    }

    @Test
    void testKeyPressLeft() {
        fireEvent(mockBackground, new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.LEFT, false, false, false, false));
        assertTrue(testPlane.movedLeft);
    }

    @Test
    void testKeyPressRight() {
        fireEvent(mockBackground, new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.RIGHT, false, false, false, false));
        assertTrue(testPlane.movedRight);
    }

    @Test
    void testKeyPressSpace() {
        final boolean[] fired = {false};
        inputHandler.setOnFire(() -> fired[0] = true);

        fireEvent(mockBackground, new KeyEvent(KeyEvent.KEY_PRESSED, "", "", KeyCode.SPACE, false, false, false, false));
        assertTrue(fired[0]);
    }

    @Test
    void testKeyReleaseStopsUserPlane() {
        fireEvent(mockBackground, new KeyEvent(KeyEvent.KEY_RELEASED, "", "", KeyCode.UP, false, false, false, false));
        assertTrue(testPlane.stopped);
    }

    // Correct event firing using Platform.runLater and waiting
    private void fireEvent(ImageView target, KeyEvent event) {
        Platform.runLater(() -> target.fireEvent(event));
        waitForRunLater();  // Ensure JavaFX processes the event before moving on
    }

    // Wait until JavaFX thread completes all tasks
    private void waitForRunLater() {
        try {
            Platform.runLater(() -> {});  // Submit an empty task
            Thread.sleep(100);  // Give time for event processing
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();  // Restore interrupt status
        }
    }
}
