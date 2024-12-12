package com.example.demo.levels;

import com.example.demo.levels.LevelOne;
import com.example.demo.actors.UserPlane;
import javafx.application.Platform;
import javafx.stage.Stage;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class LevelOneTest {

    private LevelOne levelOne;
    private Stage primaryStage;

    @BeforeAll
    static void initToolkit() {
        Platform.startup(() -> {});
    }

    @BeforeEach
    void setUp() {
        Platform.runLater(() -> {
            primaryStage = new Stage();
            levelOne = new LevelOne(750, 1300, primaryStage);
        });
        waitForRunLater();
    }

    @Test
    void testLevelInitialization() {
        Platform.runLater(() -> {
            assertNotNull(levelOne, "LevelOne should be initialized.");
            assertEquals(750, levelOne.getScreenHeight(), "Screen height should be correctly set.");
            assertEquals(1300, levelOne.getScreenWidth(), "Screen width should be correctly set.");
        });
        waitForRunLater();
    }

    @Test
    void testStartGame() {
        Platform.runLater(() -> {
            levelOne.startGame();
            assertFalse(levelOne.getRoot().getChildren().isEmpty(), "Intro screen should be displayed.");
        });
        waitForRunLater();
    }

    @Test
    void testUserInitialization() {
        Platform.runLater(() -> {
            levelOne.initializeFriendlyUnits();
            assertEquals(1, levelOne.getRoot().getChildren().size(), "User plane should be added to the scene.");
        });
        waitForRunLater();
    }

    @Test
    void testWinCondition() {
        Platform.runLater(() -> {
            UserPlane user = levelOne.getUser();
            for (int i = 0; i < 10; i++) {
                user.incrementKillCount();
            }
            levelOne.checkIfGameOver();
            assertEquals(10, user.getNumberOfKills(), "Kill count should be updated correctly.");
        });
        waitForRunLater();
    }

    @Test
    void testLoseCondition() {
        Platform.runLater(() -> {
            UserPlane user = levelOne.getUser();
            for (int i = 0; i < 5; i++) {
                user.takeDamage();
            }
            levelOne.checkIfGameOver();
            assertEquals(0, user.getHealth(), "User should have zero health after taking all damage.");
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
