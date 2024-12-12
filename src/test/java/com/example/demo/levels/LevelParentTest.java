package com.example.demo.levels;

import com.example.demo.managers.SoundManager;
import javafx.animation.Animation;
import javafx.application.Platform;
import javafx.scene.Scene;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.Mockito;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LevelParentTest {

    private LevelParentTestHelper level;
    private Stage mockStage;

    @Mock
    private SoundManager mockSoundManager;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        Platform.runLater(() -> {
            mockStage = new Stage();
            mockSoundManager = Mockito.mock(SoundManager.class);
            level = new LevelParentTestHelper(mockStage);
        });
        waitForRunLater();
    }

    @Test
    void testInitializeScene() {
        Platform.runLater(() -> {
            Scene scene = level.initializeScene();
            assertNotNull(scene, "Scene should be initialized.");
            assertEquals(1300, scene.getWidth(), "Scene width should be 1300.");
            assertEquals(750, scene.getHeight(), "Scene height should be 750.");
        });
        waitForRunLater();
    }

    @Test
    void testStartGame() {
        Platform.runLater(() -> {
            level.startGame();
            verify(mockSoundManager, times(1)).playBackgroundSound();
            assertEquals(level.getTimeline().getStatus(), Animation.Status.RUNNING, "Timeline should be running after game start.");
        });
        waitForRunLater();
    }

    @Test
    void testWinGame() {
        Platform.runLater(() -> {
            level.winGame();
            verify(mockSoundManager, times(1)).playWinSound();
            assertNotEquals(level.getTimeline().getStatus(), Animation.Status.RUNNING, "Timeline should be stopped after winning the game.");
        });
        waitForRunLater();
    }

    @Test
    void testLoseGame() {
        Platform.runLater(() -> {
            level.loseGame();
            verify(mockSoundManager, times(1)).playLoseSound();
            assertNotEquals(level.getTimeline().getStatus(), Animation.Status.RUNNING, "Timeline should be stopped after losing the game.");
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

// Helper class for testing LevelParent
class LevelParentTestHelper extends LevelParent {

    public LevelParentTestHelper(Stage primaryStage) {
        super("/com/example/demo/images/testBackground.png", 750, 1300, 5, primaryStage);
    }

    @Override
    protected void initializeFriendlyUnits() {
        // No-op for testing
    }

    @Override
    protected void checkIfGameOver() {
        // No-op for testing
    }

    @Override
    protected void spawnEnemyUnits() {
        // No-op for testing
    }

    @Override
    protected void goToNextLevel() {
        // No-op for testing
    }

    @Override
    protected com.example.demo.ui.LevelViewLevelOne instantiateLevelView() {
        return Mockito.mock(com.example.demo.ui.LevelViewLevelOne.class);
    }
}
