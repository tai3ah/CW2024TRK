package com.example.demo.factories;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LevelEndScreenFactoryTest {

    private LevelEndScreenFactory levelEndScreenFactory;
    private Pane root;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        Platform.runLater(() -> {
            root = new Pane();
            levelEndScreenFactory = new LevelEndScreenFactory();
            levelEndScreenFactory.initialize(root, 1300, 750);
        });
        waitForRunLater();
    }

    @Test
    void testInitialize() {
        Platform.runLater(() -> {
            assertNotNull(root, "Root should be initialized.");
            assertEquals(0, root.getChildren().size(), "Root should be empty after initialization.");
        });
        waitForRunLater();
    }

    @Test
    void testShowWinLevelScreen() {
        Platform.runLater(() -> {
            Runnable mockPlayAgain = mock(Runnable.class);
            Runnable mockNextLevel = mock(Runnable.class);

            levelEndScreenFactory.showWinLevelScreen(mockPlayAgain, mockNextLevel);

            assertTrue(root.getChildren().stream().anyMatch(node -> node instanceof Text && ((Text) node).getText().equals("GOOD JOB")), "Message text should be 'GOOD JOB'.");
            assertTrue(root.getChildren().stream().anyMatch(node -> node instanceof Button && ((Button) node).getText().equals("Play Again")), "Play Again button should be displayed.");
            assertTrue(root.getChildren().stream().anyMatch(node -> node instanceof Button && ((Button) node).getText().equals("Next Level")), "Next Level button should be displayed.");
        });
        waitForRunLater();
    }

    @Test
    void testShowLoseLevelScreen() {
        Platform.runLater(() -> {
            Runnable mockPlayAgain = mock(Runnable.class);

            levelEndScreenFactory.showLoseLevelScreen(mockPlayAgain);

            assertTrue(root.getChildren().stream().anyMatch(node -> node instanceof Text && ((Text) node).getText().equals("BETTER LUCK NEXT TIME")), "Message text should be 'BETTER LUCK NEXT TIME'.");
            assertTrue(root.getChildren().stream().anyMatch(node -> node instanceof Button && ((Button) node).getText().equals("Play Again")), "Play Again button should be displayed.");
            assertFalse(root.getChildren().stream().anyMatch(node -> node instanceof Button && ((Button) node).getText().equals("Next Level")), "Next Level button should not be displayed.");
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
