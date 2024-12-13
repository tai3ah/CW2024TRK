package com.example.demo.factories;

import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LevelEndScreenFactoryTest {

    private LevelEndScreenFactory levelEndScreenFactory;
    private Pane root;
    private Stage primaryStage;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        Platform.runLater(() -> {
            root = new Pane();
            primaryStage = new Stage();
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

            levelEndScreenFactory.showWinLevelScreen(mockPlayAgain, mockNextLevel, primaryStage);

            assertTrue(root.getChildren().stream()
                            .anyMatch(node -> node instanceof Text && ((Text) node).getText().equals("GOOD JOB")),
                    "Message text should be 'GOOD JOB'.");
            assertTrue(root.getChildren().stream()
                            .anyMatch(node -> node instanceof Button && ((Button) node).getText().equals("Play Again")),
                    "Play Again button should be displayed.");
            assertTrue(root.getChildren().stream()
                            .anyMatch(node -> node instanceof Button && ((Button) node).getText().equals("Next Level")),
                    "Next Level button should be displayed.");
            assertTrue(root.getChildren().stream()
                            .anyMatch(node -> node instanceof Button && ((Button) node).getText().equals("Main Menu")),
                    "Main Menu button should be displayed.");
        });
        waitForRunLater();
    }

    @Test
    void testShowLoseLevelScreen() {
        Platform.runLater(() -> {
            Runnable mockPlayAgain = mock(Runnable.class);

            levelEndScreenFactory.showLoseLevelScreen(mockPlayAgain, primaryStage);

            assertTrue(root.getChildren().stream()
                            .anyMatch(node -> node instanceof Text && ((Text) node).getText().equals("BETTER LUCK NEXT TIME")),
                    "Message text should be 'BETTER LUCK NEXT TIME'.");
            assertTrue(root.getChildren().stream()
                            .anyMatch(node -> node instanceof Button && ((Button) node).getText().equals("Play Again")),
                    "Play Again button should be displayed.");
            assertFalse(root.getChildren().stream()
                            .anyMatch(node -> node instanceof Button && ((Button) node).getText().equals("Next Level")),
                    "Next Level button should not be displayed.");
            assertTrue(root.getChildren().stream()
                            .anyMatch(node -> node instanceof Button && ((Button) node).getText().equals("Main Menu")),
                    "Main Menu button should be displayed.");
        });
        waitForRunLater();
    }

    private void waitForRunLater() {
        try {
            Platform.runLater(() -> {});
            Thread.sleep(100);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
