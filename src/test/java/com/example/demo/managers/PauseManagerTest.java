package com.example.demo.managers;

import com.example.demo.levels.LevelParent;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.scene.control.Button;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.lang.reflect.Method;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class PauseManagerTest {

    private PauseManager pauseManager;
    private Pane root;
    private Stage mockStage;
    private Timeline mockTimeline;
    private LevelParent mockLevelParent;
    private ImageView mockBackground;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        Platform.runLater(() -> {
            root = new Pane();
            mockStage = new Stage();
            mockTimeline = Mockito.mock(Timeline.class);
            mockLevelParent = Mockito.mock(LevelParent.class);
            mockBackground = Mockito.mock(ImageView.class);

            pauseManager = new PauseManager(
                    root, mockStage, mockTimeline, 1300, 750, mockBackground, mockLevelParent
            );
        });
        waitForRunLater();
    }

    @Test
    void testInitializePauseButton() {
        Platform.runLater(() -> {
            pauseManager.initializePauseButton();
            Button pauseButton = (Button) root.getChildren().stream()
                    .filter(node -> node instanceof Button)
                    .findFirst().orElse(null);

            assertNotNull(pauseButton, "Pause button should be added to root.");
            assertEquals("Pause", pauseButton.getText(), "Pause button text should be 'Pause'.");
        });
        waitForRunLater();
    }

    @Test
    void testShowPauseWindow() {
        Platform.runLater(() -> {
            pauseManager.initializePauseButton();
            Button pauseButton = (Button) root.getChildren().stream()
                    .filter(node -> node instanceof Button)
                    .findFirst().orElse(null);

            assertNotNull(pauseButton);
            pauseButton.fire(); // Simulate button click

            assertTrue(root.getChildren().size() > 1, "Pause window should be displayed.");
            verify(mockTimeline, times(1)).pause();
            verify(mockLevelParent, times(1)).setGamePaused(true);
        });
        waitForRunLater();
    }

    @Test
    void testResumeGameWithCountdown() throws Exception {
        Platform.runLater(() -> {
            try {
                Button pauseButton = new Button("Pause");
                pauseManager.initializePauseButton();

                Method method = PauseManager.class.getDeclaredMethod("resumeGameWithCountdown", Button.class);
                method.setAccessible(true);
                method.invoke(pauseManager, pauseButton);

                verify(mockTimeline, times(1)).play();
                verify(mockLevelParent, times(1)).setGamePaused(false);
            } catch (Exception e) {
                fail("Error invoking private method: " + e.getMessage());
            }
        });
        waitForRunLater();
    }

    @Test
    void testGoToMainMenu() throws Exception {
        Platform.runLater(() -> {
            try {
                Method method = PauseManager.class.getDeclaredMethod("goToMainMenu");
                method.setAccessible(true);
                method.invoke(pauseManager);

                assertEquals("Sky Battle - Main Menu", mockStage.getTitle(), "Stage title should be updated.");
                verify(mockTimeline, times(1)).stop();
                verify(mockLevelParent, times(1)).setGamePaused(true);
            } catch (Exception e) {
                fail("Error invoking private method: " + e.getMessage());
            }
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
