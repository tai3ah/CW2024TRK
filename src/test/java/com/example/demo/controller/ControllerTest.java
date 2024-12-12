package com.example.demo.controller;

import com.example.demo.levels.LevelParent;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ControllerTest {

    private Controller controller;
    private Stage mockStage;
    private LevelParent mockLevel;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        Platform.runLater(() -> {
            mockStage = new Stage();
            mockLevel = Mockito.mock(LevelParent.class);
            controller = new Controller(mockStage);
        });
        waitForRunLater();
    }

    @Test
    void testLaunchGame() {
        Platform.runLater(() -> {
            controller.launchGame();
            assertTrue(mockStage.isShowing(), "Stage should be shown after launching the game.");
        });
        waitForRunLater();
    }

    @Test
    void testTransitionToNextLevel() {
        Platform.runLater(() -> {
            when(mockLevel.initializeScene()).thenReturn(new Scene(mockLevel.getRoot()));
            controller.launchGame();
            verify(mockLevel, times(1)).initializeScene();
        });
        waitForRunLater();
    }

    @Test
    void testTransitionToNextLevelWithCleanup() {
        Platform.runLater(() -> {
            when(mockLevel.getRoot()).thenReturn(mockLevel.getRoot());
            when(mockLevel.getTimeline()).thenReturn(mockLevel.getTimeline());

            // Simulate level transition
            controller.launchGame();

            // Verify cleanup occurred
            verify(mockLevel, times(1)).stopGame();
            verify(mockLevel.getRoot(), times(1)).getChildren().clear();
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
